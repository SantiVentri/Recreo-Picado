package views;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import java.util.List;
import modelo.Entidad;
import orquestador.Orquestador;
import javax.swing.JButton;
import javax.swing.JPanel;

import main.VentanaLayout;
import orquestador.Orquestador;

public class BatallaPanel extends JPanel {
    private VentanaLayout ventana; 

    public BatallaPanel(VentanaLayout ventana) {
        this.ventana = ventana;
        setLayout(null);
    }

    public void cargarPanel() {        
    	JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    	panelBotones.setOpaque(false);

    	JButton btnAtacar = new JButton("Atacar");
        btnAtacar.setPreferredSize(new Dimension(120, 42));
        panelBotones.add(btnAtacar);
        
        JButton btnDefender = new JButton("Defender");
        btnDefender.setPreferredSize(new Dimension(120, 42));
        panelBotones.add(btnDefender);
        
        JButton btnHabilidad = new JButton("Usar Habilidad");
        btnHabilidad.setPreferredSize(new Dimension(140, 42));
        panelBotones.add(btnHabilidad);
        
        JButton btnItem = new JButton("Usar Item");
        btnItem.setPreferredSize(new Dimension(120, 42));
        panelBotones.add(btnItem);
        
        JButton btnHuir = new JButton("Huir");
        btnHuir.setPreferredSize(new Dimension(120, 42));
        btnHuir.addActionListener(e -> {
            Orquestador.getInstance().reiniciar();
            ventana.verNiveles();
        });
        panelBotones.add(btnHuir);

    	// Calcular la posición del panel
    	Dimension tamañoReal = panelBotones.getPreferredSize();
    	int margenDerecho = 40;
    	int margenInferior = 30;

    	panelBotones.setBounds(
    	    1010 - tamañoReal.width - margenDerecho, 
    	    610 - tamañoReal.height - margenInferior - 40, 
    	    tamañoReal.width,
    	    tamañoReal.height
    	);

    	add(panelBotones);
    }

    public void cargarEntidades() {
        if (Orquestador.getInstance().getBatalla() == null || Orquestador.getInstance().getAlumnos() == null) return;

        List<Entidad> alumnos  = Orquestador.getInstance().getAlumnos().getEntidades();
        List<Entidad> enemigos = Orquestador.getInstance().getBatalla().getEnemigos().getEntidades();

        // Parámetros de escaleritas enfrentadas.
        int alumnoBaseX  = 260;
        int enemigoBaseX = 480;
        int baseY = 120;
        int pasoX = 100;
        int pasoY = 30;
        int evAncho = 220;
        int evAlto = 360;

        // ALUMNOS — índice 0 arriba-centro, los siguientes bajan y van a la izquierda
        for (int i = 0; i < alumnos.size(); i++) {
            EntidadView ev = alumnos.get(i).toView();
            ev.setMostrarHUD(true);
            ev.setMirandoIzquierda(false);
            ev.setBounds(alumnoBaseX - i * pasoX, baseY + i * pasoY, evAncho, evAlto);
            add(ev);
        }

        // ENEMIGOS — índice 0 arriba-centro, los siguientes bajan y van a la derecha
        for (int i = 0; i < enemigos.size(); i++) {
            EntidadView ev = enemigos.get(i).toView();
            ev.setMostrarHUD(true);
            ev.setMirandoIzquierda(true);
            ev.setBounds(enemigoBaseX + i * pasoX, baseY + i * pasoY, evAncho, evAlto);
            add(ev);
        }

        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (Orquestador.getInstance().getBatalla() == null) return;

        // Dibujar arena
        String nombreArena = Orquestador.getInstance().getBatalla().getNombreArena();
        try {
            Image imagenArena = new ImageIcon(getClass().getResource("/resources/arenas/" + nombreArena + ".png")).getImage();
            g.drawImage(imagenArena, 0, 0, getWidth(), getHeight(), this);
        } catch (Exception e) {
            System.out.println("No se encontró la arena: " + nombreArena);
        }
    }
}