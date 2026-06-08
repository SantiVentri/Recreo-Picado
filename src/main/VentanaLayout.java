package main;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

import views.MenuPanel;
import views.WelcomePanel;

public class VentanaLayout extends JFrame {
	CardLayout cl;
	JPanel mainPanel;
	
	public VentanaLayout() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Recreo Picado - Grupo 5");
		setSize(new Dimension(1000, 600));
		setResizable(false);
		setLocationRelativeTo(null);
		
		cargarVentana();
		
		setVisible(true);
	}
	
	public void cargarVentana() {
		cl = new CardLayout();
		mainPanel = new JPanel(cl);
		
		mainPanel.add(new WelcomePanel(this), "WELCOME");
		mainPanel.add(new MenuPanel(this), "MENU");
		
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
}
