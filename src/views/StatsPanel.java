package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import modelo.Alumno;
import modelo.Entidad;

/**
 * Panel que muestra las estadísticas principales de un personaje:
 * Nivel, XP, VidaMax, EnergiaMax, Ataque y Defensa.
 * Pensado para ubicarse abajo a la izquierda en las pantallas de Jugador.
 */
public class StatsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Entidad personaje;

    public StatsPanel(Entidad personaje) {
        this.personaje = personaje;
        setOpaque(false);
    }

    public void actualizar() {
        repaint();
    }

    private List<String> construirLineas() {
        List<String> lineas = new ArrayList<>();

        if (personaje instanceof Alumno) {
            Alumno alumno = (Alumno) personaje;
            lineas.add("Nivel: " + alumno.getNivel());
            lineas.add("XP: " + alumno.getXp() + "/" + alumno.getXpPorNivel());
        }

        lineas.add("Vida Max: " + personaje.getVidaMax());
        lineas.add("Energia Max: " + personaje.getEnergiaMax());
        lineas.add("Ataque: " + personaje.getAtaque());
        lineas.add("Defensa: " + personaje.getDefensa());
        lineas.add("Velocidad: " + personaje.getVelocidad());

        return lineas;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (personaje == null) return;

        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();

        g2D.setColor(new Color(0, 0, 0, 160));
        g2D.fillRoundRect(0, 0, ancho, alto, 16, 16);
        g2D.setColor(new Color(255, 255, 255, 180));
        g2D.drawRoundRect(0, 0, ancho - 1, alto - 1, 16, 16);

        int padding = 12;
        int y = padding + 14;

        g2D.setFont(new Font("Arial", Font.BOLD, 16));
        g2D.setColor(Color.WHITE);
        g2D.drawString(personaje.getNombre(), padding, y);

        y += 8;
        g2D.setColor(new Color(255, 255, 255, 100));
        g2D.drawLine(padding, y, ancho - padding, y);

        g2D.setFont(new Font("Arial", Font.PLAIN, 14));
        g2D.setColor(Color.WHITE);

        int alturaLinea = 20;
        for (String linea : construirLineas()) {
            y += alturaLinea;
            g2D.drawString(linea, padding, y);
        }
    }
}