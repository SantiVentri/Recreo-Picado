package views;

import javax.swing.JPanel;
import main.VentanaLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Cursor;
import java.awt.Graphics;

public class MyTeam extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private Image imagenFondo;
	private Image imagenFlecha;
	private JButton btnJugador1, btnJugador2, btnJugador3, btnJugador4;
	private JButton botonSeleccionado = null;
	
	public MyTeam (VentanaLayout ventana) {
		
		try {
			imagenFondo= new ImageIcon("src/resources/Character-selection.jpg.jpeg").getImage();
			imagenFlecha = new ImageIcon("src/resources/Flecha.png").getImage();
			
		} catch (Exception e) {
			System.out.println("Error al cargar la imagen");
		}
		
		configurarPantalla(ventana);
		
	}
	
	private void configurarPantalla( VentanaLayout ventana) {
		
		setLayout(null);
		
		 btnJugador1 = new JButton();
	     btnJugador2 = new JButton();
	     btnJugador3 = new JButton();
	     btnJugador4 = new JButton();
	     
	     
	     btnJugador1.setBounds(80, 180, 150, 300);
	     btnJugador2.setBounds(290, 180, 150, 300);
	     btnJugador3.setBounds(500, 180, 150, 300);
	     btnJugador4.setBounds(710, 180, 150, 300);
	     
	     JButton[] buttons = {btnJugador1, btnJugador2, btnJugador3, btnJugador4};
	     
	     for (JButton button : buttons) {
	    	 configurarBoton(button);
	    	 configurarEventoHover(button);
	    	 add(button);	    	 
	     }
	     
	     btnJugador1.addActionListener(e -> ventana.verJugador1());
	     // btnJugador2.addActionListener(e -> ventana.verJugador2());
	     // btnJugador3.addActionListener(e -> ventana.verJugador3());
	     // btnJugador4.addActionListener(e -> ventana.verJugador4());
		
	}
	
	private void configurarBoton(JButton boton) {
		
		boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	private void configurarEventoHover(JButton boton) {
		 boton.addMouseListener(new MouseAdapter() {
	          @Override
	          public void mouseEntered(MouseEvent e) {
	              botonSeleccionado = boton;
	              repaint(); 
	            }

	          @Override
	          public void mouseExited(MouseEvent e) {
	              botonSeleccionado = null;
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

	        if (imagenFlecha != null && botonSeleccionado != null) {
	            int anchoFlecha = 60;
	            int altoFlecha = 60;

	            int xCentrada = botonSeleccionado.getX() + (botonSeleccionado.getWidth() / 2 + 15) - (anchoFlecha / 2);
	            
	            int yArriba = botonSeleccionado.getY() - altoFlecha - 10;

	            g.drawImage(imagenFlecha, xCentrada, yArriba, anchoFlecha, altoFlecha, this);
	        }
	    
	   }
}
