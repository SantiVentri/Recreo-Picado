package main;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

import views.Jugador1;
import views.Jugador2;
import views.Jugador3;
import views.Jugador4;
import views.MenuPanel;
import views.MyTeam;
import views.WelcomePanel;
import views.PartidasPanel;

import utils.ReproductorMusica;

public class VentanaLayout extends JFrame {
	CardLayout cl;
	JPanel mainPanel;
	private ReproductorMusica musica;
	
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
	
	public void cargarVentana() {
		cl = new CardLayout();
		mainPanel = new JPanel(cl);
		
		mainPanel.add(new WelcomePanel(this), "WELCOME");
		mainPanel.add(new MenuPanel(this), "MENU");
		mainPanel.add(new MyTeam(this), "MYTEAM");
		mainPanel.add(new Jugador1(this), "JUGADOR1");
		mainPanel.add(new Jugador2(this), "JUGADOR2");
		mainPanel.add(new Jugador3(this), "JUGADOR3");
		mainPanel.add(new Jugador4(this), "JUGADOR4");
		mainPanel.add(new PartidasPanel(this), "PARTIDASPANEL");
		
		cl.show(mainPanel, "WELCOME");
		add(mainPanel);
	}
	
	public void empezarJuego() {
		cl.show(mainPanel, "MENU");
	}
	public void partidasPanel() {
		cl.show(mainPanel, "PARTIDASPANEL");
	}
	
	public void volverAtras() {
		cl.show(mainPanel, "WELCOME");
	}
	
	public void verBatallas() {
		cl.show(mainPanel, "BATALLAS");
	}
	
	public void verEquipo() {
		cl.show(mainPanel, "MYTEAM");
	}
	
	public void verJugador1() {
		cl.show(mainPanel, "JUGADOR1");
	}
	public void verJugador2() {
		cl.show(mainPanel, "JUGADOR2");
	}
	public void verJugador3() {
		cl.show(mainPanel, "JUGADOR3");
	}
	public void verJugador4() {
		cl.show(mainPanel, "JUGADOR4");
	}
	public void volverMenu() {
		cl.show(mainPanel, "MENU");
	}
	
	public void volverJugadores() {
		cl.show(mainPanel, "MYTEAM");
	}
}
