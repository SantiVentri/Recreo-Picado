package views;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PartidasPanel extends JPanel {
	
private static final long serialVersionUID = 1L;
	
	private JButton botonSeleccionado = null;
    private Image imagenFondo;
    
    public PartidasPanel (VentanaLayout ventana) {    	
    	try {
            imagenFondo = new ImageIcon("src/resources/Seleccionar-batalla.png").getImage();
            
        } catch (Exception e) {
            System.out.println("Error: No se encontró la imagen de fondo.");
        }
    	 configurarInicioPartida(ventana);
    }
    
    
    private void configurarInicioPartida(VentanaLayout ventana) {
    	setLayout(null);
    	
    	//Imagen de nueva Partida
    	ImageIcon iconoOriginal = new ImageIcon("src/resources/Nueva-partida.png");
    	Image imageEscalada = iconoOriginal.getImage().getScaledInstance(270, 240, Image.SCALE_SMOOTH);
    	ImageIcon iconoPartidaNueva = new ImageIcon(imageEscalada);
    	JButton botonPartidaNueva = new JButton(iconoPartidaNueva);
    	
    	//tamaño de boton de nueva partida 
    	
    	botonPartidaNueva.setBounds(290, 230, 270, 240);
    	
    	//Sacar borde y fondo
    	botonPartidaNueva.setBorderPainted(false);
    	botonPartidaNueva.setContentAreaFilled(false);
    	botonPartidaNueva.setFocusPainted(false);
    	
    	//Accion al hacer click
    	botonPartidaNueva.addActionListener(e -> ventana.verMenu());
    	
    	add(botonPartidaNueva);
    	
    	//Imagen partida cargada 
    	
    	ImageIcon iconoOriginal2 = new ImageIcon("src/resources/Partida-Cargada.png");
    	Image imagenEscalada2 = iconoOriginal2.getImage().getScaledInstance(290,270, Image.SCALE_SMOOTH);
    	ImageIcon iconoPartidaCargada = new ImageIcon (imagenEscalada2);
    	JButton botonPartidaCargada = new JButton(iconoPartidaCargada);
    	
    	//tamaño de boton de partida cargada
    	
    	botonPartidaCargada.setBounds(530,210,290,270);
    	
    	//sacar borde y fondo
    	botonPartidaCargada.setBorderPainted(false);
    	botonPartidaCargada.setContentAreaFilled(false);
    	botonPartidaCargada.setFocusPainted(false);
    	
    	//Accion al hacer click 
    	botonPartidaCargada.addActionListener(e -> ventana.verMenu());
    	
    	add(botonPartidaCargada);
    	
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }   
}



