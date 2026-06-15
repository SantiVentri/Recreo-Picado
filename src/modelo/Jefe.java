package modelo;

import interfaces.IEntidad;

/**
 * Enemigo intermedio. Tiene más stats que el Secuaz y puede recuperar vida al defenderse..
 */
public class Jefe extends Entidad {

    public Jefe(String nombre, int vidaMax, int energiaMax, int velocidad,
                int ataque, int defensa, Habilidad habilidad) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
    }

    @Override
    public void realizarAtaque(IEntidad objetivo) {
        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - 2);
        int dano = (int)(this.getAtaque() * 1.2);
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
        objetivo.recibirDano(hab.getPotencia());

        if (hab.getEfecto() != null && objetivo instanceof Entidad) {
            ((Entidad) objetivo).aplicarEfecto(hab.getEfecto().copiar());
        }
    }
    

}