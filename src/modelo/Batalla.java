package modelo;

import java.io.Serializable;

import enums.ESTADO_BATALLA;

public class Batalla implements Serializable {
	private static final long serialVersionUID = 1L;

	private static int contador = 0;
	
	private final int id;
	private Equipo enemigos;
	private String nombreArena;
	private ESTADO_BATALLA estado;
	private Recompensa recompensa;
	
	public Batalla(Equipo enemigos, String nombreArena, Recompensa recompensa) {
		this.id = contador++;
		this.enemigos = enemigos;
		this.nombreArena = nombreArena;
		this.estado = ESTADO_BATALLA.BLOQUEADA;
		this.recompensa = recompensa;
	}
	
	// Getters y setters
	public int getId() {
		return id;
	}
	
	public Equipo getEnemigos() {
		return enemigos;
	}

	public ESTADO_BATALLA getEstado() {
		return estado;
	}

	public void setEstado(ESTADO_BATALLA estado) {
		this.estado = estado;
	}

	public String getNombreArena() {
		return nombreArena;
	}
	
	public Recompensa getRecompensa() {
		return recompensa;
	}
}
