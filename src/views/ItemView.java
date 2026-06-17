package views;

import javax.swing.JPanel;

import modelo.Item;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Panel reutilizable para mostrar un Item: ícono + (opcionalmente) nombre y precio.
 * Mismo patrón que EntidadView: encapsula carga de imagen, pintado y click,
 * para no repetir esa lógica en cada pantalla que necesite mostrar un ítem.
 */
public class ItemView extends JPanel {

    public interface ClickListener {
        void onClick(Item item);
    }

    private final Item item;
    private final boolean mostrarInfo;   // si se dibuja nombre + precio debajo del ícono
    private final int anchoIcono;
    private final int altoIcono;

    private BufferedImage imagenItem;
    private ClickListener clickListener;

    // --- Constructor completo ---
    public ItemView(Item item, int anchoIcono, int altoIcono, boolean mostrarInfo) {
        this.item = item;
        this.anchoIcono = anchoIcono;
        this.altoIcono = altoIcono;
        this.mostrarInfo = mostrarInfo;

        this.setOpaque(false);
        this.setPreferredSize(new Dimension(anchoIcono + 20, altoIcono + (mostrarInfo ? 34 : 6)));

        cargarImagen();

        if (item != null) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (clickListener != null) clickListener.onClick(item);
                }
            });
        }
    }

    // --- Con info por defecto (true) ---
    public ItemView(Item item, int anchoIcono, int altoIcono) {
        this(item, anchoIcono, altoIcono, true);
    }

    // --- Tamaño por defecto 64x64, con info ---
    public ItemView(Item item) {
        this(item, 94, 94, true);
    }

    private void cargarImagen() {
        if (item == null) return;
        try {
            imagenItem = ImageIO.read(new File(item.getRutaImagen()));
        } catch (IOException e) {
            System.err.println("No se pudo cargar la imagen del ítem: " + item.getRutaImagen());
            imagenItem = null;
        }
    }

    public void setClickListener(ClickListener listener) {
        this.clickListener = listener;
    }

    public Item getItem() {
        return item;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (item == null) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int anchoDisponible = getWidth();
        int altoDisponible  = getHeight();
        int centroX = anchoDisponible / 2;

        // Reservar espacio para el texto (si corresponde) y centrar el ícono
        // en lo que queda, en vez de usar coordenadas fijas. Así el ItemView
        // se adapta al tamaño real que le da su contenedor (p. ej. un GridLayout).
        // Espacio fijo para 2 líneas de texto (nombre + precio), independiente
        // del tamaño del ícono — así un ItemView grande (detalle) no reserva
        // de más y uno chico (catálogo) no se queda corto.
        int altoTexto = mostrarInfo ? 30 : 0;
        int altoParaIcono = altoDisponible - altoTexto;

        // El ícono se dibuja cuadrado. Usa el tamaño pedido como referencia, pero
        // puede crecer si la celda real le da más espacio (hasta 1.3x) o achicarse
        // si el contenedor le da menos, para no recortar contra los bordes.
        int topeIcono = (int) (anchoIcono * 1.3);
        int ladoIcono = Math.min(topeIcono, Math.min(anchoDisponible - 8, altoParaIcono - 4));
        if (ladoIcono < 0) ladoIcono = 0;

        int xIcono = centroX - (ladoIcono / 2);
        int yIcono = Math.max(2, (altoParaIcono - ladoIcono) / 2);

        // --- ÍCONO ---
        if (imagenItem != null) {
            g2.drawImage(imagenItem, xIcono, yIcono, ladoIcono, ladoIcono, this);
        } else {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(xIcono, yIcono, ladoIcono, ladoIcono);
            g2.setColor(Color.WHITE);
            g2.drawString("?", centroX - 4, yIcono + ladoIcono / 2);
        }

        // --- NOMBRE + PRECIO (opcional) ---
        if (mostrarInfo) {
            int yNombre = altoParaIcono + (int) (altoTexto * 0.45);
            int yPrecio = altoParaIcono + (int) (altoTexto * 0.85);

            int tamanioFuente = Math.max(10, Math.min(13, anchoDisponible / 9));

            g2.setFont(new Font("Arial", Font.PLAIN, tamanioFuente));
            FontMetrics fmNombre = g2.getFontMetrics();
            String nombre = item.getNombre();
            int xNombre = centroX - fmNombre.stringWidth(nombre) / 2;
            dibujarTextoConContorno(g2, nombre, xNombre, yNombre, Color.WHITE);

            g2.setFont(new Font("Arial", Font.BOLD, tamanioFuente));
            String precio = "$" + item.getValor();
            FontMetrics fmPrecio = g2.getFontMetrics();
            int xPrecio = centroX - fmPrecio.stringWidth(precio) / 2;
            dibujarTextoConContorno(g2, precio, xPrecio, yPrecio, new Color(255, 215, 0));
        }
        

        g2.dispose();
        
    }

    /** Dibuja texto con un leve contorno oscuro para que se lea sobre cualquier fondo. */
    private void dibujarTextoConContorno(Graphics2D g2, String texto, int x, int y, Color colorTexto) {
        g2.setColor(new Color(0, 0, 0, 200));
        g2.drawString(texto, x - 1, y);
        g2.drawString(texto, x + 1, y);
        g2.drawString(texto, x, y - 1);
        g2.drawString(texto, x, y + 1);

        g2.setColor(colorTexto);
        g2.drawString(texto, x, y);
    }
}