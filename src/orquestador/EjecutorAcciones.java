package orquestador;

import java.util.ArrayList;
import java.util.List;

import enums.ACCIONES;
import modelo.*;

public class EjecutorAcciones {

    public List<Entidad> ejecutarTurno(Entidad entidadActual, ACCIONES accion, Entidad objetivo, Item item, Equipo alumnos, Batalla batalla) {
        List<Entidad> afectados = new ArrayList<Entidad>();

        if (accion.equals(ACCIONES.ATACAR)) {
            entidadActual.realizarAtaque(objetivo);
            if (objetivo != null) afectados.add(objetivo);

        } else if (accion.equals(ACCIONES.DEFENDER)) {
            entidadActual.realizarDefensa();

        } else if (accion.equals(ACCIONES.USAR_HABILIDAD)) {
            entidadActual.usarHabilidad(objetivo, alumnos.getEntidades(), batalla.getEnemigos().getEntidades());

            // Si fue ataque múltiple, todos los enemigos vivos fueron afectados
            if (objetivo == null || entidadActual.getHabilidad().esHabilidadMultiple()) {
                for (Entidad enemigo : batalla.getEnemigos().getEntidades()) {
                    if (enemigo.estaVivo()) afectados.add(enemigo);
                }
            } else {
                afectados.add(objetivo);
            }

        } else if (accion.equals(ACCIONES.USAR_ITEM)) {
            if (item instanceof Pocion && entidadActual instanceof Alumno) {
                Pocion pocion = (Pocion) item;
                ((Alumno) entidadActual).usarItem(pocion);
                Repositorio.getInstance().getPartidaActual().quitarItem(item);
            }
            afectados.add(entidadActual);
        }

        return afectados;
    }

    public List<Entidad> ejecutarTurnoEnemigo(Entidad enemigoActual, EnemigoIA iaEnemigo, Equipo alumnos, Batalla batalla) {
        List<Entidad> atacados = new ArrayList<Entidad>();

        Entidad objetivo = iaEnemigo.seleccionarObjetivo(enemigoActual, alumnos.getEntidades());
        if (objetivo == null) {
            return atacados;
        }

        iaEnemigo.ejecutarAccion(enemigoActual, objetivo, alumnos.getEntidades(), batalla.getEnemigos().getEntidades());

        atacados.add(objetivo);
        return atacados;
    }
}