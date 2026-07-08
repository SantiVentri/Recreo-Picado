package views;

import javax.swing.*;
import java.awt.*;

import modelo.Item;
import modelo.Kiosko;
import modelo.Equipo;
import modelo.Repositorio;


public class KioskoPanel extends JPanel {

    private static final String VISTA_CATALOGO   = "catalogo";
    private static final String VISTA_DETALLE    = "detalle";

    private final VentanaLayout ventana;
    private final Kiosko kioskoLogico;
    private final Item[] todosLosItems;

    private final Image imagenFondo;
    private final Image imagenModal;

    private final CardLayout cardLayout;
    private final JPanel     contenedor;

    private Item itemSeleccionado;

    private JLabel  lblPesos;
    private JPanel  panelDetalleItem;
    private JButton btnComprar;

   
    public KioskoPanel(VentanaLayout ventana) {
        this.ventana       = ventana;
        this.kioskoLogico  = new Kiosko();
        this.todosLosItems = kioskoLogico.getItemsDisponibles();

        this.imagenFondo = cargarImagen("src/resources/KioskoBackground.png");
        this.imagenModal  = cargarImagen("src/resources/Kiosko-modal.png");

        this.setLayout(new BorderLayout());
        this.add(crearHUD(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contenedor = new JPanel(cardLayout);
        contenedor.setOpaque(false);

        contenedor.add(crearVistaCatalogo(),   VISTA_CATALOGO);
        contenedor.add(crearVistaDetalle(),    VISTA_DETALLE);

        this.add(contenedor, BorderLayout.CENTER);
        cardLayout.show(contenedor, VISTA_CATALOGO);
    }

    // Actualiza los pesos de la party en el HUD
    public void actualizar() {
        if (lblPesos != null) {
            int pesosActuales = Repositorio.getInstance().getPartidaActual().getPesos();
            lblPesos.setText("Pesos de la Party: $" + pesosActuales);
        }
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

   
    private JPanel crearHUD() {
        JPanel hud = new JPanel(new FlowLayout(FlowLayout.CENTER));
        hud.setOpaque(false);
        hud.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        lblPesos = new JLabel("Pesos de la Party: $" + Repositorio.getInstance().getPartidaActual().getPesos());
        lblPesos.setFont(new Font("Arial", Font.BOLD, 26));
        lblPesos.setForeground(Color.WHITE);
        hud.add(lblPesos);

        return hud;
    }


    private int[] rectModal(int vw, int vh) {
        int mw = (int) (vw * 0.75);
        int mh = (int) (vh * 0.80);
        int mx = (vw - mw) / 2;
        int my = (vh - mh) / 2;
        return new int[]{mx, my, mw, mh};
    }

    
    private JPanel crearVistaCatalogo() {
        JPanel vista = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenModal != null) {
                    int[] r = rectModal(getWidth(), getHeight());
                    g.drawImage(imagenModal, r[0], r[1], r[2], r[3], this);
                }
            }
        };
        vista.setOpaque(false);

       JPanel panelItems = new JPanel(null);
       panelItems.setOpaque(false);
       vista.add(panelItems);
       
       ItemView[] slots = new ItemView[20];
       
       for (int i = 0; i < 20; i++) {
    	   Item item = ( i < todosLosItems.length)
    			   ? todosLosItems[i]
    				: null;
    	   
    	   slots[i] = crearSlotItem(item);
    	   panelItems.add(slots[i]);
       }
     
       double[][] posiciones = {

    		    {0.28, 0.35}, //mielsita 
    		    {0.40, 0.35}, //bagio
    		    {0.53, 0.35},//tita
    		    {0.65, 0.35},//alfajor
    		    {0.78, 0.35},//cocaLata

    		    {0.28, 0.52},//manaos
    		    {0.40, 0.52},//Lapiz
    		    {0.54, 0.52},//regla
    		    {0.66, 0.52},//compas
    		    {0.79, 0.52},//tijera

    		    {0.25, 0.69},//gomera
    		    {0.40, 0.67},//liqui
    		    {0.54, 0.69},//Beyblade
    		    {0.67, 0.69},//guantes
    		    {0.80, 0.69},//zapatillas

    		    {0.25, 0.86},//botines
    		    {0.40, 0.86},//guardapolvo
    		    {0.52, 0.86},//camiseta D&J
    		    {0.67, 0.86},//camiseta argentina 
    		    {0.82, 0.86}//egresados
    		};
        JButton btnSalir = new JButton("Salir del Kiosko");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 18));
        btnSalir.addActionListener(e -> ventana.verMenu());
        vista.add(btnSalir);

        vista.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int vw = vista.getWidth();
                int vh = vista.getHeight();
                if (vw == 0 || vh == 0) return;

                int[] r = rectModal(vw, vh);
                int mx = r[0], my = r[1], mw = r[2], mh = r[3];

                
            
                panelItems.setBounds(mx, my, mw, mh);
                for (int i = 0; i < slots.length; i++) {

                    int x = (int)(mw * posiciones[i][0]);
                    int y = (int)(mh * posiciones[i][1]);
                    
                    
                    slots[i].setBounds(x - 40, y - 40, 80, 80);
                }
                int bw = 200, bh = 40;
                btnSalir.setBounds((vw - bw) / 2, my + mh + 10, bw, bh);
                
                vista.repaint();
            }
        });

        return vista;
    }

    /** vista visual de un item dentro del kiosco */
    private ItemView crearSlotItem(Item item) {
        ItemView slot = new ItemView(item, 64, 64, true);
        if (item != null) {
            slot.setClickListener(i -> irADetalle(i));
        }
        return slot;
    }

   
    private JPanel crearVistaDetalle() {
        JPanel vista = new JPanel(new BorderLayout());
        vista.setOpaque(false);

        
        panelDetalleItem = new JPanel(new GridBagLayout());
        panelDetalleItem.setOpaque(false);
        vista.add(panelDetalleItem, BorderLayout.CENTER);

        btnComprar = new JButton("Comprar");
        btnComprar.setFont(new Font("Arial", Font.BOLD, 18));
        btnComprar.addActionListener(e -> ejecutarCompra());

        JButton btnCancelar = new JButton("Volver al Catálogo");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 18));
        btnCancelar.addActionListener(e -> irACatalogo());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        botones.setOpaque(false);
        botones.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        botones.add(btnComprar);
        botones.add(btnCancelar);
        vista.add(botones, BorderLayout.SOUTH);

        return vista;
    }

    
    private void irACatalogo() {
        itemSeleccionado = null;
        cardLayout.show(contenedor, VISTA_CATALOGO);
    }

    private void irADetalle(Item item) {
        itemSeleccionado = item;

        // Reemplazar el ItemView del detalle (no tiene setItem, así que se recrea)
        panelDetalleItem.removeAll();
        ItemView vistaGrande = new ItemView(item, 150, 150, true);
        panelDetalleItem.add(vistaGrande);
        panelDetalleItem.revalidate();
        panelDetalleItem.repaint();

        btnComprar.setText("Comprar (" + item.getValor() + " Pesos)");

        cardLayout.show(contenedor, VISTA_DETALLE);
    }


    private void ejecutarCompra() {
        if (itemSeleccionado == null) return;

        boolean exito = kioskoLogico.comprarItem(itemSeleccionado);

        if (exito) {
            lblPesos.setText("Pesos de la Party: $" + (Repositorio.getInstance().getPartidaActual().getPesos()));
            JOptionPane.showMessageDialog(
                this,
                "¡" + itemSeleccionado.getNombre() + " se agregó al inventario!"
            );
        } else {
            JOptionPane.showMessageDialog(
                this,
                "No tenés pesos suficientes para comprar " + itemSeleccionado.getNombre() + ".",
                "Pesos insuficientes",
                JOptionPane.WARNING_MESSAGE
            );
            return; // se queda en la vista de detalle
        }

        irACatalogo();
    }

    
    private Image cargarImagen(String ruta) {
        try {
            return new ImageIcon(ruta).getImage();
        } catch (Exception e) {
            System.err.println("No se pudo cargar imagen: " + ruta);
            return null;
        }
    }
}