package views;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import main.VentanaLayout;

public class LevelsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
    private Image imagenFondo;
    private Image imagenFlecha;
    private JButton btn1, btn2, btn3, btn4;
    private boolean btn1Bloqueado, btn2Bloqueado, btn3Bloqueado, btn4Bloqueado;
    private JButton nivelSeleccionado = null;

    public LevelsPanel(VentanaLayout ventana) {
    	btn1Bloqueado = false;
    	btn2Bloqueado = true;
    	btn3Bloqueado = true;
    	btn4Bloqueado = true;
    	
        try {
            imagenFondo = new ImageIcon("src/resources/Levels.png").getImage();
            imagenFlecha = new ImageIcon("src/resources/Flecha.png").getImage();
        } catch (Exception e) {
            System.out.println("Error: No se encontró la imagen de fondo.");
        }
        
        configurarPanel(ventana);
    }
    
    private void configurarPanel(VentanaLayout ventana) {
    	setLayout(null);
        
        setLayout(new GridLayout(1, 4, 20, 0));
        setBorder(BorderFactory.createEmptyBorder(230, 165, 180, 165));
        
        btn1 = new JButton("");
        btn2 = new JButton("");
        btn3 = new JButton("");
        btn4 = new JButton("");
        
        JButton[] buttons = {btn1, btn2, btn3, btn4};
        
        for (JButton button : buttons) {
        	button.setOpaque(false);
        	button.setContentAreaFilled(false);
        	button.setBorderPainted(false);
        	button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            configurarEventoHover(button);
            add(button);
        }
        
        btn1.addActionListener(e -> {
        	if (!btn1Bloqueado) {
        		
        	}
        });
        btn2.addActionListener(e -> {
        	if (!btn2Bloqueado) {
        		
        	}
        });
        btn3.addActionListener(e -> {
        	if (!btn3Bloqueado) {
        		
        	}
        });
    	btn4.addActionListener(e -> {
    		if (!btn4Bloqueado) {
        		
        	}
    	});
        
    }
    
    private void configurarEventoHover(JButton boton) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                nivelSeleccionado = boton;
                repaint(); 
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

        if (imagenFlecha != null && nivelSeleccionado != null) {
            int anchoFlecha = 60;
            int altoFlecha = 60;

            int xCentrada = nivelSeleccionado.getX() + (nivelSeleccionado.getWidth() / 2) - (anchoFlecha / 2);
            int yArriba = nivelSeleccionado.getY() - altoFlecha - 22;

            g.drawImage(imagenFlecha, xCentrada, yArriba, anchoFlecha, altoFlecha, this);
        }
    }
}
