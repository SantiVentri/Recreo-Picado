package modelo;

import interfaces.IEntidad;

public class Jefe extends Entidad {

    public Jefe(String nombre, int vidaMax, int energiaMax, int velocidad, int ataque, int defensa, Habilidad habilidad) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
    }

    @Override
    protected int calcularDanoAtaque() {
        return (int)(this.getAtaque() * 1.5);
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

        notificarUsandoHabilidad();
    }

    // Getters de valores visuales
    @Override
    public int getAnchoSprite() {
    	return 340;
    }

    @Override
    public int getOffsetSombra() {
    	return 58;
    }

    @Override
    public int getAnchoSombra() {
    	return 110;
    }
}