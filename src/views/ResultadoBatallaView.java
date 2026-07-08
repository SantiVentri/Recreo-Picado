package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import modelo.Item;
import modelo.Recompensa;

public class ResultadoBatallaView extends JPanel {

    private JLabel lblTituloImagen;
    private JLabel lblMensaje;
    private JLabel lblOroXP;
    private JPanel panelItems;
    private JButton btnContinuar;
    private ClickListener listener;

    public interface ClickListener {
        void onContinuar();
    }

    public ResultadoBatallaView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(30, 30, 30, 240)); // Fondo oscuro semi-transparente
        setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 3));
        setPreferredSize(new Dimension(500, 400));

        //imagen de Victoria o Derrota
        lblTituloImagen = new JLabel();
        lblTituloImagen.setAlignmentX(CENTER_ALIGNMENT);
        lblTituloImagen.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(lblTituloImagen);

        //Mensaje
        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 18));
        lblMensaje.setForeground(Color.WHITE);
        lblMensaje.setAlignmentX(CENTER_ALIGNMENT);
        add(lblMensaje);

        //mostrar Oro y Experiencia ganada
        lblOroXP = new JLabel("", SwingConstants.CENTER);
        lblOroXP.setFont(new Font("Arial", Font.BOLD, 16));
        lblOroXP.setForeground(new Color(255, 215, 0)); // Color dorado
        lblOroXP.setAlignmentX(CENTER_ALIGNMENT);
        lblOroXP.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblOroXP);

        //Items de recompensa
        panelItems = new JPanel();
        panelItems.setLayout(new BoxLayout(panelItems, BoxLayout.Y_AXIS));
        panelItems.setOpaque(false);
        panelItems.setAlignmentX(CENTER_ALIGNMENT);
        add(panelItems);

        //Botón para salir del panel y volver al menú
        btnContinuar = new JButton("Continuar");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 16));
        btnContinuar.setAlignmentX(CENTER_ALIGNMENT);
        btnContinuar.setMaximumSize(new Dimension(180, 40));
        btnContinuar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.onContinuar();
                }
            }
        });
        
        JPanel panelBoton = new JPanel();
        panelBoton.setOpaque(false);
        panelBoton.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));
        panelBoton.add(btnContinuar);
        panelBoton.setAlignmentX(CENTER_ALIGNMENT);
        add(panelBoton);
    }

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    public void configurar(boolean victoria, Recompensa recompensa) {
        panelItems.removeAll();
        String rutaImagen = victoria ? "/resources/Victoria.png" : "/resources/Derrota.png";
        
        try {
            ImageIcon iconOriginal = new ImageIcon(getClass().getResource(rutaImagen));
            // Escalamos la imagen PNG para que encaje en el cartel
            Image imgEscalada = iconOriginal.getImage().getScaledInstance(320, 110, Image.SCALE_SMOOTH);
            lblTituloImagen.setIcon(new ImageIcon(imgEscalada));
        } catch (Exception e) {
            System.out.println("No se encontró la imagen: " + rutaImagen);
        }

        if (victoria && recompensa != null) {
            lblMensaje.setText("¡Dominaste el recreo y ganaste la batalla!");
            lblOroXP.setText("Recompensas: +" + recompensa.getOro() + " Pesos  |  +" + recompensa.getXp() + " XP");
            
            JLabel lblTituloItems = new JLabel("Ítems obtenidos para tu inventario:");
            lblTituloItems.setFont(new Font("Arial", Font.BOLD, 14));
            lblTituloItems.setForeground(Color.LIGHT_GRAY);
            lblTituloItems.setAlignmentX(CENTER_ALIGNMENT);
            panelItems.add(lblTituloItems);

            List<Item> items = recompensa.getItems();
            for (Item item : items) {
                if (item != null) {
                    JLabel lblItem = new JLabel("• " + item.getNombre());
                    lblItem.setFont(new Font("Arial", Font.PLAIN, 15));
                    lblItem.setForeground(Color.WHITE);
                    lblItem.setAlignmentX(CENTER_ALIGNMENT);
                    panelItems.add(lblItem);
                }
            }
        } else {
            lblMensaje.setText("Has sido derrotado en el recreo...");
            lblOroXP.setText("Perdiste el progreso de esta batalla.");
        }

        revalidate();
        repaint();
    }
}