package modelo;

import java.util.List;

import enums.EFECTOS;
import interfaces.IEntidad;
import orquestador.Orquestador;

public class Mago extends Alumno {
    private int inteligencia;

    public Mago(String nombre, int vidaMax, int energiaMax, int velocidad, int ataque, int defensa, Habilidad habilidad, int inteligencia) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
        this.inteligencia = inteligencia;
    }

    @Override
    public void realizarAtaque(IEntidad objetivo) {
        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - 2);
        int dano = this.getAtaque() + (inteligencia / 2);
        objetivo.recibirDano(dano);
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

    // Getters y setters
    public int getInteligencia() {
    	return inteligencia;
    }
    
    public void setInteligencia(int inteligencia) {
    	this.inteligencia = inteligencia;
    }
    
	
}