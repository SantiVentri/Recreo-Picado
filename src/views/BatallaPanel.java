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

    private VentanaLayout ventana;

    // Estado de selección de objetivo
    private ACCIONES accionPendiente = null;
    private boolean esperandoObjetivo = false;
    private int indiceObjetivoActual = 0;

    // Referencias a EntidadViews
    private List<EntidadView> viewsAlumnos  = new ArrayList<EntidadView>();
    private List<EntidadView> viewsEnemigos = new ArrayList<EntidadView>();

    // Flechas flotantes sobre cada enemigo (una por enemigo, se muestran/ocultan)
    private List<JLabel> flechasEnemigos = new ArrayList<JLabel>();

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

        // ALUMNOS
        for (int i = 0; i < alumnos.size(); i++) {
            EntidadView ev = alumnos.get(i).toView();
            ev.setMostrarHUD(true);
            ev.setMirandoIzquierda(false);
            ev.setBounds(alumnoBaseX - i * pasoX, baseY + i * pasoY, evAncho, evAlto);
            viewsAlumnos.add(ev);
            add(ev);
        }

        // ENEMIGOS + flechas
        Image imgFlecha = cargarFlecha();

        for (int i = 0; i < enemigos.size(); i++) {
            final int idx = i;

            // EntidadView del enemigo
            EntidadView ev = enemigos.get(i).toView();
            ev.setMostrarHUD(true);
            ev.setMirandoIzquierda(true);
            int evX = enemigoBaseX + i * pasoX;
            int evY = baseY + i * pasoY;
            ev.setBounds(evX, evY, evAncho, evAlto);

            ev.setClickListener(new EntidadView.ClickListener() {
                public void onClick(EntidadView origen, int clickX) {
                    if (!esperandoObjetivo) return;
                    Entidad objetivo = Orquestador.getInstance()
                        .getBatalla().getEnemigos().getEntidades().get(idx);
                    if (!objetivo.estaVivo()) return;

                    // Área clickeable: 80px centrados en el EntidadView
                    int centro = origen.getWidth() / 2;
                    if (clickX < centro - 40 || clickX > centro + 40) return;

                    ocultarFlechas();
                    indiceObjetivoActual = idx;
                    Orquestador.getInstance().ejecutarTurno(accionPendiente, objetivo, null);
                    esperandoObjetivo = false;
                    accionPendiente   = null;
                    avanzarTurno();
                }
            });

            ev.setHoverListener(new EntidadView.HoverListener() {
                public void onEnter() {
                    if (!esperandoObjetivo) return;
                    Entidad e = Orquestador.getInstance()
                        .getBatalla().getEnemigos().getEntidades().get(idx);
                    if (!e.estaVivo()) return;
                    // Mostrar solo la flecha de este enemigo
                    ocultarFlechas();
                    flechasEnemigos.get(idx).setVisible(true);
                    indiceObjetivoActual = idx;
                }
                public void onExit() {
                    if (!esperandoObjetivo) return;
                    // Volver a mostrar la flecha del objetivo por defecto
                    ocultarFlechas();
                    flechasEnemigos.get(indiceObjetivoActual).setVisible(true);
                }
            });

            viewsEnemigos.add(ev);
            add(ev);

            // Flecha encima del enemigo (oculta por defecto)
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
            add(lblFlecha);
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

    // ─── SELECCIÓN DE OBJETIVO ────────────────────────────────────────────────

    private void activarSeleccionObjetivo(ACCIONES accion) {
        this.accionPendiente   = accion;
        this.esperandoObjetivo = true;

        // Mostrar flecha sobre el primer enemigo vivo por defecto
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

    // ─── FLUJO DE TURNOS (uno a uno) ──────────────────────────────────────────

    private void avanzarTurno() {
        actualizarVistas();

        if (Orquestador.getInstance().batallaTerminada()) {
            finalizarBatalla();
            return;
        }

        // Pasar al siguiente turno (alumno→enemigo o enemigo→alumno)
        Orquestador.getInstance().proximoTurno();

        if (Orquestador.getInstance().batallaTerminada()) {
            finalizarBatalla();
            return;
        }

        if (!Orquestador.getInstance().esTurnoDeAlumno()) {
            // Turno del enemigo: deshabilitar botones, esperar 800ms y ejecutar
            setBotonesHabilitados(false);
            Timer timer = new Timer(800, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Orquestador.getInstance().ejecutarTurnoEnemigo();
                    // Volver al turno del alumno
                    avanzarTurno();
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            // Es turno del alumno: habilitar botones
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
        if (btnAtacar    != null) {
        	btnAtacar.setEnabled(habilitado);
        }
        if (btnDefender  != null) {
        	btnDefender.setEnabled(habilitado);
        }
        if (btnHabilidad != null) {
        	btnHabilidad.setEnabled(habilitado);
        }

        if (!habilitado) {
        	ocultarFlechas();
        }
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