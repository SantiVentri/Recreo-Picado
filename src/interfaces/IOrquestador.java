package interfaces;

import java.util.List;

import enums.ACCIONES;
import modelo.*;

public interface IOrquestador {
    void iniciarBatalla(Equipo alumnos, Batalla batalla);
    Entidad getEntidadActual();
    boolean esTurnoDeAlumno();
    void proximoTurno();
    List<Entidad> ejecutarTurno(ACCIONES accion, Entidad objetivo, Item item);
    List<Entidad> ejecutarTurnoEnemigo();    
    boolean batallaTerminada();
    boolean alumnosGanaron();
	void reiniciar();
}