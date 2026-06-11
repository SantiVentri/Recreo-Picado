package interfaces;

import modelo.*;

public interface IOrquestador {
    void iniciarBatalla(Equipo alumnos, Batalla batalla);
    Entidad getEntidadActual();
    boolean esTurnoDeAlumno();
    void proximoTurno();
    boolean batallaTerminada();
    boolean alumnosGanaron();
}