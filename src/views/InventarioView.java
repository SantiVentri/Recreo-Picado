package views;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JPanel;

import modelo.Arma;
import modelo.Armadura;
import modelo.Entidad;
import modelo.Item;
import modelo.Repositorio;

public class InventarioView extends JPanel {

    private Entidad personaje;

    public InventarioView(Entidad personaje) {
    	 System.out.println("Se creó InventarioView");
        this.personaje = personaje;

        setOpaque(false);

        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        cargarItems();
    }

    private void cargarItems() {

        removeAll();

        List<Item> inventario =
                Repositorio.getInstance()
                           .getPartidaActual()
                           .getInventario();

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
