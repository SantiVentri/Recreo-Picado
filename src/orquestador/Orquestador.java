package orquestador;

import java.util.List;

import enums.ACCIONES;
import interfaces.IOrquestador;
import modelo.*;

public class Orquestador implements IOrquestador {
    private static Orquestador instancia;

    private Equipo alumnos;
    private Batalla batalla;

    // Orden y avance de turnos (delegado)
    private ColaTurnos colaTurnos;

    // IA de los enemigos
    private EnemigoIA iaEnemigo;

    // Ejecutor de acciones
    private EjecutorAcciones ejecutorAcciones;
    
    // Gestor de progreso
    private GestorProgreso gestorProgreso;

    private Orquestador() {
        this.colaTurnos = new ColaTurnos();
        this.iaEnemigo = new EnemigoIA();
        this.ejecutorAcciones = new EjecutorAcciones();
        this.gestorProgreso = new GestorProgreso();
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
    	alumnos.resetearParaBatalla();
    	batalla.getEnemigos().resetearParaBatalla();

        this.alumnos = alumnos;
        this.batalla = batalla;

        colaTurnos.iniciar(alumnos, batalla.getEnemigos());
        procesarEfectosTurnoActual();
    }

    @Override
    public Entidad getEntidadActual() {
        return colaTurnos.getEntidadActual();
    }

    @Override
    public boolean esTurnoDeAlumno() {
        return colaTurnos.esTurnoDeAlumno();
    }

    @Override
    public void proximoTurno() {
        colaTurnos.avanzar();

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
        colaTurnos.avanzarSiEntidadActualMurio();
        // Si tras avanzar sigue sin haber nadie vivo, batallaTerminada() lo detectará
        // en el chequeo correspondiente del ciclo de batalla.
    }

    @Override
    public List<Entidad> ejecutarTurno(ACCIONES accion, Entidad objetivo, Item item) {
        Entidad entidadActual = getEntidadActual();
        return ejecutorAcciones.ejecutarTurno(entidadActual, accion, objetivo, item, alumnos, batalla);
    }

    @Override
    public List<Entidad> ejecutarTurnoEnemigo() {
        Entidad enemigoActual = getEntidadActual();
        return ejecutorAcciones.ejecutarTurnoEnemigo(enemigoActual, iaEnemigo, alumnos, batalla);
    }

    @Override
    public boolean batallaTerminada() {
        return alumnos.estaDerrotado() || batalla.getEnemigos().estaDerrotado();
    }

    @Override
    public boolean alumnosGanaron() {
        return batalla.getEnemigos().estaDerrotado();
    }

    @Override
    public void reiniciar() {
        this.alumnos = null;
        this.batalla = null;
        colaTurnos.reiniciar();
    }

    public void terminarBatalla() {
        Batalla batallaJugada = this.batalla;
        reiniciar();
        gestorProgreso.registrarVictoria(batallaJugada);
    }

    // Getters
    public Equipo getAlumnos() { return alumnos; }
    public Batalla getBatalla() { return batalla; }
    public int getTurnoActual() { return colaTurnos.getIndiceAlumno(); }
    public List<Entidad> getTurnos() { return colaTurnos.getTurnosAlumnos(); }

    public List<Entidad> getTurnosCompletos() {
        return colaTurnos.predecirProximosTurnos(6);
    }

    public int getIndiceAlumnoActual() {
        return colaTurnos.getIndiceAlumnoActual(alumnos);
    }

    public int getIndiceEnemigoActual() {
        return colaTurnos.getIndiceEnemigoActual(batalla != null ? batalla.getEnemigos() : null);
    }
}