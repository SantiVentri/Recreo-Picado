package views;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import enums.ESTADO_BATALLA;
import main.VentanaLayout;
import modelo.Batalla;

public class LevelsPanel extends JPanel {    
    private Image imagenFondo;
    private Image imagenFlecha;
    private Image imagenCheck;
    private Image imagenCandado;
    
    private JButton btn1, btn2, btn3, btn4;
    private JButton[] botones;
    
    private JButton nivelSeleccionado = null;
    
    private Batalla[] batallas;

    public LevelsPanel(VentanaLayout ventana, Batalla[] batallas) {
        this.batallas = batallas;
        
        try {
        	imagenFondo = new ImageIcon(getClass().getResource("/resources/Levels.png")).getImage();
        	imagenFlecha = new ImageIcon(getClass().getResource("/resources/Flecha.png")).getImage();
            imagenCheck = new ImageIcon(getClass().getResource("/resources/Check.png")).getImage();
            imagenCandado = new ImageIcon(getClass().getResource("/resources/Candado.png")).getImage();
        } catch (Exception e) {
            System.out.println("Error: No se encontró alguna de las imágenes.");
        }
        
        configurarPanel(ventana);
    }
    
    private void configurarPanel(VentanaLayout ventana) {
        setLayout(new GridLayout(1, 4, 20, 0));
        setBorder(BorderFactory.createEmptyBorder(230, 165, 180, 165));
        
        btn1 = new JButton("");
        btn2 = new JButton("");
        btn3 = new JButton("");
        btn4 = new JButton("");
        
        botones = new JButton[]{btn1, btn2, btn3, btn4};
        
        for (int i = 0; i < botones.length; i++) {
            JButton button = botones[i];
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            
            if (batallas[i].getEstado() == ESTADO_BATALLA.BLOQUEADA) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            } else {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            configurarEventoHover(button, i);
            add(button);
        }
        
        btn1.addActionListener(e -> {
        	if (!(batallas[0].getEstado() == ESTADO_BATALLA.BLOQUEADA)) {
        		
        	}
    	});
        btn2.addActionListener(e -> {
        	if (!(batallas[1].getEstado() == ESTADO_BATALLA.BLOQUEADA)) {
        	
        	}
        });
        btn3.addActionListener(e -> {
        	if (!(batallas[2].getEstado() == ESTADO_BATALLA.BLOQUEADA)) {
        		
        	}
        });
        btn4.addActionListener(e -> {
        	if (!(batallas[3].getEstado() == ESTADO_BATALLA.BLOQUEADA)) {
        		
        	}
    	});
    }
    
    private void configurarEventoHover(JButton boton, int indice) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!(batallas[indice].getEstado() == ESTADO_BATALLA.BLOQUEADA)) {
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
        
        // Dibujar fondo
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }

        // Dibujar candados y checks
        int anchoImagen = 80;
        int altoImagen = 80;

        for (int i = 0; i < botones.length; i++) {
            ESTADO_BATALLA estado = batallas[i].getEstado();
            
            Image imagenADibujar = null;
            if (estado == ESTADO_BATALLA.BLOQUEADA) {
                imagenADibujar = imagenCandado;
            } else if (estado == ESTADO_BATALLA.VICTORIA) {
                imagenADibujar = imagenCheck;
            }

            if (imagenADibujar != null) {
                JButton btn = botones[i];

                int x = btn.getX() + (btn.getWidth() / 2) - (anchoImagen / 2);
                int y = btn.getY() + (btn.getHeight() / 2) - (altoImagen / 2);
                
                g.drawImage(imagenADibujar, x, y, anchoImagen, altoImagen, this);
            }
        }

        // Dibujar flecha
        if (imagenFlecha != null && nivelSeleccionado != null) {
            int anchoFlecha = 60;
            int altoFlecha = 60;

            int xCentrada = nivelSeleccionado.getX() + (nivelSeleccionado.getWidth() / 2) - (anchoFlecha / 2);
            int yArriba = nivelSeleccionado.getY() - altoFlecha - 22;

            g.drawImage(imagenFlecha, xCentrada, yArriba, anchoFlecha, altoFlecha, this);
        }
    }
}