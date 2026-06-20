package modelo;
 
import java.util.ArrayList;
import java.util.List;

import interfaces.IEntidad;
import orquestador.Orquestador;
 
public class Curandera extends Entidad {
	// Atributos de nivel
	private int nivel;
	private int xp;
 
    private int amistad;
 
    public Curandera(String nombre, int vidaMax, int energiaMax, int velocidad,
                     int ataque, int defensa, Habilidad habilidad, int amistad) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
        this.amistad = amistad;
		this.nivel = 1;
		this.xp = 0;
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
     * Curación: le restaura vida al objetivo alumno usando la potencia de la habilidad + amistad.
     */
    @Override
    public void usarHabilidad(IEntidad objetivo) {
        Habilidad hab = this.getHabilidad();
        if (hab == null || !hab.sePuedeUsar(this)) return;
 
        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - hab.getCostoEnergia());
        
        // Se asigna un objetivo aleatorio entre los alumnos vivos
        List<Entidad> alumnosVivos = new ArrayList<Entidad>();
        for (Entidad a : Orquestador.getInstance().getAlumnos().getEntidades()) {
            if (a.estaVivo()) alumnosVivos.add(a);
        }
        
        if (alumnosVivos.isEmpty()) return;

        // Empezamos asumiendo que el primer alumno es el que tiene menos vida
        objetivo = alumnosVivos.get(0);

        // Recorremos el resto para encontrar si hay alguno con menos vida aún
        for (Entidad alumno : alumnosVivos) {
            if (alumno.getVida() < ((Entidad) objetivo).getVida()) {
                objetivo = alumno;
            }
        }
 
        int curacion = hab.getPotencia() + amistad;
        objetivo.aumentarVida(curacion);
    }
    
	private void subirNivel() {
	    this.nivel++;
	    this.setAtaque(getAtaque() + 3);
	    this.setDefensa(getDefensa() + 2);;
	    this.setVidaMax(getVidaMax() + 10);;
	}
    
	public void recibirXp(int xp) {
		this.xp += xp;
		
		while (this.xp >= 100) {
			this.xp -= 100;
			subirNivel(); 
		}
	}
	
	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getXp() {
		return xp;
	}
 
    public int getAmistad() { return amistad; }
    public void setAmistad(int amistad) { this.amistad = amistad; }   
}