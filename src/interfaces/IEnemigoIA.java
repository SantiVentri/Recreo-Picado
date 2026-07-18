package interfaces;

import java.util.List;
import modelo.Entidad;

public interface IEnemigoIA {
	Entidad seleccionarObjetivo(Entidad enemigoActual, List<Entidad> alumnos);
	
	void ejecutarAccion(Entidad enemigo, Entidad objetivo, List<Entidad> alumnos, List<Entidad> enemigos);
}
