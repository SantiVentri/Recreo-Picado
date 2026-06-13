package modelo;

import interfaces.IEntidad;

public class Arquero extends Entidad {
	// Atributos de nivel
	private int nivel;
	private int xp;

    private int punteria;

    public Arquero(String nombre, int vidaMax, int energiaMax, int velocidad,
                   int ataque, int defensa, Habilidad habilidad, int punteria) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
        this.punteria = punteria;
		this.nivel = 1;
		this.xp = 0;
    }

    @Override
    public void realizarAtaque(IEntidad objetivo) {
        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - 2);
        int dano = this.getAtaque() + (punteria / 3);
        objetivo.recibirDano(dano);
    }

    @Override
    public void realizarDefensa() {
        this.setDefendiendo(true);
        this.setEnergia(this.getEnergiaMax());
    }

    /**
     * Lluvia de flechas: potencia basada en la habilidad + puntería del arquero.
     * Si la habilidad tiene efecto (ej. sangrado), lo aplica al objetivo.
     */
    @Override
    public void usarHabilidad(IEntidad objetivo) {
        Habilidad hab = this.getHabilidad();
        if (hab == null || !hab.sePuedeUsar(this)) return;

        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - hab.getCostoEnergia());

        int dano = hab.getPotencia() + punteria;
        objetivo.recibirDano(dano);

        if (hab.getEfecto() != null && objetivo instanceof Entidad) {
            objetivo.aplicarEfecto(hab.getEfecto().copiar());
        }
    }
    
	private void subirNivel() {
	    this.nivel++;
	    this.setAtaque(getAtaque() + 3);
	    this.setDefensa(getDefensa() + 2);;
	    this.setVidaMax(getVidaMax() + 10);;
	}
    
	public void recibirXp(int xp) {
		this.xp += xp;
		
		while (this.xp >= 100) {
			this.xp -= 100;
			subirNivel(); 
		}
	}
	
	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getXp() {
		return xp;
	}

    public int getPunteria() { return punteria; }
    public void setPunteria(int punteria) { this.punteria = punteria; }
}