package views;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import main.VentanaLayout;

public class MenuPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
    private Image imagenFondo;
    private Image imagenFlecha;
    private JButton btnEmpezar, btnEquipo, btnKiosko, btnSalir;
    private JButton botonSeleccionado = null;

    public MenuPanel(VentanaLayout ventana) {    	
        try {
            imagenFondo = new ImageIcon("src/resources/Menu.jpg").getImage();
            imagenFlecha = new ImageIcon("src/resources/Flecha.png").getImage();
        } catch (Exception e) {
            System.out.println("Error: No se encontró la imagen de fondo.");
        }
        
        configurarMenu(ventana);
    }
    
    private void configurarMenu(VentanaLayout ventana) {
    	setLayout(null);        
        
        setLayout(new GridLayout(1, 4, 20, 0));
        setBorder(BorderFactory.createEmptyBorder(200, 120, 200, 120));
        
        btnEmpezar = new JButton("");
        btnEquipo = new JButton("");
        btnKiosko = new JButton("");
        btnSalir = new JButton("");
        
        JButton[] buttons = {btnEmpezar, btnEquipo, btnKiosko, btnSalir};
        
        for (JButton button : buttons) {
        	button.setOpaque(false);
        	button.setContentAreaFilled(false);
        	button.setBorderPainted(false);
        	button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            configurarEventoHover(button);
            add(button);
        } 
        
        // Acción de navegación
        btnEmpezar.addActionListener(e -> ventana.verBatallas());
        btnEquipo.addActionListener(e -> ventana.verEquipo());
        // btnKiosko.addActionListener(e -> ventana.verKisoko());
        btnSalir.addActionListener(e -> ventana.volverAtras());
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
            
            int yArriba = botonSeleccionado.getY() - altoFlecha + 7;

            g.drawImage(imagenFlecha, xCentrada, yArriba, anchoFlecha, altoFlecha, this);
        }
    }
}
