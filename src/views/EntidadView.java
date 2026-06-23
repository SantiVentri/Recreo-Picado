package views;

import javax.swing.JPanel;
import javax.swing.Timer;

import enums.ANIMACIONES;
import modelo.Entidad;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

public class EntidadView extends JPanel {

    public interface ClickListener {
        void onClick(EntidadView origen, int clickX);
    }

    public interface HoverListener {
        void onEnter();
        void onExit();
    }

    private ClickListener clickListener;
    private HoverListener hoverListener;
    
    private Entidad entidad;
    private boolean mirandoIzquierda = false;
    private boolean mostrarHUD;
    private float escala;
    
    // Nombre de display en caso de no usar entidad
    private String nombreDisplay = "";

    // --- VARIABLES DE ANIMACIÓN ---
    private Map<ANIMACIONES, BufferedImage[]> animaciones;
    private ANIMACIONES estadoActual = ANIMACIONES.IDLE;
    private int frameActual = 0;
    private Timer animTimer;

    // --- ANIMACIÓN DE ATAQUE ---
    private Timer timerAtaque;
    private long faseInicioMs;
    private static final int DURACION_ATAQUE_MS = 750;

    // Cuánto se expande el ancho durante el ataque (px extra)
    private static final int EXTRA_ANCHO_ATAQUE = 180;

