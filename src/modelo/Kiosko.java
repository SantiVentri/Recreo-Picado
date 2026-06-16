package modelo;

import javax.swing.*;
import main.VentanaLayout;
import java.awt.*;

public class Kiosko extends JPanel {
    
    private Item[] items;    
    
    private JLabel lblPesos;
    // Cambiamos a JList<Item> para aprovechar la POO (Programación Orientada a Objetos)
    private JList<Item> listaInventarioUI; 
    private VentanaLayout ventana; 

    public Kiosko(VentanaLayout ventana) {
        this.ventana = ventana;
        this.setLayout(new BorderLayout());
        
        
        this.items = new Item[] {
            // --- POCIONES ---
            ItemFactory.crearMielcita(),
            ItemFactory.crearBaggio(),
            ItemFactory.crearTita(),
            ItemFactory.crearAlfajor(),
            ItemFactory.crearCocacola(),
            ItemFactory.crearManaos(),
            
            // --- ARMAS ---
            ItemFactory.crearLapiz(),
            ItemFactory.crearRegla(),
            ItemFactory.crearCompas(),
            ItemFactory.crearTijera(),
            ItemFactory.crearGomera(),
            ItemFactory.crearLiquidPaper(),
            ItemFactory.crearBeyblade(),
            
            // --- ARMADURAS ---
            ItemFactory.crearGuantes(),
            ItemFactory.crearTopper(),
            ItemFactory.crearBotines(),
            ItemFactory.crearGuardapolvo(),
            ItemFactory.crearCamisetaDyJ(),
            ItemFactory.crearCamisetaSeleccion(),
            ItemFactory.crearCamperaEgresados()
        };

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        // Indicador de pesos del usuario
        JPanel panelSuperior = new JPanel();
        lblPesos = new JLabel("Pesos de la Party: $" + Repositorio.getInstance().getPartidaActual().getPesos()); 
        lblPesos.setFont(new Font("Arial", Font.BOLD, 24));
        panelSuperior.add(lblPesos);
        this.add(panelSuperior, BorderLayout.NORTH);

        // Panel de inventario de tienda
        DefaultListModel<Item> modeloLista = new DefaultListModel<>();
        for (Item item : items) {
            modeloLista.addElement(item); // Agregamos el objeto entero, no solo el texto
        }
        
        listaInventarioUI = new JList<>(modeloLista);
        
        // Usamos un CellRenderer para personalizar la UI (Interfaz de Usuario) de la lista
        listaInventarioUI.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                // Hacemos un Cast (conversión explícita de un tipo de dato a otro)
                Item item = (Item) value; 
                
                // Seteamos el texto
                label.setText(item.getNombre() + " - $" + item.getValor() + " (" + item.getDescripcion() + ")");
                
                // Intentamos cargar y escalar la imagen del ítem
                try {
                    ImageIcon iconoOriginal = new ImageIcon(item.getRutaImagen());
                    // Escalamos la imagen a 32x32 píxeles para que no deforme la lista
                    Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                    label.setIcon(new ImageIcon(imagenEscalada));
                } catch (Exception e) {
                    System.out.println("No se pudo cargar la imagen: " + item.getRutaImagen());
                }
                
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaInventarioUI);
        
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BorderLayout()); // Para que el scroll ocupe todo el espacio
        panelCentro.add(scrollPane, BorderLayout.CENTER);
        this.add(panelCentro, BorderLayout.CENTER);

        // Botones de acciones (comprar, salir)
        JPanel panelInferior = new JPanel();
        JButton btnComprar = new JButton("Comprar Seleccionado");
        JButton btnSalir = new JButton("Volver");

        btnComprar.addActionListener(e -> {
            Item itemSeleccionado = listaInventarioUI.getSelectedValue();
            if (itemSeleccionado != null) {
                comprarItem(itemSeleccionado, 1); 
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un ítem primero.");
            }
        });

        btnSalir.addActionListener(e -> {
            ventana.verMenu();
        });

        panelInferior.add(btnComprar);
        panelInferior.add(btnSalir);
        this.add(panelInferior, BorderLayout.SOUTH);
    }

    // Métodos
    public void comprarItem(Item item, int cantidad) {
        int costoTotal = item.getValor() * cantidad;
        
        if (Repositorio.getInstance().getPartidaActual().getPesos() >= costoTotal) {
            
            // BUG CORREGIDO: Se restaba 'cantidad' en vez de 'costoTotal'
            Repositorio.getInstance().getPartidaActual().quitarPesos(costoTotal);
            Repositorio.getInstance().getPartidaActual().agregarItem(item);
            
            lblPesos.setText("Pesos de la Party: $" + Repositorio.getInstance().getPartidaActual().getPesos());
            JOptionPane.showMessageDialog(this, "¡Compraste " + cantidad + " " + item.getNombre() + "!");
        } else {
            JOptionPane.showMessageDialog(this, "Pesos insuficientes.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Cuando tengas el fondo del kiosko, asegúrate de que la ruta sea correcta
        Image imagenFondo = new ImageIcon("src/resources/fondos/fondo_kiosko.png").getImage();
        g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
    }
}
