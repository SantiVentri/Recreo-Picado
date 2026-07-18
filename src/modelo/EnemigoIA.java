package modelo;

import java.util.ArrayList;
import java.util.List;

import interfaces.IEnemigoIA;

public class EnemigoIA implements IEnemigoIA {

	@Override
	public Entidad seleccionarObjetivo(Entidad enemigoActual, List<Entidad> alumnos) {
	      List<Entidad> objetivosVivos = new ArrayList<Entidad>();
	        for (Entidad e : alumnos) {
	            if (e.estaVivo()) {
	                objetivosVivos.add(e);
	            }
	        }
	 
	        if (objetivosVivos.isEmpty()) {
	            return null;
	        }
	 
	        int indice = (int) (Math.random() * objetivosVivos.size());
	        return objetivosVivos.get(indice);
	}

	@Override
	public void ejecutarAccion(Entidad enemigo, Entidad objetivo, List<Entidad> alumnos, List<Entidad> enemigos) {
        if (objetivo == null) {
            return;
        }	 
        if (!(enemigo instanceof Secuaz) && enemigo.getEnergia() >= enemigo.getHabilidad().getCostoEnergia()) {
            enemigo.usarHabilidad(objetivo, alumnos, enemigos);;
        } else if (enemigo.getEnergia() > 15) {
            enemigo.realizarAtaque(objetivo);
        } else {
            enemigo.realizarDefensa();
        }
	}

}
