package modelo;

import enums.ESTADO_BATALLA;

public class Batalla {
	private static int contador = 0;
	
	private final int id;
	private Equipo enemigos;
	private String nombreArena;
	private ESTADO_BATALLA estado;
	
	public Batalla(Equipo enemigos, String nombreArena) {
		this.id = contador++;
		this.enemigos = enemigos;
		this.nombreArena = nombreArena;
		this.estado = ESTADO_BATALLA.BLOQUEADA;
	}
	
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
}
