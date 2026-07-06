package modelo;

import enums.EFECTOS;

public abstract class Alumno extends Entidad {

    private static final int XP_POR_NIVEL = 100;

    private int xp;
    private int nivel;
    private Arma armaEquipada;
    private Armadura armaduraEquipada;

    public Alumno(String nombre, int vidaMax, int energiaMax, int velocidad, int ataque, int defensa, Habilidad habilidad) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
        this.nivel = 1;
        this.xp = 0;
        this.armaEquipada = null;
        this.armaduraEquipada = null;
    }

    /**
     * Otorga XP al alumno y sube de nivel tantas veces como corresponda.
     */
    public void recibirXp(int cantidad) {
        this.xp += cantidad;
        while (this.xp >= XP_POR_NIVEL) {
            this.xp -= XP_POR_NIVEL;
            subirNivel();
        }
    }

    protected void subirNivel() {
        this.nivel++;
        this.setAtaque(getAtaque() + 3);
        this.setDefensa(getDefensa() + 2);
        this.setVidaMax(getVidaMax() + 10);
    }
    
    // Items (Pociones, Armas y Armaduras)
	public void usarItem(Pocion pocion) {
		if (pocion.getEfecto() == EFECTOS.CURACION) {
			super.aumentarVida(pocion.getCantidadCuracion());;
		} else if (pocion.getEfecto() == EFECTOS.ENERGIA) {
			super.aumentarEnergia(pocion.getCantidadEnergia());
		}
	}
	
	public void equiparArma(Arma arma) {

		// Si ya tiene un arma equipada, la libera y revierte su bonus
		if (this.armaEquipada != null) {
			desequiparArma();
		}
		// Equipa el arma nueva y aplica su bonus de ataque
		this.armaEquipada = arma;
		if (arma != null) {
			arma.setEquipadoPor(this);
			super.setAtaque(super.getAtaque() + arma.getDanioBase());
		}
	}


	public void desequiparArma() {

		if (this.armaEquipada != null) {
			super.setAtaque(super.getAtaque() - armaEquipada.getDanioBase());
			this.armaEquipada.setEquipadoPor(null);
		}
		this.armaEquipada = null;
	}
	
	public void equiparArmadura(Armadura armadura) {

		// Si ya tiene una armadura equipada, la libera y revierte su bonus
		if (this.armaduraEquipada != null) {
			desequiparArmadura();
		}

		this.armaduraEquipada = armadura;

		if (armadura != null) {
			armadura.setEquipadoPor(this);
			super.setVidaMax(super.getVidaMax() + armadura.getVidaBonus());
			super.setDefensa(super.getDefensa() + armadura.getDefensaBonus());
			super.setVelocidad(super.getVelocidad() + armaduraEquipada.getVelocidadBonus());
		}
	}

	public void desequiparArmadura() {

		if (this.armaduraEquipada != null) {
			super.setVidaMax(super.getVidaMax() - armaduraEquipada.getVidaBonus());
			super.setDefensa(super.getDefensa() - armaduraEquipada.getDefensaBonus());
			super.setVelocidad(super.getVelocidad() - armaduraEquipada.getVelocidadBonus());
			this.armaduraEquipada.setEquipadoPor(null);
		}
		this.armaduraEquipada = null;
	}

    // Getters y setters
    public int getXp() {
    	return xp;
    }
    
    public int getXpPorNivel() {
    	return XP_POR_NIVEL;
    }
    
    public int getNivel() {
    	return nivel;
    }
    
    public void setNivel(int nivel) {
    	this.nivel = nivel;
    }
    
	public Arma getArmaEquipada() {
		return armaEquipada;
	}
	
	public Armadura getArmaduraEquipada() {
		return armaduraEquipada;
	}
}