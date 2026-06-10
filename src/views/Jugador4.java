package views;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import main.VentanaLayout;

public class Jugador4 extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private Image imagenFondo;
	


    public Jugador4(VentanaLayout ventana) {
    	
    	try {
			imagenFondo= new ImageIcon("src/resources/Inventario-batalla.jpg.jpeg"
					+ ""
					+ ""
					+ "").getImage();
			
			
		} catch (Exception e) {
			System.out.println("Error al cargar la imagen");
		}
		
    	setLayout(null);
    	
    	//imagen de boton
    	ImageIcon iconoOriginal = new ImageIcon("src/resources/Volver-atras.png");
    	
    	Image imageEscalada = iconoOriginal.getImage().getScaledInstance(90, 70, Image.SCALE_SMOOTH);
    	
    	ImageIcon iconoExit = new ImageIcon(imageEscalada);
    	
    	JButton botonExit = new JButton(iconoExit);
    	
    	//posición y tamaño del boton
    	
    	botonExit.setBounds(700, 15, 90, 70);
    	
    	//Sacar borde y fondo
    	botonExit.setBorderPainted(false);
    	botonExit.setContentAreaFilled(false);
    	botonExit.setFocusPainted(false);
    	
    	//Accion al hacer click
    	botonExit.addActionListener(e -> ventana.volverJugadores());
    	
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

