package views;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import modelo.Item;
import modelo.Pocion;
import modelo.Repositorio;

/**
 * Panel simple que muestra, sobre el mismo fondo de inventario usado en
 * Jugador1/2/3/4, únicamente las pociones disponibles en la partida actual.
 * Pensado para usarse como popup dentro de la batalla al presionar "Usar Item".
 */
public class InventarioBatallaView extends JPanel {

    public interface ClickListener {
        void onPocionSeleccionada(Pocion pocion);
    }

    private static final int COLUMNAS = 4;
    private static final int MAX_ITEMS = 20;
    private static final int TAMANIO_CELDA = 46;
    private static final int ESPACIADO_X = 18;
    private static final int ESPACIADO_Y = 12;

    // Posición de la grilla de estantes dentro de la imagen del locker
    // (mismas coordenadas que usa InventarioView, escaladas al tamaño del popup)
    private static final int GRID_OFFSET_X = 108;
    private static final int GRID_OFFSET_Y = 75;

    private Image imagenFondo;
    private ClickListener clickListener;

    public InventarioBatallaView() {
        try {
            imagenFondo = new ImageIcon("src/resources/inventario.png").getImage();
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen de fondo del inventario");
        }
        setOpaque(false);
        setLayout(null);
        actualizar();
    }

    public void setClickListener(ClickListener listener) {
        this.clickListener = listener;
    }

    public void actualizar() {
        removeAll();

        List<Item> inventario = Repositorio.getInstance().getPartidaActual().getInventario();
        List<Pocion> pociones = new ArrayList<>();
        for (Item item : inventario) {
            if (item instanceof Pocion) {
                pociones.add((Pocion) item);
            }
        }

        int indice = 0;
        for (Pocion pocion : pociones) {
        	if (indice >= MAX_ITEMS) break;
            ItemView vista = new ItemView(pocion, TAMANIO_CELDA, TAMANIO_CELDA, false);
            vista.setClickListener(item -> {
                if (clickListener != null) clickListener.onPocionSeleccionada((Pocion) item);
            });

            int fila = indice / COLUMNAS;
            int columna = indice % COLUMNAS;
            int x = GRID_OFFSET_X + columna * (TAMANIO_CELDA + ESPACIADO_X);
            int y = GRID_OFFSET_Y + fila * (TAMANIO_CELDA + ESPACIADO_Y);
            vista.setBounds(x, y, TAMANIO_CELDA, TAMANIO_CELDA);

            add(vista);
            indice++;
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
}