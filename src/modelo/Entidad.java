package modelo;

import java.util.*;

import interfaces.IEntidad;

public abstract class Entidad implements IEntidad {
	// Atributos básicos
	private final String nombre;
	private int vida;
	private int vidaMax;
	private int energia;
	private int energiaMax;
	private int velocidad;
	private boolean defendiendo;
	
	// Atributos de batalla
	private int ataque;
	private int defensa;
	private Habilidad habilidad;

	// Atributos de items y efectos
	private List<Efecto> efectosActivos;
	private List<Item> itemsEquipados;
	
	public interface EntidadListener {
		void onAtacado();
        void onCurado();
        void onEnvenenado();
    }
	
	private transient EntidadListener listener;
	
	public Entidad(String nombre, int vidaMax, int energiaMax, int velocidad, int ataque, int defensa, Habilidad habilidad) {
		this.nombre = nombre;
		this.vidaMax = vidaMax;
		this.vida = vidaMax;
		this.energiaMax = energiaMax;
		this.energia = energiaMax;
		this.velocidad = velocidad;
		this.defendiendo = false;
		
		this.ataque = ataque;
		this.defensa = defensa;
		this.habilidad = habilidad;
		
		this.efectosActivos = new ArrayList<Efecto>();
		this.itemsEquipados = new ArrayList<Item>();
	}

	private int calcularDano(int daño) {
		int danoReal = Math.max(1, (int)(daño * (100.0 / (100 + this.defensa))));
		
		if (this.defendiendo == true) {
			danoReal = (int) (danoReal * 0.2);
		}
		
		return danoReal;
	}

	@Override
	public void recibirDano(int daño) {
		int danoReal = calcularDano(daño);
		this.quitarVida(danoReal);
	}

	@Override
	public boolean estaVivo() {
		return vida > 0;
	}

	@Override
	public void realizarAtaque(IEntidad objetivo) {
		this.defendiendo = false;
		this.energia -= 2;
		objetivo.recibirDano(this.ataque);
	}

	@Override
	public void realizarDefensa() {
		this.defendiendo = true;
		this.energia = this.energiaMax;
	}

	@Override
	public abstract void usarHabilidad(IEntidad objetivo);

	@Override
	public void usarItem(Item item) {
		item.usar(this);
	}
	
	@Override
	public void agregarItem(Item item) {
		if (this.itemsEquipados.size() < 3) {
			this.itemsEquipados.add(item);				
		} else {
			System.out.println("Error: Inventario de personaje lleno");
		}
	}

	@Override
	public void quitarItem(Item item) {
		if (this.itemsEquipados.contains(item)) {
			this.itemsEquipados.remove(item);
		}
	}
	
	@Override
	public void aplicarEfecto(Efecto efecto) {
		if (!this.efectosActivos.contains(efecto)) {
			this.efectosActivos.add(efecto);
		}
	}

	@Override
	public void quitarEfecto(Efecto efecto) {
		if (this.efectosActivos.contains(efecto)) {
			this.efectosActivos.remove(efecto);
		}
	}

	/**
	 * Procesa todos los efectos activos de esta entidad (veneno, curación, regeneración, etc.).
	 * Se debe llamar al comienzo del turno de la entidad, antes de que actúe.
	 * Los efectos que ya expiraron se eliminan de la lista.
	 */
	@Override
	public void procesarEfectos() {
		List<Efecto> aEliminar = new ArrayList<Efecto>();
		for (Efecto efecto : efectosActivos) {
			boolean sigueActivo = efecto.aplicarPorTurno(this);
			if (!sigueActivo) {
				aEliminar.add(efecto);
			}
		}
		efectosActivos.removeAll(aEliminar);
	}
	
	@Override
	public void quitarVida(int cantidad) {
		this.vida -= cantidad;
        
        if (this.listener != null) {
            this.listener.onAtacado();
        }
	}
	
	@Override
	public void aumentarVida(int cantidad) {
		this.vida = Math.min(this.vida + cantidad, this.vidaMax);
        
        if (this.listener != null) {
            this.listener.onCurado();
        }
	}
	
	@Override
	public void recibirDanoVeneno(int cantidad) {
        this.vida -= cantidad;
        
        if (this.listener != null) {
            this.listener.onEnvenenado();
        }
    }
	
	// Getters y setters
	public String getNombre() {
		return nombre;
	}

	public int getVida() {
		return vida;
	}

	public int getVidaMax() {
		return vidaMax;
	}

	public void setVidaMax(int vidaMax) {
		this.vidaMax = vidaMax;
	}

	public int getEnergia() {
		return energia;
	}

	public void setEnergia(int energia) {
		this.energia = energia;
	}

	public int getEnergiaMax() {
		return energiaMax;
	}

	public void setEnergiaMax(int energiaMax) {
		this.energiaMax = energiaMax;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}
	
	public boolean estaDefendiendo() {
		return defendiendo;
	}
	
	public void setDefendiendo(boolean estado) {
		this.defendiendo = estado;
	}

	public int getAtaque() {
		return ataque;
	}

	public void setAtaque(int ataque) {
		this.ataque = ataque;
	}

	public int getDefensa() {
		return defensa;
	}

	public void setDefensa(int defensa) {
		this.defensa = defensa;
	}
	
	public Habilidad getHabilidad() {
		return habilidad;
	}
	
	public List<Efecto> getEfectosActivos() {
	    return efectosActivos;
	}
	
	// Getters de valores visuales
	public int getAnchoSprite() { return 300; }
	public int getOffsetSombra() { return 52; }
	public int getAnchoSombra() { return 95; }
	public int getYOffsetSprite() { return 0; }
	
	// Setter de Listener
	public void setListener(EntidadListener listener) {
        this.listener = listener;
    }
}