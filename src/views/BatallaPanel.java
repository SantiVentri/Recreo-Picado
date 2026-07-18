package views;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import enums.ACCIONES;
import modelo.Curandera;
import modelo.Entidad;
import modelo.Pocion;
import modelo.Repositorio;
import orquestador.Orquestador;


public class BatallaPanel extends JPanel {

    private CharacterOrderPanel characterOrderPanel;
    private VentanaLayout ventana;

    // Estado de selección de objetivo
    private ACCIONES accionPendiente = null;
    private boolean esperandoObjetivo = false;
    private int indiceObjetivoActual = 0;

    // Referencias a EntidadViews
    private List<EntidadView> viewsAlumnos  = new ArrayList<>();
    private List<EntidadView> viewsEnemigos = new ArrayList<>();

    // Flechas flotantes sobre alumnos y enemigos
    private List<JLabel> flechasAlumnos = new ArrayList<>();
    private List<JLabel> flechasEnemigos = new ArrayList<>();

    // Botones
    private EntidadView viewActual;
    private JButton btnAtacar, btnDefender, btnHabilidad, btnItem, btnHuir;
    
    // Popup de inventario (pociones) usado por el botón "Usar Item"
    private InventarioBatallaView inventarioBatallaView;
    private JPanel overlayInventario;
    private ResultadoBatallaView resultadoBatallaView;
    private JPanel overlayResultado;

    // Tamaño de la flecha renderizada
    private static final int FLECHA_W = 48;
    private static final int FLECHA_H = 48;
    // Píxeles por encima del borde superior del EntidadView
    private static final int FLECHA_OFFSET_Y = 55;

    public BatallaPanel(VentanaLayout ventana) {
        this.ventana = ventana;
        setLayout(null);
    }

    // ─── CARGA INICIAL ────────────────────────────────────────────────────────

