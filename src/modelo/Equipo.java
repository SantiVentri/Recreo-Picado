package modelo;

import java.util.ArrayList;
import java.util.List;

public class Equipo {
	private List<Entidad> entidades;
	private int pesos;
	private final int cantidadMaxima = 3;
	
	public Equipo() {
		entidades = new ArrayList<Entidad>();
		pesos = 0;
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

	public int getPesos() {
		return pesos;
	}
	
	public void recibirPesos(int cantidad) {
		this.pesos += cantidad;
	}
	
	public boolean quitarPesos(int cantidad) {
		if (cantidad > 0 && this.pesos >= cantidad) {
			this.pesos -= cantidad;
			return true; // Compra exitosa
		}
		
		return false; // No hay suficientes pesos
	}
}