    // Constructor completo
    public EntidadView(String nombreDisplay, Entidad entidad, boolean mostrarHUD, float escala) {
    	this.nombreDisplay = nombreDisplay;
        this.entidad = entidad;
        this.mostrarHUD = mostrarHUD;
        this.escala = escala;

        this.setPreferredSize(new Dimension(320, 420));
        this.setOpaque(false);

        this.animaciones = new HashMap();
        cargarAnimaciones();

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (clickListener != null) {
                    clickListener.onClick(EntidadView.this, e.getX());
                }
            }
            public void mouseEntered(MouseEvent e) {
                if (hoverListener != null) hoverListener.onEnter();
            }
            public void mouseExited(MouseEvent e) {
                if (hoverListener != null) hoverListener.onExit();
            }
        });

        animTimer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarFrame();
            }
        });
        animTimer.start();
    }

    // Contructor básico + control de HUD
    public EntidadView(Entidad entidad, boolean mostrarHUD) {
        this("", entidad, mostrarHUD, 1.0f);
    }

    // Contructor básico
    public EntidadView(Entidad entidad) {
        this("", entidad, true, 1.0f);
    }
    
    // Contructor sin entidad (Para mostrar en MyTeam.java)
    public EntidadView(String nombre, float escala) {
        this(nombre, null, false, escala);
    }

    private void cargarAnimaciones() {
        String nombreFormateado = entidad != null ? entidad.getNombre().toLowerCase().replace(" ", "_") : nombreDisplay;
        String pathIdle   = "src/resources/sprites/" + nombreFormateado + "/" + nombreFormateado + "-idle.png";
        String pathAtaque = "src/resources/sprites/" + nombreFormateado + "/" + nombreFormateado + "-attack.png";
        String pathMuerto = "src/resources/sprites/dead.png";

        animaciones.put(ANIMACIONES.IDLE, recortarSprite(pathIdle, 22, 5));
        animaciones.put(ANIMACIONES.ATACAR, recortarSprite(pathAtaque, 1, 1));
        animaciones.put(ANIMACIONES.MUERTO, recortarSprite(pathMuerto, 1, 1));
    }

    /**
     * Inicia la animación de ataque.
     * Expande los bounds del panel hacia el lado del enemigo durante el ataque
     * y los restaura al terminar.
     * BatallaPanel puede consultar estaAnimandoAtaque() para saber si terminó.
     */
    public void reproducirAnimacionAtaque() {
    	if (this.entidad.getVida() <= 0 || (timerAtaque != null && timerAtaque.isRunning())) return;

        // Guardar bounds originales
        final int xOrig = getX();
        final int yOrig = getY();
        final int wOrig = getWidth();
        final int hOrig = getHeight();

        // Expandir hacia el lado del enemigo:
        // - mira a la derecha (alumno): expandir hacia la derecha → x no cambia, w crece
        // - mira a la izquierda (enemigo): expandir hacia la izquierda → x se corre, w crece
        if (mirandoIzquierda) {
            setBounds(xOrig - EXTRA_ANCHO_ATAQUE, yOrig, wOrig + EXTRA_ANCHO_ATAQUE, hOrig);
        } else {
            setBounds(xOrig, yOrig, wOrig + EXTRA_ANCHO_ATAQUE, hOrig);
        }

        setEstadoActual(ANIMACIONES.ATACAR);
        faseInicioMs = System.currentTimeMillis();

        timerAtaque = new Timer(16, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                long transcurrido = System.currentTimeMillis() - faseInicioMs;
                if (transcurrido >= DURACION_ATAQUE_MS) {
                    setEstadoActual(ANIMACIONES.IDLE);
                    // Restaurar bounds originales
                    setBounds(xOrig, yOrig, wOrig, hOrig);
                    timerAtaque.stop();
                }
                repaint();
            }
        });
        timerAtaque.start();
    }

    /**
     * Devuelve true si la animación de ataque todavía está en curso.
     */
    public boolean estaAnimandoAtaque() {
        return timerAtaque != null && timerAtaque.isRunning();
    }

    private BufferedImage[] recortarSprite(String ruta, int cantidadFrames, int columnas) {
        try {
            BufferedImage spriteSheet = ImageIO.read(new File(ruta));

            int anchoFrame = 256;
            int altoFrame = 256;

            BufferedImage[] frames = new BufferedImage[cantidadFrames];

            for (int i = 0; i < cantidadFrames; i++) {
                int fila = i / columnas;
                int columna = i % columnas;
                frames[i] = spriteSheet.getSubimage(columna * anchoFrame, fila * altoFrame, anchoFrame, altoFrame);
            }
            return frames;

        } catch (IOException e) {
            System.err.println("No se pudo cargar el sprite en la ruta: " + ruta);
            return null;
        } catch (java.awt.image.RasterFormatException e) {
            System.err.println("Error de dimensiones al recortar " + ruta);
            return null;
        }
    }

    private void actualizarFrame() {
    	if (this.entidad != null && !entidad.estaVivo()) {
        	this.setEstadoActual(ANIMACIONES.MUERTO);
        }
    	
        BufferedImage[] framesActuales = animaciones.get(estadoActual);
        
        if (framesActuales != null && framesActuales.length > 0) {
            frameActual = (frameActual + 1) % framesActuales.length;
            repaint();
        }
    }

    public void setEstadoActual(ANIMACIONES nuevoEstado) {
        if (this.estadoActual != nuevoEstado) {
            this.estadoActual = nuevoEstado;
            this.frameActual = 0;
        }
    }

    public void setMirandoIzquierda(boolean mirandoIzquierda) {
        this.mirandoIzquierda = mirandoIzquierda;
    }

    public void setEscala(float escala) {
        this.escala = escala;
        repaint();
    }

    public void setClickListener(ClickListener listener) {
        this.clickListener = listener;
    }

    public void setHoverListener(HoverListener listener) {
        this.hoverListener = listener;
    }

    public void setMostrarHUD(boolean mostrarHUD) {
        this.mostrarHUD = mostrarHUD;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centroX = getWidth() / 2;
        int inicioY = 20;
        int anchoBarras = 120;
        int altoBarras = 10;
        int xBarras = centroX - (anchoBarras / 2);

        // --- HUD: nombre + barras ---
        if (mostrarHUD) {
            // NOMBRE
            g2D.setFont(new Font("Arial", Font.BOLD, 14));
            g2D.setColor(Color.WHITE);
            FontMetrics fm = g2D.getFontMetrics();
            g2D.drawString(entidad.getNombre(), centroX - (fm.stringWidth(entidad.getNombre()) / 2), inicioY);

            // BARRA DE VIDA
            int yVida = inicioY + 10;
            double porcentajeVida = (double) entidad.getVida() / entidad.getVidaMax();
            g2D.setColor(new Color(50, 50, 50));
            g2D.fillRect(xBarras, yVida, anchoBarras, altoBarras);
            if (porcentajeVida > 0.5) g2D.setColor(Color.GREEN);
            else if (porcentajeVida > 0.2) g2D.setColor(Color.YELLOW);
            else g2D.setColor(Color.RED);
            g2D.fillRect(xBarras, yVida, (int) (anchoBarras * porcentajeVida), altoBarras);
            g2D.setColor(Color.BLACK);
            g2D.drawRect(xBarras, yVida, anchoBarras, altoBarras);

            // BARRA DE ENERGÍA
            int yEnergia = yVida + altoBarras + 5;
            double porcentajeEnergia = (double) entidad.getEnergia() / entidad.getEnergiaMax();
            g2D.setColor(new Color(50, 50, 50));
            g2D.fillRect(xBarras, yEnergia, anchoBarras, altoBarras);
            g2D.setColor(new Color(0, 190, 255));
            g2D.fillRect(xBarras, yEnergia, (int) (anchoBarras * porcentajeEnergia), altoBarras);
            g2D.setColor(Color.BLACK);
            g2D.drawRect(xBarras, yEnergia, anchoBarras, altoBarras);
        }

        // --- SPRITE Y SOMBRA ---
        int ySprite;
        if (mostrarHUD) {
            int yVida    = inicioY + 10;
            int yEnergia = yVida + altoBarras + 5;
            ySprite = yEnergia + altoBarras + 20;
        } else {
            ySprite = inicioY;
        }

        int anchoBase = 300;
        int altoBase = anchoBase;
        int offsetSombra = 52;

        int anchoEscalado = (int) (anchoBase * escala);
        int altoEscalado = (int) (altoBase  * escala);

        // Durante el ataque el sprite se ancla al borde interno
        // (lado opuesto al enemigo) para que el contenido extra salga hacia el enemigo.
        int xSprite;
        if (estadoActual == ANIMACIONES.ATACAR) {
            if (mirandoIzquierda) {
                // Enemigo: mira izquierda, el panel se extendió hacia la izquierda.
                // Anclar a la derecha del panel (borde interno).
                xSprite = getWidth() - anchoEscalado;
            } else {
                // Alumno: mira derecha, el panel se extendió hacia la derecha.
                // Anclar a la izquierda del panel (borde interno).
                xSprite = 0;
            }
        } else {
            xSprite = centroX - (anchoEscalado / 2);
        }

        BufferedImage[] frames = animaciones.get(estadoActual);

        // SOMBRA
        int anchoSombraBase = 95;
        int anchoSombra = (int) (anchoSombraBase * escala);
        int altoSombra  = (int) (30 * escala);
        int xSombra = xSprite + (anchoEscalado / 2) - (anchoSombra / 2);
        int ySombra = ySprite + altoEscalado - (int) (offsetSombra * escala);

        Graphics2D g2DSombra = (Graphics2D) g2D.create();
        g2DSombra.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2DSombra.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        g2DSombra.setColor(Color.BLACK);
        g2DSombra.fillOval(xSombra, ySombra, anchoSombra, altoSombra);
        g2DSombra.dispose();

        // SPRITE
        if (frames != null && frames[frameActual] != null) {
            BufferedImage frame = frames[frameActual];
            if (mirandoIzquierda) {
                g2D.drawImage(frame, xSprite + anchoEscalado, ySprite, -anchoEscalado, altoEscalado, null);
            } else {
                g2D.drawImage(frame, xSprite, ySprite, anchoEscalado, altoEscalado, null);
            }
        } else {
            g2D.setColor(Color.darkGray);
            g2D.fillRect(centroX - 40, ySprite, 80, 120);
        }
    }
}