    public void cargarPanel() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);

        btnAtacar = new JButton("Atacar");
        btnAtacar.setPreferredSize(new Dimension(120, 42));
        btnAtacar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activarSeleccionEnemigos(ACCIONES.ATACAR);
            }
        });
        panelBotones.add(btnAtacar);

        btnDefender = new JButton("Defender");
        btnDefender.setPreferredSize(new Dimension(120, 42));
        btnDefender.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Orquestador.getInstance().ejecutarTurno(ACCIONES.DEFENDER, null, null);
                onAccionIniciada();
            }
        });
        panelBotones.add(btnDefender);

        btnHabilidad = new JButton("Usar Habilidad");
        btnHabilidad.setPreferredSize(new Dimension(140, 42));
        btnHabilidad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Entidad actual = Orquestador.getInstance().getEntidadActual();
                if (actual instanceof Curandera) {
                    activarSeleccionAlumnos(ACCIONES.USAR_HABILIDAD);
                } else {
                    activarSeleccionEnemigos(ACCIONES.USAR_HABILIDAD);
                }
            }
        });
        panelBotones.add(btnHabilidad);

        btnItem = new JButton("Usar Item");
        btnItem.setPreferredSize(new Dimension(120, 42));
        btnItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirInventarioBatalla();
            }
        });
        panelBotones.add(btnItem);

        btnHuir = new JButton("Huir");
        btnHuir.setPreferredSize(new Dimension(120, 42));
        btnHuir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(
                    ventana,
                    "¿Estás seguro de que querés huir? Vas a perder el progreso",
                    "Confirmar acción",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                if (respuesta == JOptionPane.YES_OPTION) {
                    Orquestador.getInstance().reiniciar();
                    ventana.verNiveles();
                }
            }
        });
        panelBotones.add(btnHuir);

        Dimension tamañoReal = panelBotones.getPreferredSize();
        panelBotones.setBounds(
            1010 - tamañoReal.width - 40,
            610 - tamañoReal.height - 70,
            tamañoReal.width,
            tamañoReal.height
        );
        add(panelBotones);

        characterOrderPanel = new CharacterOrderPanel();
        characterOrderPanel.setBounds(-15, -35, 270, 190);
        add(characterOrderPanel);
        
        // --- OVERLAY + INVENTARIO DE POCIONES ---
        overlayInventario = new JPanel();
        overlayInventario.setOpaque(false);
        overlayInventario.setVisible(false);
        overlayInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cerrarInventarioBatalla();
            }
        });
        add(overlayInventario);

        inventarioBatallaView = new InventarioBatallaView();
        inventarioBatallaView.setVisible(false);
        inventarioBatallaView.setClickListener(new InventarioBatallaView.ClickListener() {
            public void onPocionSeleccionada(Pocion pocion) {
                usarPocionSeleccionada(pocion);
            }
        });
        add(inventarioBatallaView);

        // --- OVERLAY + PANEL DE RESULTADO DE BATALLA ---
        overlayResultado = new JPanel();
        overlayResultado.setOpaque(false);
        overlayResultado.setVisible(false);
        add(overlayResultado);

        resultadoBatallaView = new ResultadoBatallaView();
        resultadoBatallaView.setVisible(false);
        resultadoBatallaView.setClickListener(new ResultadoBatallaView.ClickListener() {
            public void onContinuar() {
                cerrarResultadoBatalla();
                if (Orquestador.getInstance().alumnosGanaron()) {
                    Orquestador.getInstance().terminarBatalla();
                } else {
                    Orquestador.getInstance().reiniciar();
                }
                ventana.verNiveles();
            }
        });
        add(resultadoBatallaView);
    }

    public void cargarEntidades() {
        if (Orquestador.getInstance().getBatalla() == null ||
            Orquestador.getInstance().getAlumnos() == null) return;

        viewsAlumnos.clear();
        viewsEnemigos.clear();
        flechasAlumnos.clear();
        flechasEnemigos.clear();

        List<Entidad> alumnos  = Orquestador.getInstance().getAlumnos().getEntidades();
        List<Entidad> enemigos = Orquestador.getInstance().getBatalla().getEnemigos().getEntidades();

        int alumnoBaseX = 330;
        int enemigoBaseX = 520;
        int baseY = 120;
        int pasoX = 110;
        int pasoY = 30;
        int evAncho = 140;
        int evAlto = 360;

        Image imgFlecha = cargarFlecha();

        // ALUMNOS + flechas
        JLabel[] flechasAlumnosArr = new JLabel[alumnos.size()];
        for (int i = 0; i < alumnos.size(); i++) {
            final int idx = i;
            EntidadView ev = new EntidadView(alumnos.get(i));
            ev.setMostrarHUD(true);
            ev.setMirandoIzquierda(false);
            
            int evX = alumnoBaseX - i * pasoX;
            int evY = baseY + i * pasoY;
            ev.setBounds(evX, evY, evAncho, evAlto);

            ev.setClickListener(new EntidadView.ClickListener() {
                public void onClick(EntidadView origen, int clickX) {
                    if (!esperandoObjetivo || accionPendiente != ACCIONES.USAR_HABILIDAD) return;
                    Entidad objetivo = Orquestador.getInstance().getAlumnos().getEntidades().get(idx);
                    if (!objetivo.estaVivo()) return;

                    int centro = origen.getWidth() / 2;
                    if (clickX < centro - 40 || clickX > centro + 40) return;

                    ocultarFlechas();
                    indiceObjetivoActual = idx;

                    Orquestador.getInstance().ejecutarTurno(accionPendiente, objetivo, null);
                    esperandoObjetivo = false;
                    accionPendiente   = null;
                    onAccionIniciada();
                }
            });

            ev.setHoverListener(new EntidadView.HoverListener() {
                public void onEnter() {
                    if (!esperandoObjetivo || accionPendiente != ACCIONES.USAR_HABILIDAD) return;
                    Entidad e = Orquestador.getInstance().getAlumnos().getEntidades().get(idx);
                    if (!e.estaVivo()) return;
                    ocultarFlechas();
                    flechasAlumnos.get(idx).setVisible(true);
                    indiceObjetivoActual = idx;
                }
                public void onExit() {
                    if (!esperandoObjetivo || accionPendiente != ACCIONES.USAR_HABILIDAD) return;
                    ocultarFlechas();
                    flechasAlumnos.get(indiceObjetivoActual).setVisible(true);
                }
            });

            viewsAlumnos.add(ev);

            // Preparar flecha de alumno
            JLabel lblFlecha = new JLabel();
            if (imgFlecha != null) {
                Image flechaEscalada = imgFlecha.getScaledInstance(FLECHA_W, FLECHA_H, Image.SCALE_SMOOTH);
                lblFlecha.setIcon(new ImageIcon(flechaEscalada));
            }
            int flechaX = evX + (140 / 2) - (FLECHA_W / 2);
            int flechaY = evY - FLECHA_OFFSET_Y;
            lblFlecha.setBounds(flechaX, flechaY, FLECHA_W, FLECHA_H);
            lblFlecha.setVisible(false);
            flechasAlumnos.add(lblFlecha);
            flechasAlumnosArr[i] = lblFlecha;
        }

        // Agregar alumnos al panel de atrás hacia adelante para z-order correcto
        for (int i = alumnos.size() - 1; i >= 0; i--) {
            add(viewsAlumnos.get(i));
            add(flechasAlumnosArr[i]);
        }

        // ENEMIGOS + flechas
        JLabel[] flechasEnemigosArr = new JLabel[enemigos.size()];

        for (int i = 0; i < enemigos.size(); i++) {
            final int idx = i;

            EntidadView ev = new EntidadView(enemigos.get(i));
            ev.setMostrarHUD(true);
            ev.setMirandoIzquierda(true);
            int evX = enemigoBaseX + i * pasoX;
            int evY = baseY + i * pasoY;
            ev.setBounds(evX, evY, evAncho, evAlto);

            ev.setClickListener(new EntidadView.ClickListener() {
                public void onClick(EntidadView origen, int clickX) {
                    if (!esperandoObjetivo || accionPendiente == ACCIONES.USAR_HABILIDAD && Orquestador.getInstance().getEntidadActual() instanceof Curandera) return;
                    Entidad objetivo = Orquestador.getInstance()
                        .getBatalla().getEnemigos().getEntidades().get(idx);
                    if (!objetivo.estaVivo()) return;

                    int centro = origen.getWidth() / 2;
                    if (clickX < centro - 40 || clickX > centro + 40) return;

                    ocultarFlechas();
                    indiceObjetivoActual = idx;

                    Orquestador.getInstance().ejecutarTurno(accionPendiente, objetivo, null);
                    esperandoObjetivo = false;
                    accionPendiente   = null;
                    onAccionIniciada();
                }
            });

            ev.setHoverListener(new EntidadView.HoverListener() {
                public void onEnter() {
                    if (!esperandoObjetivo || accionPendiente == ACCIONES.USAR_HABILIDAD && Orquestador.getInstance().getEntidadActual() instanceof Curandera) return;
                    Entidad e = Orquestador.getInstance()
                        .getBatalla().getEnemigos().getEntidades().get(idx);
                    if (!e.estaVivo()) return;
                    ocultarFlechas();
                    flechasEnemigos.get(idx).setVisible(true);
                    indiceObjetivoActual = idx;
                }
                public void onExit() {
                    if (!esperandoObjetivo || accionPendiente == ACCIONES.USAR_HABILIDAD && Orquestador.getInstance().getEntidadActual() instanceof Curandera) return;
                    ocultarFlechas();
                    flechasEnemigos.get(indiceObjetivoActual).setVisible(true);
                }
            });

            viewsEnemigos.add(ev);

            // Preparar flecha de enemigo
            JLabel lblFlecha = new JLabel();
            if (imgFlecha != null) {
                Image flechaEscalada = imgFlecha.getScaledInstance(FLECHA_W, FLECHA_H, Image.SCALE_SMOOTH);
                lblFlecha.setIcon(new ImageIcon(flechaEscalada));
            }
            int flechaX = evX + (140 / 2) - (FLECHA_W / 2);
            int flechaY = evY - FLECHA_OFFSET_Y;
            lblFlecha.setBounds(flechaX, flechaY, FLECHA_W, FLECHA_H);
            lblFlecha.setVisible(false);
            flechasEnemigos.add(lblFlecha);
            flechasEnemigosArr[i] = lblFlecha;
        }

        // Agregar enemigos al panel de atrás hacia adelante para z-order correcto
        for (int i = enemigos.size() - 1; i >= 0; i--) {
            add(viewsEnemigos.get(i));
            add(flechasEnemigosArr[i]);
        }

        setBotonesHabilitados(true);
        actualizarViewActual();
        revalidate();
        repaint();
    }

    private Image cargarFlecha() {
        try {
            return new ImageIcon(getClass().getResource("/resources/Flecha.png")).getImage();
        } catch (Exception e) {
            System.out.println("No se encontró Flecha.png");
            return null;
        }
    }

    // ─── ESPERAR FIN DE ANIMACIÓN ──────────────────────────
    private void onAccionIniciada() {
        setBotonesHabilitados(false);
        esperarFinAnimacion(viewActual);
    }

    private void esperarFinAnimacion(final EntidadView entidadView) {
        Timer timerEspera = new Timer(16, null);
        timerEspera.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!entidadView.estaAnimandoAccion()) {
                    timerEspera.stop();
                    avanzarTurno();
                }
            }
        });
        timerEspera.start();
    }

    // ─── SELECCIÓN DE OBJETIVO ────────────────────────────────────────────────

    private void activarSeleccionAlumnos(ACCIONES accion) {
        this.accionPendiente = accion;
        this.esperandoObjetivo = true;

        List<Entidad> alumnos = Orquestador.getInstance().getAlumnos().getEntidades();
        ocultarFlechas();
        
        for (int i = 0; i < alumnos.size(); i++) {
            if (alumnos.get(i).estaVivo()) {
                flechasAlumnos.get(i).setVisible(true);
                indiceObjetivoActual = i;
                break;
            }
        }
    }
    
    private void activarSeleccionEnemigos(ACCIONES accion) {
        this.accionPendiente = accion;
        this.esperandoObjetivo = true;

        List<Entidad> enemigos = Orquestador.getInstance().getBatalla().getEnemigos().getEntidades();
        ocultarFlechas();
        
        for (int i = 0; i < enemigos.size(); i++) {
            if (enemigos.get(i).estaVivo()) {
                flechasEnemigos.get(i).setVisible(true);
                indiceObjetivoActual = i;
                break;
            }
        }
    }

    private void ocultarFlechas() {
        for (int i = 0; i < flechasEnemigos.size(); i++) {
            flechasEnemigos.get(i).setVisible(false);
        }
        for (int i = 0; i < flechasAlumnos.size(); i++) {
            flechasAlumnos.get(i).setVisible(false);
        }
    }

    // ─── USAR ITEM (POCIONES) ─────────────────────────────────────────────────

    private void abrirInventarioBatalla() {
        inventarioBatallaView.actualizar();

        int invAncho = 600;
        int invAlto = 420;
        int x = (getWidth() - invAncho) / 2;
        int y = (getHeight() - invAlto) / 2;
        inventarioBatallaView.setBounds(x, y, invAncho, invAlto);
        overlayInventario.setBounds(0, 0, getWidth(), getHeight());

        setComponentZOrder(overlayInventario, 0);
        setComponentZOrder(inventarioBatallaView, 0);

        overlayInventario.setVisible(true);
        inventarioBatallaView.setVisible(true);
    }

    private void cerrarInventarioBatalla() {
        if (overlayInventario != null) overlayInventario.setVisible(false);
        if (inventarioBatallaView != null) inventarioBatallaView.setVisible(false);
    }

    private void usarPocionSeleccionada(Pocion pocion) {
        int respuesta = JOptionPane.showConfirmDialog(
            ventana,
            "¿Querés usar " + pocion.getNombre() + "?",
            "Confirmar acción",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (respuesta == JOptionPane.YES_OPTION) {
            cerrarInventarioBatalla();
            Orquestador.getInstance().ejecutarTurno(ACCIONES.USAR_ITEM, null, pocion);
            onAccionIniciada();
        }
    }

    // ─── FLUJO DE TURNOS ──────────────────────────────────────────────────────
    private void avanzarTurno() {
        actualizarVistas();

        if (Orquestador.getInstance().batallaTerminada()) {
            finalizarBatalla();
            return;
        }

        Orquestador.getInstance().proximoTurno();
        actualizarViewActual();
        characterOrderPanel.repaint();
        actualizarVistas();

        if (Orquestador.getInstance().batallaTerminada()) {
            finalizarBatalla();
            return;
        }

        if (!Orquestador.getInstance().esTurnoDeAlumno()) {
            setBotonesHabilitados(false);
            Timer timerPreAtaque = new Timer(800, null);
            timerPreAtaque.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    timerPreAtaque.stop();
                    Orquestador.getInstance().ejecutarTurnoEnemigo();
                    esperarFinAnimacion(viewActual);
                }
            });
            timerPreAtaque.setRepeats(false);
            timerPreAtaque.start();
        } else {
            setBotonesHabilitados(true);
        }
    }

    private void finalizarBatalla() {
        setBotonesHabilitados(false);
        boolean ganaron = Orquestador.getInstance().alumnosGanaron();
        modelo.Recompensa recompensa = null;

        if (ganaron) {
            modelo.Batalla batallaActual = Orquestador.getInstance().getBatalla();
            recompensa = modelo.factories.RecompensaFactory.obtenerRecompensaPorBatalla(batallaActual);
            Repositorio.getInstance().getPartidaActual().recibirPesos(recompensa.getOro());
            
            for (modelo.Item item : recompensa.getItems()) {
                if (item != null) {
                    Repositorio.getInstance().getPartidaActual().agregarItem(item);
                }
            }
            
            for (modelo.Entidad alumno : Orquestador.getInstance().getAlumnos().getEntidades()) {
                if (alumno instanceof modelo.Alumno) {
                    ((modelo.Alumno) alumno).recibirXp(recompensa.getXp());
                }
            }
            recompensa.setReclamada();
        }

        Repositorio.getInstance().guardarPartidaActual();
        mostrarPanelResultado(ganaron, recompensa);
    }

    private void mostrarPanelResultado(boolean victoria, modelo.Recompensa recompensa) {
        resultadoBatallaView.configurar(victoria, recompensa);
        int resAncho = 520;
        int resAlto = 380;
        int x = (getWidth() - resAncho) / 2;
        int y = (getHeight() - resAlto) / 2;
        
        resultadoBatallaView.setBounds(x, y, resAncho, resAlto);
        overlayResultado.setBounds(0, 0, getWidth(), getHeight());
        
        setComponentZOrder(overlayResultado, 0);
        setComponentZOrder(resultadoBatallaView, 0);

        overlayResultado.setVisible(true);
        resultadoBatallaView.setVisible(true);
        repaint();
    }

    private void cerrarResultadoBatalla() {
        if (overlayResultado != null) overlayResultado.setVisible(false);
        if (resultadoBatallaView != null) resultadoBatallaView.setVisible(false);
        if (ventana != null) ventana.verNiveles();
    }

    // ─── ACTUALIZAR VISTAS ────────────────────────────────────────────────────
    private void actualizarViewActual() {
        Entidad entidadActual = Orquestador.getInstance().getEntidadActual();
        viewActual = null;
        List<EntidadView> lista = Orquestador.getInstance().esTurnoDeAlumno()
            ? viewsAlumnos : viewsEnemigos;
        for (EntidadView ev : lista) {
            if (ev.getEntidad() == entidadActual) {
                viewActual = ev;
                break;
            }
        }
    }

    private void actualizarVistas() {
        List<Entidad> alumnos  = Orquestador.getInstance().getAlumnos().getEntidades();
        List<Entidad> enemigos = Orquestador.getInstance().getBatalla().getEnemigos().getEntidades();

        for (int i = 0; i < viewsAlumnos.size() && i < alumnos.size(); i++) {
            viewsAlumnos.get(i).repaint();
        }
        for (int i = 0; i < viewsEnemigos.size() && i < enemigos.size(); i++) {
            viewsEnemigos.get(i).repaint();
        }
    }

    private void setBotonesHabilitados(boolean habilitado) {
        if (!habilitado) {
            if (btnAtacar != null) btnAtacar.setEnabled(false);
            if (btnDefender != null) btnDefender.setEnabled(false);
            if (btnHabilidad != null) btnHabilidad.setEnabled(false);
            if (btnItem != null) btnItem.setEnabled(false);
            cerrarInventarioBatalla();
            ocultarFlechas();
        } else {
            Entidad actual = Orquestador.getInstance().getEntidadActual();
            if (actual != null) {
                int energiaActual = actual.getEnergia();
                if (btnAtacar != null) btnAtacar.setEnabled(energiaActual >= 10);
                if (btnDefender != null) btnDefender.setEnabled(true);
                if (btnHabilidad != null) {
                    if (actual.getHabilidad() != null) {
                        btnHabilidad.setEnabled(energiaActual >= actual.getHabilidad().getCostoEnergia());
                    } else {
                        btnHabilidad.setEnabled(false);
                    }
                }
                if (btnItem != null) btnItem.setEnabled(true);
            }
        }
        if (characterOrderPanel != null) characterOrderPanel.repaint();
    }

    // ─── PINTAR ARENA ─────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (Orquestador.getInstance().getBatalla() == null) return;
        String nombreArena = Orquestador.getInstance().getBatalla().getNombreArena();
        try {
            Image imagenArena = new ImageIcon(
                getClass().getResource("/resources/arenas/" + nombreArena + ".png")
            ).getImage();
            g.drawImage(imagenArena, 0, 0, getWidth(), getHeight(), this);
        } catch (Exception e) {
            System.out.println("No se encontró la arena: " + nombreArena);
        }
    }
}