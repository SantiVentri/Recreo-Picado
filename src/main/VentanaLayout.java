package main;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

import views.Jugador1;
import views.MenuPanel;
import views.MyTeam;
import views.WelcomePanel;

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
		
		cl.show(mainPanel, "WELCOME");
		add(mainPanel);
	}
	
	public void empezarJuego() {
		cl.show(mainPanel, "MENU");
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
}
