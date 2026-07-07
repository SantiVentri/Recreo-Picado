package modelo;

import java.util.List;

import interfaces.IEntidad;

public class Arquero extends Alumno {
    private int punteria;

    public Arquero(String nombre, int vidaMax, int energiaMax, int velocidad, int ataque, int defensa, Habilidad habilidad, int punteria) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
        this.punteria = punteria;
    }

    @Override
    protected int calcularDanoAtaque() {
        return this.getAtaque() + (punteria / 3);
    }

    /**
     * Lluvia de flechas: potencia basada en la habilidad + puntería del arquero.
     * Si la habilidad tiene efecto (ej. sangrado), lo aplica al objetivo.
     */
    @Override
    public void usarHabilidad(IEntidad objetivo, List<Entidad> alumnos, List<Entidad> enemigos) {
        Habilidad hab = this.getHabilidad();
        if (hab == null || !hab.sePuedeUsar(this)) return;

        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - hab.getCostoEnergia());

        int dano = hab.getPotencia() + punteria;
        objetivo.recibirDano(dano);

        if (hab.getEfecto() != null && objetivo instanceof Entidad) {
            objetivo.aplicarEfecto(hab.getEfecto().copiar());
        }

        notificarUsandoHabilidad();
    }
    
    // Getters y setters
    public int getPunteria() {
    	return punteria;
    }
    
    public void setPunteria(int punteria) {
    	this.punteria = punteria;
    }
    
   
}