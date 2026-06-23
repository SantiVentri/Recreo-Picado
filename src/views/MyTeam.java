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
	private EntidadView entidadSeleccionada = null;
	
	public MyTeam (VentanaLayout ventana) {

		try {
			imagenFondo= new ImageIcon("src/resources/Pizarron.jpeg").getImage();
			imagenFlecha = new ImageIcon("src/resources/Flecha.png").getImage();
		} catch (Exception e) {
			System.out.println("Error al cargar la imagen");
		}

		configurarPantalla(ventana);
	}

	private void configurarPantalla(VentanaLayout ventana) {
	    setLayout(null);

	    EntidadView vistaJugador1 = new EntidadView("Mago", 1.8f);
	    EntidadView vistaJugador2 = new EntidadView("Arquero", 1.7f);
	    EntidadView vistaJugador3 = new EntidadView("Curandera", 1.8f);
	    EntidadView vistaJugador4 = new EntidadView("Guerrero", 1.8f);

	    vistaJugador1.setBounds(70, 120, 270, 440);
	    vistaJugador2.setBounds(260, 150, 270, 440);
	    vistaJugador3.setBounds(460, 120, 270, 440);
	    vistaJugador4.setBounds(650, 120, 270, 440);

	    configurarEventoHover(vistaJugador1);
	    configurarEventoHover(vistaJugador2);
	    configurarEventoHover(vistaJugador3);
	    configurarEventoHover(vistaJugador4);

	    vistaJugador1.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) { ventana.verJugador1(); }
	    });
	    vistaJugador2.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) { ventana.verJugador2(); }
	    });
	    vistaJugador3.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) { ventana.verJugador3(); }
	    });
	    vistaJugador4.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) { ventana.verJugador4(); }
	    });

	    add(vistaJugador1);
	    add(vistaJugador2);
	    add(vistaJugador3);
	    add(vistaJugador4);

	    // --- BOTÓN VOLVER AL MENÚ ---
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

	private void configurarEventoHover(EntidadView vista) {
	    vista.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseEntered(MouseEvent e) {
	        	entidadSeleccionada = vista;
	            repaint();
	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	        	entidadSeleccionada = null;
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

		if (imagenFlecha != null && entidadSeleccionada != null) {
			int anchoFlecha = 60;
			int altoFlecha = 60;
			int xCentrada = entidadSeleccionada.getX() + (entidadSeleccionada.getWidth() / 2 + 7) - (anchoFlecha / 2);
			int yArriba = entidadSeleccionada.getY() - altoFlecha + 30;
			g.drawImage(imagenFlecha, xCentrada, yArriba, anchoFlecha, altoFlecha, this);
		}
	}
}