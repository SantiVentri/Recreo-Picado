package modelo;

import java.util.ArrayList;
import java.util.List;

import interfaces.IEntidad;

public class Curandera extends Alumno {
    private int amistad;

    public Curandera(String nombre, int vidaMax, int energiaMax, int velocidad, int ataque, int defensa, Habilidad habilidad, int amistad) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
        this.amistad = amistad;
    }

    @Override
    protected int calcularDanoAtaque() {
        return this.getAtaque() + (amistad / 4);
    }
 
    /**
     * La habilidad de la curandera aumenta la vida del alumno vivo con menos vida.
     * Puede ser que ese alumno con menos vida sea la propia curandera.
     */
    @Override
    public void usarHabilidad(IEntidad objetivo, List<Entidad> alumnos, List<Entidad> enemigos) {
        Habilidad hab = this.getHabilidad();
        if (hab == null || !hab.sePuedeUsar(this)) return;

        this.setDefendiendo(false);
        this.setEnergia(this.getEnergia() - hab.getCostoEnergia());
        
        List<Entidad> alumnosVivos = new ArrayList<Entidad>();
        for (Entidad a : alumnos) {
            if (a.estaVivo()) alumnosVivos.add(a);
        }

        if (alumnosVivos.isEmpty()) return;

        objetivo = alumnosVivos.get(0);
        for (Entidad alumno : alumnosVivos) {
            if (alumno.getVida() < ((Entidad) objetivo).getVida()) {
                objetivo = alumno;
            }
        }

        int curacion = hab.getPotencia() + amistad;
        objetivo.aumentarVida(curacion);

        notificarUsandoHabilidad();
    }
 
    // Getters y setters
    public int getAmistad() {
    	return amistad;
    }
    
    public void setAmistad(int amistad) {
    	this.amistad = amistad;
    }   
}