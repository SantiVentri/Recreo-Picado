package modelo;
 
import interfaces.IEntidad;
 
public class Curandera extends Entidad {
 
    private int amistad;
 
    public Curandera(String nombre, int vidaMax, int energiaMax, int velocidad,
                     int ataque, int defensa, Habilidad habilidad, int amistad) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
        this.amistad = amistad;
    }
 
    @Override
    public void realizarAtaque(IEntidad objetivo) {
        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - 2);
        int dano = this.getAtaque() + (amistad / 4);
        objetivo.recibirDano(dano);
    }
 
    @Override
    public void realizarDefensa() {
        this.setDefendiendo(true);
        this.setEnergia(this.getEnergiaMax());
    }
 
    /**
     * Curación: se restaura vida a sí mismo usando la potencia de la habilidad + amistad.
     * El parámetro objetivo es ignorado, el curandero siempre se cura a sí mismo.
     * Si la habilidad tiene un efecto positivo, también se lo aplica.
     */
    @Override
    public void usarHabilidad(IEntidad objetivo) {
        Habilidad hab = this.getHabilidad();
        if (hab == null || !hab.sePuedeUsar(this)) return;
 
        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - hab.getCostoEnergia());
 
        int curacion = hab.getPotencia() + amistad;
        this.aumentarVida(curacion);
 
        if (hab.getEfecto() != null) {
            this.aplicarEfecto(hab.getEfecto());
        }
    }
 
    public int getAmistad() { return amistad; }
    public void setAmistad(int amistad) { this.amistad = amistad; }
}