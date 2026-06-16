
package modelo;

import java.util.ArrayList;
import java.util.List;

public class Equipo {
    private List<Entidad> entidades;
    private List<Item> inventario; 
    private int pesos;             
    private final int cantidadMaxima = 4;
    
    public Equipo() {
        this.entidades = new ArrayList<Entidad>();
        this.inventario = new ArrayList<Item>();
        this.pesos = 200; // Valor de pesos inicial
    }

    public List<Entidad> getEntidades() {
        return entidades;
    }

    public void agregarEntidad(Entidad entidad) {
        if (entidades.size() < cantidadMaxima) {
            entidades.add(entidad);
        }
    }
    
    public void quitarEntidad(Entidad entidad) {
        entidades.remove(entidad);
    }

    // --- MÉTODOS DE INVENTARIO Y PESOS ---

    public int getPesos() {
        return pesos;
    }

    public void sumarPesos(int cantidad) {
        this.pesos += cantidad;
    }

    public void restarPesos(int cantidad) {
        this.pesos -= cantidad;
    }

    public void agregarItem(Item item) {
        this.inventario.add(item);
    }

    public List<Item> getInventario() {
        return inventario;
    }

    public void asignarItemAEntidad(Item item, Entidad personaje) {
        this.inventario.add(item); 
        personaje.agregarItem(item);
    }
}