package orquestador;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import enums.ACCIONES;
import interfaces.IOrquestador;
import modelo.Batalla;
import modelo.Entidad;
import modelo.Equipo;
import modelo.Item;
import modelo.Repositorio;

public class Orquestador implements IOrquestador {
    private static Orquestador instancia;

    private Equipo alumnos;
    private Batalla batalla;

    // Lista de alumnos ordenada por velocidad (mayor velocidad = primero)
    private List<Entidad> turnosAlumnos;
    private int indiceAlumno;

    // Lista de enemigos ordenada por velocidad (mayor velocidad = primero)
    private List<Entidad> turnosEnemigos;
    private int indiceEnemigo;

    // true = turno de alumno, false = turno de enemigo
    private boolean turnoAlumno;

    private Orquestador() {
        this.turnosAlumnos  = new ArrayList<Entidad>();
        this.turnosEnemigos = new ArrayList<Entidad>();
        this.indiceAlumno   = 0;
        this.indiceEnemigo  = 0;
        this.turnoAlumno    = true;
    }

    public static Orquestador getInstance() {
        if (instancia == null) {
            instancia = new Orquestador();
        }
        return instancia;
    }

    @Override
    public void iniciarBatalla(Equipo alumnos, Batalla batalla) {
        this.alumnos  = alumnos;
        this.batalla  = batalla;
        this.indiceAlumno  = 0;
        this.indiceEnemigo = 0;
        this.turnoAlumno   = true;

        // Ordenar alumnos por velocidad descendente
        turnosAlumnos.clear();
        turnosAlumnos.addAll(alumnos.getEntidades());
        turnosAlumnos.sort(Comparator.comparing(Entidad::getVelocidad).reversed());

        // Ordenar enemigos por velocidad descendente
        turnosEnemigos.clear();
        turnosEnemigos.addAll(batalla.getEnemigos().getEntidades());
        turnosEnemigos.sort(Comparator.comparing(Entidad::getVelocidad).reversed());

        // Avanzar al primer alumno vivo
        avanzarAlumnoVivo();
    }

    @Override
    public Entidad getEntidadActual() {
        if (turnoAlumno) {
            return turnosAlumnos.get(indiceAlumno);
        } else {
            return turnosEnemigos.get(indiceEnemigo);
        }
    }

    @Override
    public boolean esTurnoDeAlumno() {
        return turnoAlumno;
    }

    @Override
    public void proximoTurno() {
        if (turnoAlumno) {
            // Después del alumno, le toca al siguiente enemigo vivo
            avanzarEnemigoVivo();
            turnoAlumno = false;
        } else {
            // Después del enemigo, le toca al siguiente alumno vivo
            indiceAlumno = (indiceAlumno + 1) % turnosAlumnos.size();
            avanzarAlumnoVivo();
            turnoAlumno = true;
        }
    }

    private void avanzarAlumnoVivo() {
        int intentos = 0;
        while (!turnosAlumnos.get(indiceAlumno).estaVivo()
                && intentos < turnosAlumnos.size()) {
            indiceAlumno = (indiceAlumno + 1) % turnosAlumnos.size();
            intentos++;
        }
    }

    private void avanzarEnemigoVivo() {
        int intentos = 0;
        // Avanzar al siguiente y buscar uno vivo
        indiceEnemigo = (indiceEnemigo + 1) % turnosEnemigos.size();
        while (!turnosEnemigos.get(indiceEnemigo).estaVivo()
                && intentos < turnosEnemigos.size()) {
            indiceEnemigo = (indiceEnemigo + 1) % turnosEnemigos.size();
            intentos++;
        }
    }

    public void ejecutarTurno(ACCIONES accion, Entidad objetivo, Item item) {
        Entidad e = getEntidadActual();
        if (accion.equals(ACCIONES.ATACAR)) {
            e.realizarAtaque(objetivo);
        } else if (accion.equals(ACCIONES.DEFENDER)) {
            e.realizarDefensa();
        } else if (accion.equals(ACCIONES.USAR_HABILIDAD)) {
            e.usarHabilidad(objetivo);
        }
    }

