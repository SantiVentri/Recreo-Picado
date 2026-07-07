package views;

import java.awt.Graphics;
import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import modelo.Entidad;
import orquestador.Orquestador;

public class CharacterOrderPanel extends JPanel {

    private Image fondo, mago, arquero, guerrero, curandera, jefa1, jefa2, secuaz1, jefe3, secuaz2, jefeFinal, secuaz3;
    
    public CharacterOrderPanel() {
        setOpaque(false);
        fondo = new ImageIcon(getClass().getResource("/resources/turnos/PanelDeOrdenDeTurnos.png")).getImage();
        mago = new ImageIcon(getClass().getResource("/resources/turnos/MagoPanel.png")).getImage();
        arquero = new ImageIcon(getClass().getResource("/resources/turnos/ArqueroPanel.png")).getImage();
        guerrero = new ImageIcon(getClass().getResource("/resources/turnos/GuerreroPanel.png")).getImage();
        curandera = new ImageIcon(getClass().getResource("/resources/turnos/CuranderaPanel.png")).getImage();
        jefa1 = new ImageIcon(getClass().getResource("/resources/turnos/JefaDelBañoPanel.png")).getImage();
        jefa2 = new ImageIcon(getClass().getResource("/resources/turnos/EnfermeraPanel.png")).getImage();
        secuaz1 = new ImageIcon(getClass().getResource("/resources/turnos/SecuazEnfermeraPanel.png")).getImage();
        jefe3 = new ImageIcon(getClass().getResource("/resources/turnos/ProfesorEDPanel.png")).getImage();
        secuaz2 = new ImageIcon(getClass().getResource("/resources/turnos/SecuazPatioPanel.png")).getImage();
        jefeFinal = new ImageIcon(getClass().getResource("/resources/turnos/DirectorPanel.png")).getImage();
        secuaz3 = new ImageIcon(getClass().getResource("/resources/turnos/SecuazNerdPanel.png")).getImage();
    }

    private Image obtenerImagen(Entidad entidad) {
        String nombre = entidad.getNombre();

     // Imágenes de panel
        switch (nombre.toLowerCase()) {
            case "mago": return mago;
            case "arquero": return arquero;
            case "guerrero": return guerrero;
            case "curandera": return curandera;
            case "jefa del baño": return jefa1;
            case "enfermera": return jefa2;
            case "secuaz enfermera": return secuaz1;
            case "profesor ed": return jefe3;
            case "secuaz patio": return secuaz2;
            case "director": return jefeFinal;
            case "secuaz nerd": return secuaz3;
            default: return null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);

        List<Entidad> orden = Orquestador.getInstance().getTurnosCompletos();
        if (orden == null || orden.isEmpty()) return;

        int x = 0;
        int y = 50;

        int cantidadAMostrar = Math.min(orden.size(), 4);
        for (int i = 0; i < cantidadAMostrar; i++) {
            Entidad entidad = orden.get(i);
            Image img = obtenerImagen(entidad);
            if (img == null) continue;

            int tamaño = (i == 0) ? 80 : 55;

            g.drawImage(img, x, y, tamaño + 60, tamaño, this);

            if(i == 0) {
                x += 70; // separación grande después del actual
            } else {
                x += 50; // separación normal
            }
        
        }
    }
}