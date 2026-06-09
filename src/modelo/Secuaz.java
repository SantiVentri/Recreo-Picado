package modelo;

import interfaces.IEntidad;

public class Secuaz extends Entidad {

    public Secuaz(String nombre, int vidaMax, int energiaMax, int velocidad,
                  int ataque, int defensa, Habilidad habilidad) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
    }

    @Override
    public void realizarAtaque(IEntidad objetivo) {
        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - 2);
        objetivo.recibirDano(this.getAtaque());
    }

    @Override
    public void realizarDefensa() {
        this.setDefendiendo(true);
        this.setEnergia(this.getEnergiaMax());
    }

    // Los secuazes no poseen habilidad
    @Override
    public void usarHabilidad(IEntidad objetivo) { }
}