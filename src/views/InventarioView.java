package views;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;

import modelo.Arma;
import modelo.Armadura;
import modelo.Entidad;
import modelo.Item;
import modelo.Repositorio;

public class InventarioView extends JPanel {

	
	private static final int COLUMNAS = 4;
    private static final int FILAS = 5;
    private static final int TAMANIO_CELDA = 65; // celda cuadrada (ancho = alto)
    private static final int ESPACIADO_X = 3;
    private static final int ESPACIADO_Y = 2; 
    
    private Entidad personaje;
    

    public InventarioView(Entidad personaje) {
        this.personaje = personaje;

        setOpaque(false);

        setLayout(null);

        cargarItems();
    }

    private void cargarItems() {

        removeAll();

        List<Item> inventario =
                Repositorio.getInstance()
                           .getPartidaActual()
                           .getInventario();
        
        int indice = 0;
        for(Item item : inventario) {

        	ItemView vista = new ItemView(item ,TAMANIO_CELDA, TAMANIO_CELDA,false);

        	// Si este personaje tiene equipado este objeto,
        	// dibuja el borde verde.
        	if(item.getEquipadoPor() == personaje) {
        	    vista.setSeleccionado(true);
        	}

        	vista.setClickListener(i -> seleccionarItem(i));
        	int fila = indice / COLUMNAS;
        	int columna = indice % COLUMNAS;
        	int x = columna * (TAMANIO_CELDA + ESPACIADO_X);
        	int y = fila * (TAMANIO_CELDA + ESPACIADO_Y);
        	vista.setBounds(x, y, TAMANIO_CELDA, TAMANIO_CELDA);

        	add(vista);
        	indice++;
        }

        revalidate();
        repaint();
    }

    private void seleccionarItem(Item item) {

    	if(item instanceof Arma) {

            Arma arma = (Arma)item;

            if(arma.getEquipadoPor() == personaje) {
                // Ya la tenía puesta este personaje: al tocarla de nuevo, se la saca.
                personaje.desequiparArma();

            } else if(arma.getEquipadoPor() == null) {

                personaje.equiparArma(arma);
            }
        }

    	if(item instanceof Armadura) {

            Armadura armadura = (Armadura)item;

            if(armadura.getEquipadoPor() == personaje) {
                // Ya la tenía puesta este personaje: al tocarla de nuevo, se la saca.
                personaje.desequiparArmadura();

            } else if(armadura.getEquipadoPor() == null) {

                personaje.equiparArmadura(armadura);
            }
        }
        cargarItems();
    }
    
    public void actualizar() {
    	cargarItems();
    }

}
