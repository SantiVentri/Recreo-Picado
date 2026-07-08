package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recompensa implements Serializable {
	private static final long serialVersionUID = 1L;

	private int oro;
	private int xp;
	private boolean reclamada;
	
	// Items
	private Pocion pocion;
	private Arma arma;
	private Armadura armadura;
	
	public Recompensa(int oro, int xp, Pocion pocion, Arma arma, Armadura armadura) {
		this.oro = oro;
		this.xp = xp;
		this.reclamada = false;
		
		this.pocion = pocion;
		this.arma = arma;
		this.armadura = armadura;
	}

	// Getters y setter
	public int getOro() {
		return oro;
	}

	public int getXp() {
		return xp;
	}

	public boolean estaReclamada() {
		return reclamada;		
	}
	
	public void setReclamada() {
		this.reclamada = true;		
	}
	
	public List<Item> getItems() {
		List<Item> items = new ArrayList<>();
		items.add(pocion);
		items.add(arma);
		items.add(armadura);
		return items;
	}
}
