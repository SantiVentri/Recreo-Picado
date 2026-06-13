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

    // Lista de alumnos ordenada por velocidad (solo alumnos)
    private List<Entidad> turnosAlumnos;
    private int indiceAlumno;

    // Índice rotativo de enemigos (para que en cada ronda ataque el siguiente)
    private int indiceEnemigo;

    // true = turno de alumno, false = turno de enemigo
    private boolean turnoAlumno;

    private Orquestador() {
        this.turnosAlumnos = new ArrayList<Entidad>();
        this.indiceAlumno  = 0;
        this.indiceEnemigo = 0;
        this.turnoAlumno   = true;
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

        turnosAlumnos.clear();
        turnosAlumnos.addAll(alumnos.getEntidades());
        turnosAlumnos.sort(Comparator.comparing(Entidad::getVelocidad).reversed());

        // Avanzar al primer alumno vivo
        avanzarAlumnoVivo();
    }

    // Devuelve la entidad que tiene el turno ahora
    @Override
    public Entidad getEntidadActual() {
        if (turnoAlumno) {
            return turnosAlumnos.get(indiceAlumno);
        } else {
            return batalla.getEnemigos().getEntidades().get(indiceEnemigo);
        }
    }

    @Override
    public boolean esTurnoDeAlumno() {
        return turnoAlumno;
    }

    // Alterna: si era alumno pasa a enemigo, si era enemigo pasa al siguiente alumno
    @Override
    public void proximoTurno() {
        if (turnoAlumno) {
            // Pasar al turno del enemigo: avanzar al siguiente enemigo vivo
            avanzarEnemigoVivo();
            turnoAlumno = false;
        } else {
            // Pasar al turno del siguiente alumno vivo
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
        List<Entidad> enemigos = batalla.getEnemigos().getEntidades();
        int intentos = 0;
        indiceEnemigo = (indiceEnemigo + 1) % enemigos.size();
        while (!enemigos.get(indiceEnemigo).estaVivo()
                && intentos < enemigos.size()) {
            indiceEnemigo = (indiceEnemigo + 1) % enemigos.size();
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
}