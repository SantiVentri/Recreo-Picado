package orquestador;

import modelo.*;
import modelo.factories.RecompensaFactory;

public class GestorRecompensas {
	
    public Recompensa procesarResultado(boolean ganaron, Batalla batalla, Equipo alumnos) {
        Recompensa recompensa = null;

        if (ganaron) {
            recompensa = otorgarRecompensa(batalla, alumnos);
        }

        Repositorio.getInstance().guardarPartidaActual();
        return recompensa;
    }

    private Recompensa otorgarRecompensa(Batalla batalla, Equipo alumnos) {
        Recompensa recompensa = RecompensaFactory.obtenerRecompensaPorBatalla(batalla);
        Partida partidaActual = Repositorio.getInstance().getPartidaActual();

        // Otorga el oro
        partidaActual.recibirPesos(recompensa.getOro());

        // Agrega los ítems obtenidos al inventario de la partida
        for (Item item : recompensa.getItems()) {
            if (item != null) {
                partidaActual.agregarItem(item);
            }
        }

        // Reparte la XP entre los alumnos del equipo
        for (Entidad alumno : alumnos.getEntidades()) {
            if (alumno instanceof Alumno) {
                ((Alumno) alumno).recibirXp(recompensa.getXp());
            }
        }

        recompensa.setReclamada();
        return recompensa;
    }
}