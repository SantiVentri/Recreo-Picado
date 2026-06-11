package views;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import main.VentanaLayout;

public class Jugador1 extends JPanel {

    private static final long serialVersionUID = 1L;
    private Image imagenFondo;
    private EntidadView entidadView;

    public Jugador1(VentanaLayout ventana) {

        try {
            imagenFondo = new ImageIcon("src/resources/Inventario-batalla.jpg.jpeg").getImage();
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen de fondo");
        }

        setLayout(null);

        // --- ENTIDAD VIEW ---
        entidadView = new EntidadView("Mago", 100, 100, 100, 100, false, 2f);
        entidadView.setBounds(60, 80, 400, 500);
        add(entidadView);

        // --- BOTÓN VOLVER ---
        ImageIcon iconoOriginal = new ImageIcon("src/resources/Volver-atras.png");
        Image imageEscalada = iconoOriginal.getImage().getScaledInstance(90, 70, Image.SCALE_SMOOTH);
        ImageIcon iconoExit = new ImageIcon(imageEscalada);

        JButton botonExit = new JButton(iconoExit);
        botonExit.setBounds(700, 15, 90, 70);
        botonExit.setBorderPainted(false);
        botonExit.setContentAreaFilled(false);
        botonExit.setFocusPainted(false);
        botonExit.addActionListener(e -> ventana.verEquipo());

        add(botonExit);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}