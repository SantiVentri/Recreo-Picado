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
        this.alumnos = alumnos;
        this.batalla = batalla;
        this.indiceAlumno = 0;
        this.indiceEnemigo = 0;
        this.turnoAlumno = true;

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
        procesarEfectosTurnoActual();
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
            indiceAlumno = (indiceAlumno + 1) % turnosAlumnos.size();
            
            turnoAlumno = false;
            avanzarEnemigoVivo(); 
        } else {
            indiceEnemigo = (indiceEnemigo + 1) % turnosEnemigos.size();
            
            turnoAlumno = true;
            avanzarAlumnoVivo();
        }

        Entidad siguiente = getEntidadActual();
        if (siguiente != null && siguiente.estaVivo()) {
            siguiente.aumentarEnergia();
        }

        procesarEfectosTurnoActual();
    }

    /**
     * Aplica los efectos activos (veneno, regeneración, etc.) de la entidad
     * a la que le toca actuar ahora. Si el efecto la mata (ej. veneno),
     * se avanza al siguiente turno para no dejar actuar a una entidad muerta.
     */
    private void procesarEfectosTurnoActual() {
        Entidad actual = getEntidadActual();
        actual.procesarEfectos();

        if (!actual.estaVivo()) {
            if (turnoAlumno) {
                avanzarAlumnoVivo();
            } else {
                avanzarEnemigoVivo();
            }
            // Si tras avanzar sigue sin haber nadie vivo, batallaTerminada() lo detectará
            // en el chequeo correspondiente del ciclo de batalla.
        }
    }

    private void avanzarAlumnoVivo() {
        int intentos = 0;
        while (!turnosAlumnos.get(indiceAlumno).estaVivo() && intentos < turnosAlumnos.size()) {
            indiceAlumno = (indiceAlumno + 1) % turnosAlumnos.size();
            intentos++;
        }
    }

    private void avanzarEnemigoVivo() {
        int intentos = 0;
        while (!turnosEnemigos.get(indiceEnemigo).estaVivo() && intentos < turnosEnemigos.size()) {
            indiceEnemigo = (indiceEnemigo + 1) % turnosEnemigos.size();
            intentos++;
        }
    }

    public List<Entidad> ejecutarTurno(ACCIONES accion, Entidad objetivo, Item item) {
        Entidad e = getEntidadActual();
        List<Entidad> afectados = new ArrayList<Entidad>();

        if (accion.equals(ACCIONES.ATACAR)) {
            e.realizarAtaque(objetivo);
            if (objetivo != null) afectados.add((Entidad) objetivo);

        } else if (accion.equals(ACCIONES.DEFENDER)) {
            e.realizarDefensa();

        } else if (accion.equals(ACCIONES.USAR_HABILIDAD)) {
            e.usarHabilidad(objetivo);
            // Si fue ataque múltiple, todos los enemigos vivos fueron afectados
            if (objetivo == null || esHabilidadMultiple(e)) {
                for (Entidad enemigo : batalla.getEnemigos().getEntidades()) {
                    if (enemigo.estaVivo()) afectados.add(enemigo);
                }
            } else {
                if (objetivo != null) afectados.add((Entidad) objetivo);
            }
        }

        return afectados;
    }

    private boolean esHabilidadMultiple(Entidad e) {
        return e.getHabilidad() != null
            && e.getHabilidad().getEfecto() != null
            && e.getHabilidad().getEfecto().getTipo() == enums.EFECTOS.ATAQUE_MULTIPLE;
    }

    public List<Entidad> ejecutarTurnoEnemigo() {
        Entidad enemigo = getEntidadActual();

        List<Entidad> objetivosVivos = new ArrayList<Entidad>();
        for (int i = 0; i < alumnos.getEntidades().size(); i++) {
            Entidad e = alumnos.getEntidades().get(i);
            if (e.estaVivo()) {
                objetivosVivos.add(e);
            }
        }

        if (objetivosVivos.isEmpty()) return new ArrayList<Entidad>();

        int indice = (int) (Math.random() * objetivosVivos.size());
        Entidad objetivo = objetivosVivos.get(indice);
        enemigo.realizarAtaque(objetivo);

        List<Entidad> atacados = new ArrayList<Entidad>();
        atacados.add(objetivo);
        return atacados;
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

    public List<Entidad> getTurnosCompletos() {
        List<Entidad> resultado = new ArrayList<>();
        
        int totalAlumnos = turnosAlumnos.size();
        int totalEnemigos = turnosEnemigos.size();
        
        for (int i = 0; i < 6; i++) {
            if (turnoAlumno) {
                if (i % 2 == 0) {
                    resultado.add(turnosAlumnos.get((indiceAlumno + (i/2)) % totalAlumnos));
                } else {
                    resultado.add(turnosEnemigos.get((indiceEnemigo + (i/2)) % totalEnemigos));
                }
            } else {
                if (i % 2 == 0) {
                    resultado.add(turnosEnemigos.get((indiceEnemigo + (i/2)) % totalEnemigos));
                } else {
                    resultado.add(turnosAlumnos.get((indiceAlumno + (i/2)) % totalAlumnos));
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