    public void ejecutarTurnoEnemigo() {
        Entidad enemigo = getEntidadActual();

        List<Entidad> objetivosVivos = new ArrayList<Entidad>();
        for (int i = 0; i < alumnos.getEntidades().size(); i++) {
            Entidad e = alumnos.getEntidades().get(i);
            if (e.estaVivo()) {
                objetivosVivos.add(e);
            }
        }

        if (objetivosVivos.isEmpty()) return;

        int indice = (int) (Math.random() * objetivosVivos.size());
        enemigo.realizarAtaque(objetivosVivos.get(indice));
    }

    @Override
    public boolean batallaTerminada() {
        boolean alumnosMuertos = true;
        for (int i = 0; i < alumnos.getEntidades().size(); i++) {
            if (alumnos.getEntidades().get(i).estaVivo()) {
                alumnosMuertos = false;
                break;
            }
        }
        boolean enemigosMuertos = true;
        for (int i = 0; i < batalla.getEnemigos().getEntidades().size(); i++) {
            if (batalla.getEnemigos().getEntidades().get(i).estaVivo()) {
                enemigosMuertos = false;
                break;
            }
        }
        return alumnosMuertos || enemigosMuertos;
    }

    @Override
    public boolean alumnosGanaron() {
        for (int i = 0; i < batalla.getEnemigos().getEntidades().size(); i++) {
            if (batalla.getEnemigos().getEntidades().get(i).estaVivo()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void reiniciar() {
        this.alumnos  = null;
        this.batalla  = null;
        this.turnosAlumnos.clear();
        this.turnosEnemigos.clear();
        this.indiceAlumno  = 0;
        this.indiceEnemigo = 0;
        this.turnoAlumno   = true;
    }

    public void terminarBatalla() {
        Batalla batallaJugada = this.batalla;
        reiniciar();
        Repositorio.getInstance().getPartidaActual().desbloquearProximaBatalla(batallaJugada);
    }

    // Getters
    public Equipo getAlumnos() { return alumnos; }
    public Batalla getBatalla() { return batalla; }
    public int getTurnoActual() { return indiceAlumno; }
    public List<Entidad> getTurnos() { return turnosAlumnos; }

    /**
     * Devuelve la lista completa de turnos intercalados (alumno, enemigo, alumno, enemigo...)
     * comenzando desde la entidad que tiene el turno ahora.
     * Útil para mostrar el panel de orden de turnos.
     */
    public List<Entidad> getTurnosCompletos() {
        List<Entidad> resultado = new ArrayList<Entidad>();
        if (turnosAlumnos.isEmpty()) return resultado;

        int totalAlumnos  = turnosAlumnos.size();
        int totalEnemigos = turnosEnemigos.size();
        int maxPares = Math.max(totalAlumnos, totalEnemigos);

        if (!turnoAlumno && totalEnemigos > 0) {
            // Turno del enemigo: enemigo va primero
            for (int i = 0; i < maxPares; i++) {
                int idxE = (indiceEnemigo + i) % totalEnemigos;
                Entidad enemigo = turnosEnemigos.get(idxE);
                if (enemigo.estaVivo()) resultado.add(enemigo);

                int idxA = (indiceAlumno + 1 + i) % totalAlumnos;
                Entidad alumno = turnosAlumnos.get(idxA);
                if (alumno.estaVivo()) resultado.add(alumno);
            }
        } else {
            // Turno del alumno: alumno va primero
            for (int i = 0; i < maxPares; i++) {
                int idxA = (indiceAlumno + i) % totalAlumnos;
                Entidad alumno = turnosAlumnos.get(idxA);
                if (alumno.estaVivo()) resultado.add(alumno);

                if (totalEnemigos > 0) {
                    int idxE = (indiceEnemigo + i) % totalEnemigos;
                    Entidad enemigo = turnosEnemigos.get(idxE);
                    if (enemigo.estaVivo()) resultado.add(enemigo);
                }
            }
        }

        return resultado;
    }

    public int getIndiceAlumnoActual() {
        if (!turnoAlumno || alumnos == null) return -1;
        Entidad actual = getEntidadActual();
        List<Entidad> lista = alumnos.getEntidades();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i) == actual) return i;
        }
        return -1;
    }

    public int getIndiceEnemigoActual() {
        if (turnoAlumno || batalla == null) return -1;
        Entidad actual = getEntidadActual();
        List<Entidad> lista = batalla.getEnemigos().getEntidades();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i) == actual) return i;
        }
        return -1;
    }
}