package modelo;

import java.util.List;

import enums.EFECTOS;
import interfaces.IEntidad;
import orquestador.Orquestador;

public class Guerrero extends Entidad {

    private int fuerza;

    public Guerrero(String nombre, int vidaMax, int energiaMax, int velocidad,
                    int ataque, int defensa, Habilidad habilidad, int fuerza) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
        this.fuerza = fuerza;
    }

    @Override
    public void realizarAtaque(IEntidad objetivo) {
        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - 2);
        int dano = this.getAtaque() + (fuerza / 2);
        objetivo.recibirDano(dano);
    }

    @Override
    public void realizarDefensa() {
        this.setDefendiendo(true);
        this.setEnergia(this.getEnergiaMax());
    }

    @Override
    public void usarHabilidad(IEntidad objetivo) {
        if (this.getHabilidad() == null || !this.getHabilidad().sePuedeUsar(this)) return;
        this.setEnergia(this.getEnergia() - this.getHabilidad().getCostoEnergia());
        int dano = this.getHabilidad().getPotencia() + fuerza;
        if (this.getHabilidad().getEfecto() != null && this.getHabilidad().getEfecto().getTipo() == EFECTOS.ATAQUE_MULTIPLE) {
            List<Entidad> enemigos = Orquestador.getInstance().getBatalla().getEnemigos().getEntidades();
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
    }

    public int getFuerza() { return fuerza; }
    public void setFuerza(int fuerza) { this.fuerza = fuerza; }
}