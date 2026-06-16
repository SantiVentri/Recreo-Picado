package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import main.VentanaLayout;
import modelo.Item;
import modelo.Kiosko;
import modelo.Equipo;
import modelo.Entidad;
import modelo.Repositorio;

public class KioskoPanel extends JPanel {

    private VentanaLayout ventana;
    private Kiosko kioskoLogico;
    private CardLayout cardLayout; 

    private Image imagenFondoPrincipal;
    private Image imagenFondoCatalogo;

    private JLabel lblPesos;
    private JLabel lblItemMostrado;
    private JButton btnComprar;
    private Item itemSeleccionadoParaComprar;

    private Item[] todosLosItems;

    public KioskoPanel(VentanaLayout ventana) {
        this.ventana = ventana;
        this.kioskoLogico = new Kiosko();
        this.todosLosItems = kioskoLogico.getItemsDisponibles();

        this.cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        // Cargamos las imágenes (Asegurate de que la nueva imagen se llame KioskoBackground.jpg)
        try {
            imagenFondoPrincipal = new ImageIcon("src/resources/KioskoBackground.jpg").getImage();
            imagenFondoCatalogo = new ImageIcon("src/resources/Kiosko-modal.jpg").getImage();
        } catch (Exception e) {
            System.err.println("No se pudieron cargar las imágenes del Kiosko.");
        }

        JPanel panelPrincipal = crearPanelPrincipal();
        JPanel panelCatalogo = crearPanelCatalogo();

        this.add(panelPrincipal, "VISTA_PRINCIPAL");
        this.add(panelCatalogo, "VISTA_CATALOGO");

        cardLayout.show(this, "VISTA_PRINCIPAL");
    }

