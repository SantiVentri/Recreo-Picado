package views;


import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import modelo.Entidad;
import modelo.Repositorio;

public class Jugador1 extends JPanel {

    private static final long serialVersionUID = 1L;
    private Image imagenFondo;
    private EntidadView entidadView;
    private InventarioView inventario;
    private StatsPanel stats;

    public Jugador1(VentanaLayout ventana) {

        try {
            imagenFondo = new ImageIcon("src/resources/Inventario-batalla.jpg.jpeg").getImage();
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen de fondo");
        }

        setLayout(null);

        // Obtener el mago de la partida
        Entidad mago = Repositorio.getInstance()
                .getPartidaActual()
                .getAlumnos()
                .getEntidadPorNombre("Mago");

        // --- ENTIDAD VIEW ---
        entidadView = new EntidadView(mago, false);
        entidadView.setEscala(2f);
        entidadView.setBounds(60, 80, 400, 500);
        add(entidadView);

        // --- INVENTARIO ---
        inventario = new InventarioView(mago);
        inventario.setBounds(505, 115, 284, 357);
        add(inventario);
        
        // --- PANEL DE STATS ---
        stats = new StatsPanel(mago);
        stats.setBounds(15, 355, 220, 200);
        add(stats);

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
    
    public void refrescarInventario() {
        if (inventario != null) {
            inventario.actualizar();
        }
        
        if (stats != null) {
            stats.actualizar();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}