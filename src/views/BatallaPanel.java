package views;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import main.VentanaLayout;
import orquestador.Orquestador;

public class BatallaPanel extends JPanel {
    private VentanaLayout ventana;

    public BatallaPanel(VentanaLayout ventana) {
        this.ventana = ventana;
        setLayout(null);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        inicializar();
    }

    private void inicializar() {
        if (Orquestador.getInstance().getBatalla() == null) return;

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