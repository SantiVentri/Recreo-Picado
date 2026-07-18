package views;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import modelo.Partida;
import modelo.Repositorio;

public class PartidasPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
    private Image imagenFondo;
    private VentanaLayout ventana;
    
    public PartidasPanel (VentanaLayout ventana) {    	
    	this.ventana = ventana;
    	
    	try {
            imagenFondo = new ImageIcon("src/resources/Seleccionar-batalla.png").getImage();
            
        } catch (Exception e) {
            System.out.println("Error: No se encontró la imagen de fondo.");
        }
    	 configurarInicioPartida(ventana);
    }
    
    /**
     * Reconstruye el contenido del panel para reflejar el estado actual
     * de la partida guardada en disco (por ejemplo, el nivel alcanzado).
     * Se llama cada vez que se vuelve a mostrar esta pantalla.
     */
    public void refrescar() {
    	removeAll();
    	configurarInicioPartida(ventana);
    	revalidate();
    	repaint();
    }
    
    
    private void configurarInicioPartida(VentanaLayout ventana) {
    	setLayout(null);
    	
    	boolean hayGuardado = Repositorio.getInstance().hayPartidaGuardada();
    	
    	//Tamaño y posicion unicos, centrados en el panel, para el boton
    	//"grande" que corresponda mostrar: Nueva Partida (si no hay guardado)
    	//o Cargar Partida (si hay guardado). Solo se dibuja uno de los dos.
    	int anchoBoton = 270, altoBoton = 240;
    	int xBoton = (1000 - anchoBoton) / 2;
    	int yBoton = 230;
    	
    	if (!hayGuardado) {
    		//Imagen de nueva Partida
    		ImageIcon iconoOriginal = new ImageIcon("src/resources/Nueva-partida.png");
    		Image imageEscalada = iconoOriginal.getImage().getScaledInstance(anchoBoton, altoBoton, Image.SCALE_SMOOTH);
    		ImageIcon iconoPartidaNueva = new ImageIcon(imageEscalada);
    		JButton botonPartidaNueva = new JButton(iconoPartidaNueva);
    		
    		botonPartidaNueva.setBounds(xBoton, yBoton, anchoBoton, altoBoton);
    		
    		//Sacar borde y fondo
    		botonPartidaNueva.setBorderPainted(false);
    		botonPartidaNueva.setContentAreaFilled(false);
    		botonPartidaNueva.setFocusPainted(false);
    		
    		//Accion al hacer click
    		botonPartidaNueva.addActionListener(e -> ventana.iniciarNuevaPartida());
    		
    		add(botonPartidaNueva);
    		return;
    	}
    	
    	//Hay una partida guardada: se muestra unicamente el boton de Cargar Partida,
    	//en el mismo lugar y con el mismo tamaño que tendria el de Nueva Partida.
    	ImageIcon iconoOriginal2 = new ImageIcon("src/resources/Partida-cargada.png");
    	Image imagenEscalada2 = iconoOriginal2.getImage().getScaledInstance(anchoBoton, altoBoton, Image.SCALE_SMOOTH);
    	ImageIcon iconoPartidaCargada = new ImageIcon (imagenEscalada2);
    	JButton botonPartidaCargada = new JButton(iconoPartidaCargada);
    	
    	botonPartidaCargada.setBounds(xBoton, yBoton, anchoBoton, altoBoton);
    	
    	//sacar borde y fondo
    	botonPartidaCargada.setBorderPainted(false);
    	botonPartidaCargada.setContentAreaFilled(false);
    	botonPartidaCargada.setFocusPainted(false);
    	
    	//El texto se dibuja centrado, encima de la imagen, en vez de al costado
    	botonPartidaCargada.setHorizontalTextPosition(SwingConstants.CENTER);
    	botonPartidaCargada.setVerticalTextPosition(SwingConstants.CENTER);
    	botonPartidaCargada.setHorizontalAlignment(SwingConstants.CENTER);
    	botonPartidaCargada.setVerticalAlignment(SwingConstants.CENTER);
    	botonPartidaCargada.setForeground(new Color(25, 40, 100));
    	botonPartidaCargada.setFont(new Font("Arial", Font.BOLD, 18));
    	botonPartidaCargada.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	
    	Partida partidaGuardada = Repositorio.getInstance().leerPartidaGuardada();
    	int nivel = (partidaGuardada != null) ? partidaGuardada.getNivelActual() : 1;
    	botonPartidaCargada.setText("<html><div style='text-align:center'>Cargar<br>Partida<br>Nivel " + nivel + "</div></html>");
    	botonPartidaCargada.addActionListener(e -> ventana.cargarPartidaGuardada());
    	
    	add(botonPartidaCargada);
    	
    	//Boton de "Borrar partida", centrado horizontalmente en la pantalla,
    	//justo debajo del post-it.
    	int borrarAncho = 170;
    	int borrarAlto = (int) Math.round(borrarAncho / 2.0274);
    	int xBorrar = (1000 - borrarAncho) / 2;
    	int yBorrar = yBoton + altoBoton + 8;
    	
    	ImageIcon iconoBorrar = new ImageIcon("src/resources/Borrar-partida.png");
    	Image imagenBorrarEscalada = iconoBorrar.getImage().getScaledInstance(borrarAncho, borrarAlto, Image.SCALE_SMOOTH);
    	JButton botonBorrarPartida = new JButton(new ImageIcon(imagenBorrarEscalada));
    	
    	botonBorrarPartida.setBounds(xBorrar, yBorrar, borrarAncho, borrarAlto);
    	botonBorrarPartida.setBorderPainted(false);
    	botonBorrarPartida.setContentAreaFilled(false);
    	botonBorrarPartida.setFocusPainted(false);
    	botonBorrarPartida.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	botonBorrarPartida.setToolTipText("Borrar partida guardada");
    	botonBorrarPartida.addActionListener(e -> confirmarBorrado(ventana));
    	
    	add(botonBorrarPartida);
    }
    
    private void confirmarBorrado(VentanaLayout ventana) {
    	int opcion = JOptionPane.showConfirmDialog(
    			this,
    			"¿Seguro que queres borrar la partida guardada?\nEsta accion no se puede deshacer.",
    			"Borrar partida guardada",
    			JOptionPane.YES_NO_OPTION,
    			JOptionPane.WARNING_MESSAGE);
    	
    	if (opcion == JOptionPane.YES_OPTION) {
    		ventana.borrarPartidaGuardada();
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
