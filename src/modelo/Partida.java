package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import enums.ESTADO_BATALLA;
import modelo.factories.BatallaFactory;
import modelo.factories.PersonajeFactory;

public class Partida implements Serializable {
	private static final long serialVersionUID = 1L;

	private static int contador = 0;
	
	private final int id;
	private int pesos;
	private Equipo alumnos;
	private List<Item> inventario;
	private List<Batalla> batallas;
	
	public Partida() {
		this.id = contador++;
		this.pesos = 200;
		this.inventario = new ArrayList<Item>();
		
		this.alumnos = new Equipo();
		cargarEquipo();
		
		this.batallas = new ArrayList<Batalla>();
		cargarBatallas();
	}
	
	public int getId() {
		return id;
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
	
	public List<Item> getInventario() {
		return inventario;
	}
	
	public void agregarItem(Item item) {
		this.inventario.add(item);
	}
	
	public void quitarItem(Item item) {
		this.inventario.remove(item);
	}
	
	public Equipo getAlumnos() {
		return alumnos;
	}
	
	public void cargarEquipo() {
		alumnos.agregarEntidad(PersonajeFactory.crearMago());
		alumnos.agregarEntidad(PersonajeFactory.crearArquero());
		alumnos.agregarEntidad(PersonajeFactory.crearCurandera());
		alumnos.agregarEntidad(PersonajeFactory.crearGuerrero());
	}

	public void resetearEquipo() {
		this.alumnos = new Equipo();
		cargarEquipo();
	}

	public void cargarBatallas() {
		batallas.add(BatallaFactory.crearNivel1());
		batallas.add(BatallaFactory.crearNivel2());
		batallas.add(BatallaFactory.crearNivel3());
		batallas.add(BatallaFactory.crearNivel4());
	}
	
	public List<Batalla> getBatallas() {
		return batallas;
	}
	
	public Batalla getProximaBatalla() {
		for (Batalla b : batallas) {
			if (b.getEstado() == ESTADO_BATALLA.PENDIENTE) {
				return b;
			}
		}
		
		return null;
	}
	
	public void desbloquearProximaBatalla(Batalla batallaJugada) {
		batallaJugada.setEstado(ESTADO_BATALLA.VICTORIA);
		
		for (Batalla b : batallas) {
			if (b.getEstado() == ESTADO_BATALLA.BLOQUEADA) {
				b.setEstado(ESTADO_BATALLA.PENDIENTE);
				return;
			}
		}
	}
	
	/**
	 * Devuelve el nivel en el que se encuentra el jugador dentro de esta partida,
	 * es decir, la cantidad de batallas ya ganadas + 1 (o la última si ya las ganó todas).
	 * Se usa para mostrar el progreso en la pantalla de "Cargar partida".
	 */
	public int getNivelActual() {
		int ganadas = 0;
		for (Batalla b : batallas) {
			if (b.getEstado() == ESTADO_BATALLA.VICTORIA) {
				ganadas++;
			}
		}
		
		return Math.min(ganadas + 1, batallas.size());
	}
}