    // =========================================================================
    // 1. LA VISTA PRINCIPAL (Fondo nuevo)
    // =========================================================================
    private JPanel crearPanelPrincipal() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondoPrincipal != null) {
                    g.drawImage(imagenFondoPrincipal, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        // --- Panel Superior ---
        JPanel panelNorte = new JPanel();
        panelNorte.setOpaque(false);
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS));

        Equipo equipoJugador = Repositorio.getInstance().getPartidaActual().getAlumnos();
        lblPesos = new JLabel("Pesos de la Party: $" + equipoJugador.getPesos());
        lblPesos.setFont(new Font("Arial", Font.BOLD, 28));
        lblPesos.setForeground(Color.WHITE);
        lblPesos.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPesos.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        
        panelNorte.add(lblPesos);
        panel.add(panelNorte, BorderLayout.NORTH);

        // --- Panel Central (Ítem centrado) ---
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setOpaque(false);
        
        lblItemMostrado = new JLabel();
        // AHORA EL ÍTEM APARECE CENTRADO EN LA PANTALLA
        lblItemMostrado.setVerticalAlignment(SwingConstants.CENTER);
        lblItemMostrado.setHorizontalAlignment(SwingConstants.CENTER);
        lblItemMostrado.setVisible(false); 
        
        panelCentro.add(lblItemMostrado, BorderLayout.CENTER);
        panel.add(panelCentro, BorderLayout.CENTER);

        // --- Panel Inferior (Botones) ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelSur.setOpaque(false);

        JButton btnVerCatalogo = new JButton("Ver Catálogo");
        btnComprar = new JButton("Comprar Ítem");
        JButton btnVolver = new JButton("Salir del Kiosko");

        Font fontBotones = new Font("Arial", Font.BOLD, 20);
        btnVerCatalogo.setFont(fontBotones);
        btnComprar.setFont(fontBotones);
        btnVolver.setFont(fontBotones);

        btnComprar.setEnabled(false); 

        btnVerCatalogo.addActionListener(e -> cardLayout.show(this, "VISTA_CATALOGO"));
        btnComprar.addActionListener(e -> accionComprar());
        btnVolver.addActionListener(e -> ventana.verMenu());

        panelSur.add(btnVerCatalogo);
        panelSur.add(btnComprar);
        panelSur.add(btnVolver);
        panelSur.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0)); 
        panel.add(panelSur, BorderLayout.SOUTH);

        return panel;
    }

    // =========================================================================
    // 2. LA VISTA DEL CATÁLOGO
    // =========================================================================
    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondoCatalogo != null) {
                    g.drawImage(imagenFondoCatalogo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        JLabel lblTituloCat = new JLabel("Seleccioná un ítem");
        lblTituloCat.setFont(new Font("Arial", Font.BOLD, 24));
        lblTituloCat.setForeground(Color.WHITE);
        lblTituloCat.setHorizontalAlignment(SwingConstants.CENTER);
        lblTituloCat.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panel.add(lblTituloCat, BorderLayout.NORTH);

        DefaultListModel<Item> modeloLista = new DefaultListModel<>();
        for (Item item : todosLosItems) {
            modeloLista.addElement(item);
        }

        JList<Item> listaInventarioUI = new JList<>(modeloLista);
        listaInventarioUI.setOpaque(false);
        listaInventarioUI.setBackground(new Color(0, 0, 0, 0));
        listaInventarioUI.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        listaInventarioUI.setVisibleRowCount(-1);

        listaInventarioUI.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Item item = (Item) value;

                label.setText("<html><div style='text-align: center; width: 80px;'>" + 
                                item.getNombre() + "<br><font color='yellow'>$" + item.getValor() + "</font></div></html>");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalTextPosition(SwingConstants.BOTTOM);
                label.setHorizontalTextPosition(SwingConstants.CENTER);

                if (isSelected) {
                    label.setBackground(new Color(255, 255, 255, 80));
                    label.setOpaque(true);
                } else {
                    label.setOpaque(false);
                }
                label.setForeground(Color.WHITE);

                try {
                    ImageIcon iconoOriginal = new ImageIcon(item.getRutaImagen());
                    Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                    label.setIcon(new ImageIcon(imagenEscalada));
                } catch (Exception e) {}

                label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                return label;
            }
        });

        listaInventarioUI.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Item elegido = listaInventarioUI.getSelectedValue();
                if (elegido != null) {
                    actualizarItemSeleccionado(elegido);
                    cardLayout.show(KioskoPanel.this, "VISTA_PRINCIPAL"); 
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaInventarioUI);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setOpaque(false);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        panelCentro.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelCentro, BorderLayout.CENTER);

        JPanel panelSur = new JPanel();
        panelSur.setOpaque(false);
        JButton btnCancelar = new JButton("Volver");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 18));
        btnCancelar.addActionListener(e -> cardLayout.show(this, "VISTA_PRINCIPAL"));
        
        panelSur.add(btnCancelar);
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panel.add(panelSur, BorderLayout.SOUTH);

        return panel;
    }

    // =========================================================================
    // LÓGICA DE ACTUALIZACIÓN Y COMPRA
    // =========================================================================

    private void actualizarItemSeleccionado(Item item) {
        this.itemSeleccionadoParaComprar = item;

        if (item != null) {
            try {
                ImageIcon iconoOriginal = new ImageIcon(item.getRutaImagen());
                Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblItemMostrado.setIcon(new ImageIcon(imagenEscalada));
                
                lblItemMostrado.setText("<html><div style='text-align:center;'><font color='white' size='6'><b>" + 
                                       item.getNombre() + "</b></font><br><font color='yellow' size='5'>$" + 
                                       item.getValor() + "</font></div></html>");
                
                lblItemMostrado.setVisible(true); 
                btnComprar.setEnabled(true);     
                btnComprar.setText("Comprar (" + item.getValor() + " Pesos)");
            } catch (Exception e) {
                System.err.println("Error cargando imagen grande del ítem");
            }
        } else {
            lblItemMostrado.setVisible(false);
            btnComprar.setEnabled(false);
            btnComprar.setText("Comprar Ítem");
        }
    }

    private void accionComprar() {
        if (itemSeleccionadoParaComprar != null) {
            Equipo equipo = Repositorio.getInstance().getPartidaActual().getAlumnos();
            List<Entidad> personajes = equipo.getEntidades();

            String[] nombresPersonajes = new String[personajes.size()];
            for (int i = 0; i < personajes.size(); i++) {
                nombresPersonajes[i] = personajes.get(i).getNombre(); 
            }

            String elegido = (String) JOptionPane.showInputDialog(
                    this, "¿A quién le damos " + itemSeleccionadoParaComprar.getNombre() + "?",
                    "Asignar Ítem", JOptionPane.QUESTION_MESSAGE, null, nombresPersonajes, nombresPersonajes[0]
            );

            if (elegido != null) {
                Entidad destinatario = null;
                for (Entidad p : personajes) {
                    if (p.getNombre().equals(elegido)) {
                        destinatario = p;
                        break;
                    }
                }
                
                boolean compraExitosa = kioskoLogico.comprarItem(itemSeleccionadoParaComprar, destinatario, equipo);
                
                if (compraExitosa) {
                    lblPesos.setText("Pesos de la Party: $" + equipo.getPesos());
                    JOptionPane.showMessageDialog(this, "¡" + destinatario.getNombre() + " recibió " + itemSeleccionadoParaComprar.getNombre() + "!");
                    actualizarItemSeleccionado(null); 
                } else {
                    JOptionPane.showMessageDialog(this, "Pesos insuficientes.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}