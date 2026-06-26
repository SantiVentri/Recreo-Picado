package views;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

import enums.ESTADO_BATALLA;
import modelo.Batalla;

public class LevelsPanel extends JPanel {
    private Image imagenFondo;
    private Image imagenFlecha;
    private Image imagenCheck;
    private Image imagenCandado;

    private JButton btn1, btn2, btn3, btn4;
    private JButton[] botones;

    private JButton nivelSeleccionado = null;
    private List<Batalla> batallas;

    public LevelsPanel(VentanaLayout ventana, List<Batalla> batallas) {
        this.batallas = batallas;

        try {
            imagenFondo   = new ImageIcon(getClass().getResource("/resources/Levels.png")).getImage();
            imagenFlecha  = new ImageIcon(getClass().getResource("/resources/Flecha.png")).getImage();
            imagenCheck   = new ImageIcon(getClass().getResource("/resources/Check.png")).getImage();
            imagenCandado = new ImageIcon(getClass().getResource("/resources/Candado.png")).getImage();
        } catch (Exception e) {
            System.out.println("Error: No se encontró alguna de las imágenes.");
        }

        configurarPanel(ventana);
    }

    private void configurarPanel(VentanaLayout ventana) {
        setLayout(null);

        btn1 = new JButton("");
        btn2 = new JButton("");
        btn3 = new JButton("");
        btn4 = new JButton("");

        btn1.setBounds(158, 165, 165, 275);
        btn2.setBounds(333, 165, 165, 275);
        btn3.setBounds(498, 165, 165, 275);
        btn4.setBounds(663, 165, 165, 275);

        botones = new JButton[]{btn1, btn2, btn3, btn4};

        for (int i = 0; i < botones.length; i++) {
            JButton button = botones[i];
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);

            if (batallas.get(i).getEstado() == ESTADO_BATALLA.BLOQUEADA) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            } else {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            configurarEventoHover(button, i);
            add(button);
        }

        // Al clickear un nivel desbloqueado, configurar el Orquestador y navegar
        btn1.addActionListener(e -> irABatalla(ventana, 0));
        btn2.addActionListener(e -> irABatalla(ventana, 1));
        btn3.addActionListener(e -> irABatalla(ventana, 2));
        btn4.addActionListener(e -> irABatalla(ventana, 3));

        // Botón volver
        ImageIcon iconoOriginal = new ImageIcon("src/resources/Volver-atras.png");
        Image imageEscalada = iconoOriginal.getImage().getScaledInstance(90, 70, Image.SCALE_SMOOTH);
        JButton botonExit = new JButton(new ImageIcon(imageEscalada));
        botonExit.setBounds(700, 15, 90, 70);
        botonExit.setBorderPainted(false);
        botonExit.setContentAreaFilled(false);
        botonExit.setFocusPainted(false);
        botonExit.addActionListener(e -> ventana.verMenu());
        add(botonExit);
    }

    private void irABatalla(VentanaLayout ventana, int indice) {
        if (batallas.get(indice).getEstado() != ESTADO_BATALLA.BLOQUEADA) {
            ventana.empezarBatalla(batallas.get(indice));
        }
    }

    private void configurarEventoHover(JButton boton, int indice) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (batallas.get(indice).getEstado() != ESTADO_BATALLA.BLOQUEADA) {
                    nivelSeleccionado = boton;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nivelSeleccionado = null;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }

        int anchoImagen = 80;
        int altoImagen  = 80;

        for (int i = 0; i < botones.length; i++) {
            ESTADO_BATALLA estado = batallas.get(i).getEstado();

            Image imagenADibujar = null;
            if (estado == ESTADO_BATALLA.BLOQUEADA) {
                imagenADibujar = imagenCandado;
            } else if (estado == ESTADO_BATALLA.VICTORIA) {
                imagenADibujar = imagenCheck;
            }

            if (imagenADibujar != null) {
                JButton btn = botones[i];
                int x = btn.getX() + (btn.getWidth()  / 2) - (anchoImagen / 2);
                int y = btn.getY() + (btn.getHeight() / 2) - (altoImagen  / 2);
                g.drawImage(imagenADibujar, x, y, anchoImagen, altoImagen, this);
            }
        }

        if (imagenFlecha != null && nivelSeleccionado != null) {
            int anchoFlecha = 60;
            int altoFlecha  = 60;
            int xCentrada = nivelSeleccionado.getX() + (nivelSeleccionado.getWidth() / 2) - (anchoFlecha / 2);
            int yArriba   = nivelSeleccionado.getY() - altoFlecha + 20;
            g.drawImage(imagenFlecha, xCentrada, yArriba, anchoFlecha, altoFlecha, this);
        }
    }
}