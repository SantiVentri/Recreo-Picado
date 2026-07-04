package modelo;

import java.util.ArrayList;
import java.util.List;

public class Equipo {
    private List<Entidad> entidades;
    private final int cantidadMaxima = 4;
    
    public Equipo() {
        this.entidades = new ArrayList<Entidad>();
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
    
    public Entidad getEntidadPorNombre(String nombre) {
        for (Entidad entidad : entidades) {
            if (entidad.getNombre().equalsIgnoreCase(nombre)) {
                return entidad;
            }
        }
        return null;
    }
}