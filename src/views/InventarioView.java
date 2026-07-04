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
    private static final int ESPACIADO = 8;
    
    private Entidad personaje;
    

    public InventarioView(Entidad personaje) {
    	 System.out.println("Se creó InventarioView");
        this.personaje = personaje;

        setOpaque(false);

        setLayout(new GridLayout (FILAS, COLUMNAS,ESPACIADO, ESPACIADO));

        cargarItems();
    }

    private void cargarItems() {

        removeAll();

        List<Item> inventario =
                Repositorio.getInstance()
                           .getPartidaActual()
                           .getInventario();
        System.out.println("InventarioView leyendo lista con " + inventario.size() + " items");
        for(Item item : inventario) {

        	ItemView vista = new ItemView(item,70,70,false);

        	// Si este personaje tiene equipado este objeto,
        	// dibuja el borde verde.
        	if(item.getEquipadoPor() == personaje) {
        	    vista.setSeleccionado(true);
        	}

        	vista.setClickListener(i -> seleccionarItem(i));

        	add(vista);
        }

        revalidate();
        repaint();
    }

    private void seleccionarItem(Item item) {

        if(item instanceof Arma) {

            Arma arma = (Arma)item;

            if(arma.getEquipadoPor() == null ||
               arma.getEquipadoPor() == personaje) {

                personaje.equiparArma(arma);
            }
        }

        if(item instanceof Armadura) {

            Armadura armadura = (Armadura)item;

            if(armadura.getEquipadoPor() == null ||
               armadura.getEquipadoPor() == personaje) {

                personaje.equiparArmadura(armadura);
            }
        }

        cargarItems();
    }
    
    public void actualizar() {
    	cargarItems();
    }

}
