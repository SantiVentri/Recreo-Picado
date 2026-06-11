package main;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

import modelo.Partida;
import views.*;

import utils.ReproductorMusica;

public class VentanaLayout extends JFrame {
	private CardLayout cl;
	private JPanel mainPanel;
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
		
		Partida p = new Partida();
		
		mainPanel.add(new WelcomePanel(this), "INICIO");
		mainPanel.add(new PartidasPanel(this), "PARTIDAS");
		mainPanel.add(new MenuPanel(this), "MENU");
		mainPanel.add(new MyTeam(this), "EQUIPO");
		mainPanel.add(new Jugador1(this), "JUGADOR1");
		mainPanel.add(new Jugador2(this), "JUGADOR2");
		mainPanel.add(new Jugador3(this), "JUGADOR3");
		mainPanel.add(new Jugador4(this), "JUGADOR4");
		mainPanel.add(new LevelsPanel(this, p.getBatallas()), "NIVELES");
		
		cl.show(mainPanel, "INICIO");
		add(mainPanel);
	}
	
	public void verPartidas() {
		cl.show(mainPanel, "PARTIDAS");
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
}
