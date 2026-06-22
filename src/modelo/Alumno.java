package modelo;

public abstract class Alumno extends Entidad {

    private static final int XP_POR_NIVEL = 100;

    private int xp;
    private int nivel;

    public Alumno(String nombre, int vidaMax, int energiaMax, int velocidad, int ataque, int defensa, Habilidad habilidad) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
        this.nivel = 1;
        this.xp = 0;
    }

    /**
     * Otorga XP al alumno y sube de nivel tantas veces como corresponda.
     */
    public void recibirXp(int cantidad) {
        this.xp += cantidad;
        while (this.xp >= XP_POR_NIVEL) {
            this.xp -= XP_POR_NIVEL;
            subirNivel();
        }
    }

    protected void subirNivel() {
        this.nivel++;
        this.setAtaque(getAtaque() + 3);
        this.setDefensa(getDefensa() + 2);
        this.setVidaMax(getVidaMax() + 10);
    }

    // Getters y setters
    public int getXp() {
    	return xp;
    }
    
    public int getNivel() {
    	return nivel;
    }
    
    public void setNivel(int nivel) {
    	this.nivel = nivel;
    }
}