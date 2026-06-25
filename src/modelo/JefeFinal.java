package modelo;

import interfaces.IEntidad;

/**
 * Enemigo final. El más poderoso del juego.
 * Se diferencia del Jefe en que su ataque base es mayor
 * y su habilidad tiene el multiplicador de daño más alto.
 */
public class JefeFinal extends Entidad {

    public JefeFinal(String nombre, int vidaMax, int energiaMax, int velocidad,
                     int ataque, int defensa, Habilidad habilidad) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
    }

    @Override
    public void realizarAtaque(IEntidad objetivo) {
        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - 2);
        int dano = (int)(this.getAtaque() * 2.0);
        objetivo.recibirDano(dano);
    }

    @Override
    public void usarHabilidad(IEntidad objetivo) {
        Habilidad hab = this.getHabilidad();
        if (hab == null || !hab.sePuedeUsar(this)) return;

        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - hab.getCostoEnergia());
        int dano = (int)(hab.getPotencia() * 2);
        objetivo.recibirDano(dano);

        if (hab.getEfecto() != null && objetivo instanceof Entidad) {
            objetivo.aplicarEfecto(hab.getEfecto().copiar());
        }
    }
}