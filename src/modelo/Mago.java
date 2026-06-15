package modelo;

import java.util.List;

import enums.EFECTOS;
import interfaces.IEntidad;
import orquestador.Orquestador;

public class Mago extends Entidad {
	// Atributos de nivel
	private int nivel;
	private int xp;

    private int inteligencia;

    public Mago(String nombre, int vidaMax, int energiaMax, int velocidad,
                int ataque, int defensa, Habilidad habilidad, int inteligencia) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
        this.inteligencia = inteligencia;
		this.nivel = 1;
		this.xp = 0;
    }

    @Override
    public void realizarAtaque(IEntidad objetivo) {
        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - 2);
        int dano = this.getAtaque() + (inteligencia / 2);
        objetivo.recibirDano(dano);
    }

    @Override
    public void realizarDefensa() {
        this.setDefendiendo(true);
        this.setEnergia(this.getEnergiaMax());
    }

    @Override
    public void usarHabilidad(IEntidad objetivo) {
        Habilidad hab = this.getHabilidad();
        if (hab == null || !hab.sePuedeUsar(this)) return;

        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - hab.getCostoEnergia());

        int dano = hab.getPotencia() + inteligencia;

        if (hab.getEfecto() != null && hab.getEfecto().getTipo() == EFECTOS.ATAQUE_MULTIPLE) {
            List<Entidad> enemigos = Orquestador.getInstance().getBatalla().getEnemigos().getEntidades();
            for (int i = 0; i < enemigos.size(); i++) {
                if (enemigos.get(i).estaVivo()) {
                    enemigos.get(i).recibirDano(dano);
                }
            }
        } else {
            objetivo.recibirDano(dano);
            if (hab.getEfecto() != null && objetivo instanceof Entidad) {
                objetivo.aplicarEfecto(hab.getEfecto().copiar());
            }
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

    public int getInteligencia() { return inteligencia; }
    public void setInteligencia(int inteligencia) { this.inteligencia = inteligencia; }
    
	
}