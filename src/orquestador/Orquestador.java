package orquestador;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import enums.ACCIONES;
import interfaces.IOrquestador;
import modelo.*;

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
        // Resetear stats de combate antes de empezar
        for (Entidad alumno : alumnos.getEntidades()) {
            alumno.resetearParaBatalla();
        }
        
        batalla.resetearEnemigos();
        
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
            e.usarHabilidad(objetivo, alumnos.getEntidades(), batalla.getEnemigos().getEntidades());
            // Si fue ataque múltiple, todos los enemigos vivos fueron afectados
            if (objetivo == null || esHabilidadMultiple(e)) {
                for (Entidad enemigo : batalla.getEnemigos().getEntidades()) {
                    if (enemigo.estaVivo()) afectados.add(enemigo);
                }
            } else {
                if (objetivo != null) afectados.add((Entidad) objetivo);
            }

        } else if (accion.equals(ACCIONES.USAR_ITEM)) {
            if (item instanceof Pocion && e instanceof Alumno) {
                Pocion pocion = (Pocion) item;
                ((Alumno) e).usarItem(pocion);
                Repositorio.getInstance().getPartidaActual().quitarItem(item);
            }
            afectados.add(e);
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
        
        // Si el enemigo no es un Secuaz y tiene la energia suficiente, usa su habilidad
        if (!(enemigo instanceof Secuaz) && enemigo.getEnergia() >= enemigo.getHabilidad().getCostoEnergia()) {
        	enemigo.usarHabilidad(objetivo, alumnos.getEntidades(), batalla.getEnemigos().getEntidades());
        } else if (enemigo.getEnergia() > 15) {
        	// Si el enemigo puede atacar, ataca
        	enemigo.realizarAtaque(objetivo);
        } else {
        	// Sino, se defiende para aumentar su energia
        	enemigo.realizarDefensa();
        }

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

        boolean hayAlumnoVivo = hayVivo(turnosAlumnos);
        boolean hayEnemigoVivo = hayVivo(turnosEnemigos);

        // indiceAlumno/indiceEnemigo solo se garantizan vivos para el lado
        // que está jugando en este momento; el otro lado puede haber quedado
        // apuntando a alguien que murió mientras no era su turno, así que
        // hay que revalidar antes de arrancar la simulación.
        int idxAlumno = hayAlumnoVivo ? primerVivoDesde(turnosAlumnos, indiceAlumno) : indiceAlumno;
        int idxEnemigo = hayEnemigoVivo ? primerVivoDesde(turnosEnemigos, indiceEnemigo) : indiceEnemigo;
        boolean esTurnoAlumno = turnoAlumno;

        for (int i = 0; i < 6 && (hayAlumnoVivo || hayEnemigoVivo); i++) {
            if (esTurnoAlumno) {
                if (hayAlumnoVivo) {
                    resultado.add(turnosAlumnos.get(idxAlumno));
                    idxAlumno = siguienteVivo(turnosAlumnos, idxAlumno);
                }
            } else {
                if (hayEnemigoVivo) {
                    resultado.add(turnosEnemigos.get(idxEnemigo));
                    idxEnemigo = siguienteVivo(turnosEnemigos, idxEnemigo);
                }
            }
            esTurnoAlumno = !esTurnoAlumno;
        }
        return resultado;
    }

    private boolean hayVivo(List<Entidad> turnos) {
        for (Entidad e : turnos) {
            if (e.estaVivo()) return true;
        }
        return false;
    }

    /**
     * Devuelve indiceInicial si esa entidad sigue viva; si no, avanza hasta
     * encontrar la próxima viva. Misma lógica que avanzarAlumnoVivo/avanzarEnemigoVivo,
     * pero sin mutar el estado real del Orquestador (se usa solo para predecir la cola).
     */
    private int primerVivoDesde(List<Entidad> turnos, int indiceInicial) {
        int total = turnos.size();
        int idx = indiceInicial;
        int intentos = 0;
        while (!turnos.get(idx).estaVivo() && intentos < total) {
            idx = (idx + 1) % total;
            intentos++;
        }
        return idx;
    }

    /**
     * Igual que avanzarAlumnoVivo/avanzarEnemigoVivo: busca el siguiente vivo
     * a partir del índice actual. Si solo queda uno vivo en la lista, siempre
     * vuelve a caer en ese mismo (así se repite en el panel, como pasa en el juego real).
     */
    private int siguienteVivo(List<Entidad> turnos, int indiceActual) {
        int total = turnos.size();
        int siguiente = (indiceActual + 1) % total;
        int intentos = 0;
        while (!turnos.get(siguiente).estaVivo() && intentos < total) {
            siguiente = (siguiente + 1) % total;
            intentos++;
        }
        return siguiente;
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