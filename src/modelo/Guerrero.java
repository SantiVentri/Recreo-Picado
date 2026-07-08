package modelo;

import java.util.List;

import enums.EFECTOS;
import interfaces.IEntidad;

public class Guerrero extends Alumno {
    private int fuerza;

    public Guerrero(String nombre, int vidaMax, int energiaMax, int velocidad, int ataque, int defensa, Habilidad habilidad, int fuerza) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
        this.fuerza = fuerza;
    }

    @Override
    protected int calcularDanoAtaque() {
        return this.getAtaque() + (fuerza / 2);
    }

    /*
     * La habilidad del guerrero golpea a todos los enemigos vivos
     */
    @Override
    public void usarHabilidad(IEntidad objetivo, List<Entidad> alumnos, List<Entidad> enemigos) {
        if (this.getHabilidad() == null || !this.getHabilidad().sePuedeUsar(this)) return;
        this.setEnergia(this.getEnergia() - this.getHabilidad().getCostoEnergia());
        int dano = this.getHabilidad().getPotencia() + fuerza;
        if (this.getHabilidad().getEfecto() != null && this.getHabilidad().getEfecto().getTipo() == EFECTOS.ATAQUE_MULTIPLE) {
            for (int i = 0; i < enemigos.size(); i++) {
                if (enemigos.get(i).estaVivo()) {
                    enemigos.get(i).recibirDano(dano);
                }
            }
        } else {
            objetivo.recibirDano(dano);
            if (this.getHabilidad().getEfecto() != null && objetivo instanceof Entidad) {
                objetivo.aplicarEfecto(this.getHabilidad().getEfecto().copiar());
            }
        }

        notificarUsandoHabilidad();
    }

    // Getters y setters
    public int getFuerza() {
    	return fuerza;
    }
    
    public void setFuerza(int fuerza) {
    	this.fuerza = fuerza;
    }
}