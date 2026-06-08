package views;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import main.VentanaLayout;

public class WelcomePanel extends JPanel {
    private Image imagenFondo;
    private JButton btnEmpezar;

    public WelcomePanel(VentanaLayout ventana) {
    	setLayout(null);
    	
        try {
            imagenFondo = new ImageIcon("src/resources/Welcome.jpg").getImage();
        } catch (Exception e) {
            System.out.println("Error: No se encontró la imagen de fondo.");
        }
        
        // Configurar el botón "Invisible"
        btnEmpezar = new JButton("");
        
        // Hacerlo transparente
        btnEmpezar.setOpaque(false);
        btnEmpezar.setContentAreaFilled(false);
        btnEmpezar.setBorderPainted(false);
        
        // Posicionamiento absoluto (x, y, ancho, alto)
        btnEmpezar.setBounds((ventana.getWidth() / 2) - 100, 300, 220, 200); 
        
        // Cursor de mano para que el usuario sepa que es clickeable
        btnEmpezar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Acción de navegación
        btnEmpezar.addActionListener(e -> ventana.empezarJuego());
        
        add(btnEmpezar);
	}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            // Dibuja la imagen para que ocupe todo el panel
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
