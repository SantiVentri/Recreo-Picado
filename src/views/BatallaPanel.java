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
import main.VentanaLayout;
import modelo.Entidad;
import modelo.Item;
import orquestador.Orquestador;

public class BatallaPanel extends JPanel {
	
	private CharacterOrderPanel characterOrderPanel;
    private VentanaLayout ventana;

    // Estado de selección de objetivo
    private ACCIONES accionPendiente = null;
    private boolean esperandoObjetivo = false;
    private int indiceObjetivoActual = 0;

    // Referencias a EntidadViews
    private List<EntidadView> viewsAlumnos  = new ArrayList();
    private List<EntidadView> viewsEnemigos = new ArrayList();

    // Flechas flotantes sobre cada enemigo (una por enemigo, se muestran/ocultan)
    private List<JLabel> flechasEnemigos = new ArrayList();

    // Botones
    private JButton btnAtacar;
    private JButton btnDefender;
    private JButton btnHabilidad;
    private JButton btnHuir;

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
                activarSeleccionObjetivo(ACCIONES.ATACAR);
            }
        });
        panelBotones.add(btnAtacar);

        btnDefender = new JButton("Defender");
        btnDefender.setPreferredSize(new Dimension(120, 42));
        btnDefender.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Orquestador.getInstance().ejecutarTurno(ACCIONES.DEFENDER, null, null);
                avanzarTurno();
            }
        });
        panelBotones.add(btnDefender);

        btnHabilidad = new JButton("Usar Habilidad");
        btnHabilidad.setPreferredSize(new Dimension(140, 42));
        btnHabilidad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                activarSeleccionObjetivo(ACCIONES.USAR_HABILIDAD);
            }
        });
        panelBotones.add(btnHabilidad);

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

        characterOrderPanel.setBounds(
                -15,
                -35,
                270,
                190);

        add(characterOrderPanel);
    }

    public void cargarEntidades() {
        if (Orquestador.getInstance().getBatalla() == null ||
            Orquestador.getInstance().getAlumnos() == null) return;

        viewsAlumnos.clear();
        viewsEnemigos.clear();
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

        // ALUMNOS — se agregan de atrás hacia adelante (índice mayor = más adelante = encima)
        for (int i = alumnos.size() - 1; i >= 0; i--) {
            EntidadView ev = alumnos.get(i).toView();
            ev.setMostrarHUD(true);
            ev.setMirandoIzquierda(false);
            ev.setBounds(alumnoBaseX - i * pasoX, baseY + i * pasoY, evAncho, evAlto);
            viewsAlumnos.add(ev);
            add(ev);
        }
        // Revertir la lista para que el índice coincida con el orden original de alumnos
        java.util.Collections.reverse(viewsAlumnos);

        // ENEMIGOS + flechas
        Image imgFlecha = cargarFlecha();

        // Primero crear todos los views de enemigos (en orden) para tener los listeners correctos,
        // luego agregarlos al panel de atrás hacia adelante para el z-order correcto.
        int[] evXArr = new int[enemigos.size()];
        int[] evYArr = new int[enemigos.size()];
        JLabel[] flechaArr = new JLabel[enemigos.size()];

        for (int i = 0; i < enemigos.size(); i++) {
            final int idx = i;

            EntidadView ev = enemigos.get(i).toView();
            ev.setMostrarHUD(true);
            ev.setMirandoIzquierda(true);
            int evX = enemigoBaseX + i * pasoX;
            int evY = baseY + i * pasoY;
            evXArr[i] = evX;
            evYArr[i] = evY;
            ev.setBounds(evX, evY, evAncho, evAlto);

            ev.setClickListener(new EntidadView.ClickListener() {
                public void onClick(EntidadView origen, int clickX) {
                    if (!esperandoObjetivo) return;
                    Entidad objetivo = Orquestador.getInstance()
                        .getBatalla().getEnemigos().getEntidades().get(idx);
                    if (!objetivo.estaVivo()) return;

                    int centro = origen.getWidth() / 2;
                    if (clickX < centro - 40 || clickX > centro + 40) return;

                    ocultarFlechas();
                    indiceObjetivoActual = idx;

                    int idxAtacante = Orquestador.getInstance().getIndiceAlumnoActual();
                    final EntidadView viewAtacante = (idxAtacante >= 0 && idxAtacante < viewsAlumnos.size())
                        ? viewsAlumnos.get(idxAtacante) : null;

                    Orquestador.getInstance().ejecutarTurno(accionPendiente, objetivo, null);
                    esperandoObjetivo = false;
                    accionPendiente   = null;
                    setBotonesHabilitados(false);

                    if (viewAtacante != null) {
                        viewAtacante.reproducirAnimacionAtaque();
                        esperarFinAnimacion(viewAtacante);
                    } else {
                        avanzarTurno();
                    }
                }
            });

            ev.setHoverListener(new EntidadView.HoverListener() {
                public void onEnter() {
                    if (!esperandoObjetivo) return;
                    Entidad e = Orquestador.getInstance()
                        .getBatalla().getEnemigos().getEntidades().get(idx);
                    if (!e.estaVivo()) return;
                    ocultarFlechas();
                    flechasEnemigos.get(idx).setVisible(true);
                    indiceObjetivoActual = idx;
                }
                public void onExit() {
                    if (!esperandoObjetivo) return;
                    ocultarFlechas();
                    flechasEnemigos.get(indiceObjetivoActual).setVisible(true);
                }
            });

            viewsEnemigos.add(ev);

            // Preparar flecha
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
            flechaArr[i] = lblFlecha;
        }

        // Agregar al panel de atrás hacia adelante para z-order correcto
        for (int i = enemigos.size() - 1; i >= 0; i--) {
            add(viewsEnemigos.get(i));
            add(flechaArr[i]);
        }

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

    // ─── ESPERAR FIN DE ANIMACIÓN (patrón del profe) ──────────────────────────
    //
    // Timer de 16ms que chequea si la animación terminó.
    // Cuando estaAnimandoAtaque() devuelve false, detiene el timer y avanza el turno.

    private void esperarFinAnimacion(final EntidadView atacante) {
        Timer timerEspera = new Timer(16, null);
        timerEspera.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!atacante.estaAnimandoAtaque()) {
                    timerEspera.stop();
                    avanzarTurno();
                }
            }
        });
        timerEspera.start();
    }

    // ─── SELECCIÓN DE OBJETIVO ────────────────────────────────────────────────

    private void activarSeleccionObjetivo(ACCIONES accion) {
        this.accionPendiente   = accion;
        this.esperandoObjetivo = true;

        List<Entidad> enemigos = Orquestador.getInstance()
            .getBatalla().getEnemigos().getEntidades();
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
    }

    // ─── FLUJO DE TURNOS ──────────────────────────────────────────────────────

    private void avanzarTurno() {
        actualizarVistas();

        if (Orquestador.getInstance().batallaTerminada()) {
            finalizarBatalla();
            return;
        }

        Orquestador.getInstance().proximoTurno();
        characterOrderPanel.repaint();

        if (Orquestador.getInstance().batallaTerminada()) {
            finalizarBatalla();
            return;
        }

        if (!Orquestador.getInstance().esTurnoDeAlumno()) {
            // Turno del enemigo: esperar 800ms, reproducir animación, luego avanzar
            setBotonesHabilitados(false);
            Timer timerPreAtaque = new Timer(800, null);
            timerPreAtaque.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    timerPreAtaque.stop();

                    int idxEnemigo = Orquestador.getInstance().getIndiceEnemigoActual();
                    final EntidadView viewEnemigo = (idxEnemigo >= 0 && idxEnemigo < viewsEnemigos.size())
                        ? viewsEnemigos.get(idxEnemigo) : null;

                    Orquestador.getInstance().ejecutarTurnoEnemigo();

                    if (viewEnemigo != null) {
                        viewEnemigo.reproducirAnimacionAtaque();
                        esperarFinAnimacion(viewEnemigo);
                    } else {
                        avanzarTurno();
                    }
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
        if (Orquestador.getInstance().alumnosGanaron()) {
            Orquestador.getInstance().terminarBatalla();
        } else {
            Orquestador.getInstance().reiniciar();
        }
        ventana.verNiveles();
    }

    // ─── ACTUALIZAR VISTAS ────────────────────────────────────────────────────

    private void actualizarVistas() {
        List<Entidad> alumnos  = Orquestador.getInstance().getAlumnos().getEntidades();
        List<Entidad> enemigos = Orquestador.getInstance().getBatalla().getEnemigos().getEntidades();

        for (int i = 0; i < viewsAlumnos.size() && i < alumnos.size(); i++) {
            viewsAlumnos.get(i).setVida(alumnos.get(i).getVida());
            viewsAlumnos.get(i).setEnergia(alumnos.get(i).getEnergia());
        }
        for (int i = 0; i < viewsEnemigos.size() && i < enemigos.size(); i++) {
            viewsEnemigos.get(i).setVida(enemigos.get(i).getVida());
            viewsEnemigos.get(i).setEnergia(enemigos.get(i).getEnergia());
        }
    }

    private void setBotonesHabilitados(boolean habilitado) {
        if (btnAtacar    != null) btnAtacar.setEnabled(habilitado);
        if (btnDefender  != null) btnDefender.setEnabled(habilitado);
        if (btnHabilidad != null) btnHabilidad.setEnabled(habilitado);
        if (!habilitado) ocultarFlechas();
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