package interfaces;

import modelo.*;
import orquestador.Orquestador;

public interface IOrquestador {
    void iniciarBatalla(Equipo alumnos, Batalla batalla);
    Entidad getEntidadActual();
    boolean esTurnoDeAlumno();
    void proximoTurno();
    boolean batallaTerminada();
    boolean alumnosGanaron();
	void reiniciar();
}