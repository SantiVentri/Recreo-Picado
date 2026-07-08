package views;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import modelo.Batalla;
import modelo.Partida;
import modelo.Repositorio;
import orquestador.Orquestador;
import utils.ReproductorMusica;

public class VentanaLayout extends JFrame {
	private CardLayout cl;
	private JPanel mainPanel;
	private ReproductorMusica musica;
	private Partida partidaActual;
	private BatallaPanel batallaPanel;
	private Map<String, JPanel> paneles = new HashMap<>();
	
	public VentanaLayout() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Recreo Picado - Grupo 5");
		setSize(new Dimension(1000, 600));
		setResizable(false);
		setLocationRelativeTo(null);
		
		//musicaMenu
		musica = new ReproductorMusica();
	    musica.reproducir("src/resources/himno-instrumental.wav");
		
		cargarVentana();
		
		setVisible(true);
	}
	
	/**
	 * Construye (o reconstruye) todas las pantallas del juego.
	 * Las pantallas de "INICIO" y "PARTIDAS" no dependen de que exista una partida.
	 * Las pantallas de juego propiamente dichas (menú, equipo, niveles, kiosko, batalla)
	 * solo se arman cuando ya hay una partida actual (nueva o cargada), para evitar
	 * mostrar datos de una partida vieja o inexistente.
	 */
	public void cargarVentana() {
		cl = new CardLayout();
		mainPanel = new JPanel(cl);
		
		partidaActual = Repositorio.getInstance().getPartidaActual();
		
		mainPanel.add(new WelcomePanel(this), "INICIO");
		mainPanel.add(new PartidasPanel(this), "PARTIDAS");
		
		if (partidaActual != null) {
			mainPanel.add(new MenuPanel(this), "MENU");
			mainPanel.add(new MyTeam(this), "EQUIPO");
			agregarPanel(new Jugador1(this), "JUGADOR1");
			agregarPanel(new Jugador2(this), "JUGADOR2");
			agregarPanel(new Jugador3(this), "JUGADOR3");
			agregarPanel(new Jugador4(this), "JUGADOR4");
			mainPanel.add(new LevelsPanel(this, partidaActual.getBatallas()), "NIVELES");
			mainPanel.add(new KioskoPanel(this), "KIOSKO");
			batallaPanel = new BatallaPanel(this);
			mainPanel.add(batallaPanel, "BATALLA");
		}
		
		cl.show(mainPanel, "INICIO");
		
		getContentPane().removeAll();
		add(mainPanel);
		revalidate();
		repaint();
	}
	
	/**
	 * Se llama cuando el jugador elige "Nueva partida" en la pantalla de selección.
	 * Crea una partida desde cero y reconstruye las pantallas de juego para que
	 * queden asociadas a esa partida nueva.
	 */
	public void iniciarNuevaPartida() {
		Repositorio.getInstance().crearPartida();
		cargarVentana();
		verMenu();
	}
	
	/**
	 * Se llama cuando el jugador elige "Cargar partida". Si existe una partida
	 * guardada en disco, la lee, la deja como partida actual y reconstruye las
	 * pantallas de juego para que reflejen el progreso guardado.
	 */
	public void cargarPartidaGuardada() {
		Partida guardada = Repositorio.getInstance().leerPartidaGuardada();
		
		if (guardada == null) {
			return;
		}
		
		Repositorio.getInstance().cargarPartida(guardada);
		cargarVentana();
		verMenu();
	}
	
	private void agregarPanel(JPanel panel, String nombre) {
		paneles.put(nombre, panel);
		mainPanel.add(panel, nombre);
	}
	
	public JPanel obtenerPanel(String nombre) {
		return paneles.get(nombre);
	}
	
	// Navegación
	public void verPartidas() {
		cl.show(mainPanel, "PARTIDAS");
	}
	
	/**
	 * Se llama cuando el jugador confirma borrar la partida guardada desde la
	 * pantalla de selección. Borra el archivo de guardado y refresca la
	 * pantalla para que el botón de cargar partida quede deshabilitado de nuevo.
	 */
	public void borrarPartidaGuardada() {
		Repositorio.getInstance().borrarPartidaGuardada();
		cargarVentana();
		verPartidas();
	}
	
	public void verMenu() {
		cl.show(mainPanel, "MENU");
	}
	
	public void verNiveles() {
		cl.show(mainPanel, "NIVELES");
	}
	
	public void verEquipo() {
		cl.show(mainPanel, "EQUIPO");
	}
	
	public void verJugador1() {
		cl.show(mainPanel, "JUGADOR1");
		 batallaPanel.repaint();

		    // IMPORTANTE
		    ((Jugador1) obtenerPanel("JUGADOR1")).refrescarInventario();
	}
	
	public void verJugador2() {
		cl.show(mainPanel, "JUGADOR2");
		((Jugador2) obtenerPanel("JUGADOR2")).refrescarInventario();
	}
	
	public void verJugador3() {
		cl.show(mainPanel, "JUGADOR3");
		((Jugador3) obtenerPanel("JUGADOR3")).refrescarInventario();
	}
	
	public void verJugador4() {
		cl.show(mainPanel, "JUGADOR4");
		((Jugador4) obtenerPanel("JUGADOR4")).refrescarInventario();
	}
	
	
	public void verKiosko() {
    	cl.show(mainPanel, "KIOSKO");
		
		// Actualiza los pesos del Kiosko antes de mostrarlo
		((KioskoPanel) obtenerPanel("KIOSKO")).actualizar();
	}
	
	public void empezarBatalla(Batalla batalla) {
	    Orquestador.getInstance().iniciarBatalla(partidaActual.getAlumnos(), batalla);
	    batallaPanel.removeAll();
	    batallaPanel.cargarPanel();
	    batallaPanel.cargarEntidades();
	    cl.show(mainPanel, "BATALLA");
	}
	
	// Getter
    public Partida getPartida() {
    	return partidaActual;
    }

}