package modelo;

import modelo.itemFactory;
import javax.swing.*;

import main.VentanaLayout;

import java.awt.*;

public class Kiosko extends JPanel {
    
    private Item[] items;    
    
    private JLabel lblPesos;
    private JList<String> listaInventarioUI;
    private VentanaLayout ventana; 

    public Kiosko(VentanaLayout ventana) {
        this.ventana = ventana;
        this.setLayout(new BorderLayout());
        
        this.items = new Item[] {
            itemFactory.crearAlfajor(),
            itemFactory.crearManaos(),
            itemFactory.crearGomera(),
            itemFactory.crearGuardapolvo(),
            itemFactory.crearCamperaEgresados()
        };

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        //Indicador de pesos del usuario
        JPanel panelSuperior = new JPanel();
        lblPesos = new JLabel("Pesos de la Party: $" + Repositorio.getInstance().getPartidaActual().getPesos()); 
        lblPesos.setFont(new Font("Arial", Font.BOLD, 24));
        panelSuperior.add(lblPesos);
        this.add(panelSuperior, BorderLayout.NORTH);

        //Panel de inventario de tienda
        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        for (Item item : items) {
            modeloLista.addElement(item.getNombre() + " - $" + item.getValor() + " (" + item.getDescripcion() + ")");
        }
        listaInventarioUI = new JList<>(modeloLista);
        JScrollPane scrollPane = new JScrollPane(listaInventarioUI);
        
        JPanel panelCentro = new JPanel();
        panelCentro.add(scrollPane);
        this.add(panelCentro, BorderLayout.CENTER);

        //Botones de acciones (comprar, salir)
        JPanel panelInferior = new JPanel();
        JButton btnComprar = new JButton("Comprar Seleccionado");
        JButton btnSalir = new JButton("Volver");

        btnComprar.addActionListener(e -> {
            int index = listaInventarioUI.getSelectedIndex();
            if (index != -1) {
                comprarItem(items[index], 1); 
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
            
            
        	Repositorio.getInstance().getPartidaActual().quitarPesos(cantidad);
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
        
        Image imagenFondo = new ImageIcon("ruta/al/fondo_kiosko.png").getImage();
        g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
    }
}
