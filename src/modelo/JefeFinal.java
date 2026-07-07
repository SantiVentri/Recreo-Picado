package modelo;

import enums.EFECTOS;
import interfaces.IEntidad;
import orquestador.Orquestador;

/**
 * Enemigo final. El más poderoso del juego.
 * Se diferencia del Jefe en que su ataque base es mayor
 * y su habilidad tiene el multiplicador de daño más alto.
 */
public class JefeFinal extends Entidad {

    public JefeFinal(String nombre, int vidaMax, int energiaMax, int velocidad, int ataque, int defensa, Habilidad habilidad) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
    }

    @Override
    protected int calcularDanoAtaque() {
        return (int)(this.getAtaque() * 2.0);
    }

    @Override
    public void usarHabilidad(IEntidad objetivo) {
        Habilidad hab = this.getHabilidad();
        if (hab == null || !hab.sePuedeUsar(this)) return;

        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - hab.getCostoEnergia());
        int dano = (int)(hab.getPotencia() * 2);

        if (hab.getEfecto() != null && hab.getEfecto().getTipo() == EFECTOS.ATAQUE_MULTIPLE) {
            for (Entidad alumno : Orquestador.getInstance().getAlumnos().getEntidades()) {
                if (alumno.estaVivo()) {
                    alumno.recibirDano(dano);
                }
            }
        } else {
            objetivo.recibirDano(dano);
            if (hab.getEfecto() != null && objetivo instanceof Entidad) {
                objetivo.aplicarEfecto(hab.getEfecto().copiar());
            }
        }

        notificarUsandoHabilidad();
    }
}