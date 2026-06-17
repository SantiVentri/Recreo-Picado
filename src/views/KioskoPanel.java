package views;

import javax.swing.*;
import java.awt.*;

import main.VentanaLayout;
import modelo.Item;
import modelo.Kiosko;
import modelo.Equipo;
import modelo.Repositorio;

/**
 * KioskoPanel — flujo lineal por estados:
 *   CATALOGO → DETALLE_ITEM → (vuelve a CATALOGO)
 *
 * La compra impacta directo en el inventario general del Equipo
 * (no se asigna a un personaje puntual desde el kiosko).
 */
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

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // =========================================================================
    // HUD
    // =========================================================================
    private JPanel crearHUD() {
        JPanel hud = new JPanel(new FlowLayout(FlowLayout.CENTER));
        hud.setOpaque(false);
        hud.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        Equipo equipo = Repositorio.getInstance().getPartidaActual().getAlumnos();
        lblPesos = new JLabel("💰 Pesos de la Party: $" + equipo.getPesos());
        lblPesos.setFont(new Font("Arial", Font.BOLD, 26));
        lblPesos.setForeground(Color.WHITE);
        hud.add(lblPesos);

        return hud;
    }

    // =========================================================================
    // Rectángulo del modal dentro del panel vista (mx, my, mw, mh)
    // El modal ocupa 75% ancho x 80% alto, centrado
    // =========================================================================
    private int[] rectModal(int vw, int vh) {
        int mw = (int) (vw * 0.75);
        int mh = (int) (vh * 0.80);
        int mx = (vw - mw) / 2;
        int my = (vh - mh) / 2;
        return new int[]{mx, my, mw, mh};
    }

    // =========================================================================
    // VISTA 1 — CATÁLOGO
    // =========================================================================
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

        JPanel grilla = new JPanel(new GridLayout(5, 4, 8, 8));
        grilla.setOpaque(false);
        vista.add(grilla);

        for (int i = 0; i < 20; i++) {
            Item item = (i < todosLosItems.length) ? todosLosItems[i] : null;
            grilla.add(crearSlotItem(item));
        }

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

                // --- AJUSTAR ESTOS 4 VALORES para alinear la grilla con los estantes ---
                // Medir sobre la imagen del modal:
                //   gx: cuánto % desde el borde izquierdo del modal hasta el primer slot
                //   gy: cuánto % desde el borde superior del modal hasta el primer slot
                //   gw: cuánto % del ancho del modal ocupa la grilla
                //   gh: cuánto % del alto  del modal ocupa la grilla
                double pLeft   = 0.140;   // margen izquierdo dentro del modal
                double pTop    = 0.215;   // margen superior dentro del modal (debajo del letrero)
                double pWidth  = 0.720;   // ancho de la grilla relativo al modal
                double pHeight = 0.755;   // alto  de la grilla relativo al modal

                int gx = mx + (int) (mw * pLeft);
                int gy = my + (int) (mh * pTop);
                int gw = (int) (mw * pWidth);
                int gh = (int) (mh * pHeight);
                grilla.setBounds(gx, gy, gw, gh);

                int bw = 200, bh = 40;
                btnSalir.setBounds((vw - bw) / 2, my + mh + 10, bw, bh);

                vista.repaint();
            }
        });

        return vista;
    }

    /** Slot individual de la grilla, usando ItemView */
    private ItemView crearSlotItem(Item item) {
        ItemView slot = new ItemView(item, 64, 64, true);
        if (item != null) {
            slot.setClickListener(i -> irADetalle(i));
        }
        return slot;
    }

    // =========================================================================
    // VISTA 2 — DETALLE DEL ÍTEM
    // =========================================================================
    private JPanel crearVistaDetalle() {
        JPanel vista = new JPanel(new BorderLayout());
        vista.setOpaque(false);

        // Contenedor donde se inserta el ItemView grande del ítem actual.
        // Se reemplaza en cada irADetalle() porque ItemView no tiene setItem().
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

    // =========================================================================
    // NAVEGACIÓN ENTRE VISTAS
    // =========================================================================
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

    // =========================================================================
    // LÓGICA DE COMPRA
    // =========================================================================
    private void ejecutarCompra() {
        if (itemSeleccionado == null) return;

        Equipo equipo = Repositorio.getInstance().getPartidaActual().getAlumnos();
        boolean exito = kioskoLogico.comprarItem(itemSeleccionado, equipo);

        if (exito) {
            lblPesos.setText("Pesos de la Party: $" + equipo.getPesos());
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

    // =========================================================================
    // UTILIDADES
    // =========================================================================
    private Image cargarImagen(String ruta) {
        try {
            return new ImageIcon(ruta).getImage();
        } catch (Exception e) {
            System.err.println("No se pudo cargar imagen: " + ruta);
            return null;
        }
    }
}