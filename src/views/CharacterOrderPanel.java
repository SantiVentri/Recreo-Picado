package views;

import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import modelo.Entidad;
import orquestador.Orquestador;

public class CharacterOrderPanel extends JPanel {

    private Image fondo;

    private Image mago;
    private Image arquero;
    private Image guerrero;
    private Image curandera;

    public CharacterOrderPanel() {

        setOpaque(false);

        fondo = new ImageIcon(
                getClass().getResource("/resources/PanelDeOrdenDeTurnos.png"))
                .getImage();

        mago = new ImageIcon(
                getClass().getResource("/resources/MagoPanel.png"))
                .getImage();

        arquero = new ImageIcon(
                getClass().getResource("/resources/ArqueroPanel.png"))
                .getImage();

        guerrero = new ImageIcon(
                getClass().getResource("/resources/GuerreroPanel.png"))
                .getImage();

        curandera = new ImageIcon(
                getClass().getResource("/resources/CuranderaPanel.png"))
                .getImage();
    }

    private Image obtenerImagen(Entidad entidad) {

        String nombre = entidad.getNombre();

        if (nombre.equalsIgnoreCase("Mago"))
            return mago;

        if (nombre.equalsIgnoreCase("Arquero"))
            return arquero;

        if (nombre.equalsIgnoreCase("Guerrero"))
            return guerrero;

        if (nombre.equalsIgnoreCase("Curandera"))
            return curandera;

        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);

        List<Entidad> orden =
                Orquestador.getInstance().getTurnos();

        if (orden.isEmpty())
            return;

        

        int x = 0;
        int y = 50;

        int actualIndex = Orquestador.getInstance().getTurnoActual();       

        for (int i = 0; i < orden.size(); i++) {

            int indice = (actualIndex + i) % orden.size();

            Entidad entidad = orden.get(indice);

            Image img = obtenerImagen(entidad);

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