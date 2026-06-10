package modelo;

import interfaces.IEntidad;

public class Mago extends Entidad {

    private int inteligencia;

    public Mago(String nombre, int vidaMax, int energiaMax, int velocidad,
                int ataque, int defensa, Habilidad habilidad, int inteligencia) {
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
    public void realizarDefensa() {
        this.setDefendiendo(true);
        this.setEnergia(this.getEnergiaMax());
    }

    /**
     * Usa la habilidad del Mago sobre el objetivo.
     * Consume energía según el costo definido en la Habilidad.
     * El daño se calcula con la potencia de la habilidad + bonus de inteligencia.
     * Si la habilidad tiene un Efecto asociado, también se lo aplica al objetivo.
     */
    @Override
    public void usarHabilidad(IEntidad objetivo) {
        Habilidad hab = this.getHabilidad();
        if (hab == null || !hab.sePuedeUsar(this)) return;

        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - hab.getCostoEnergia());

        int dano = hab.getPotencia() + inteligencia;
        objetivo.recibirDano(dano);

        if (hab.getEfecto() != null && objetivo instanceof Entidad) {
            objetivo.aplicarEfecto(hab.getEfecto().copiar());
        }
    }

    public int getInteligencia() { return inteligencia; }
    public void setInteligencia(int inteligencia) { this.inteligencia = inteligencia; }
}