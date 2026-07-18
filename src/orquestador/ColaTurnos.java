package orquestador;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import modelo.Entidad;
import modelo.Equipo;

public class ColaTurnos {

    // Lista de alumnos ordenada por velocidad (mayor velocidad = primero)
    private List<Entidad> turnosAlumnos;
    private int indiceAlumno;

    // Lista de enemigos ordenada por velocidad (mayor velocidad = primero)
    private List<Entidad> turnosEnemigos;
    private int indiceEnemigo;

    // true = turno de alumno, false = turno de enemigo
    private boolean turnoAlumno;

    public ColaTurnos() {
        this.turnosAlumnos = new ArrayList<Entidad>();
        this.turnosEnemigos = new ArrayList<Entidad>();
        this.indiceAlumno = 0;
        this.indiceEnemigo = 0;
        this.turnoAlumno = true;
    }

    /**
     * Ordena a los equipos por velocidad descendente y deja
     * la cola lista para arrancar en el primer alumno vivo.
     */
    public void iniciar(Equipo alumnos, Equipo enemigos) {
        this.indiceAlumno = 0;
        this.indiceEnemigo = 0;
        this.turnoAlumno = true;

        turnosAlumnos.clear();
        turnosAlumnos.addAll(alumnos.getEntidades());
        turnosAlumnos.sort(Comparator.comparing(Entidad::getVelocidad).reversed());

        turnosEnemigos.clear();
        turnosEnemigos.addAll(enemigos.getEntidades());
        turnosEnemigos.sort(Comparator.comparing(Entidad::getVelocidad).reversed());

        avanzarAlumnoVivo();
    }

    public void reiniciar() {
        turnosAlumnos.clear();
        turnosEnemigos.clear();
        indiceAlumno = 0;
        indiceEnemigo = 0;
        turnoAlumno = true;
    }

    public Entidad getEntidadActual() {
        if (turnoAlumno) {
            return turnosAlumnos.get(indiceAlumno);
        } else {
            return turnosEnemigos.get(indiceEnemigo);
        }
    }

    public boolean esTurnoDeAlumno() {
        return turnoAlumno;
    }

    /**
     * Avanza al índice del siguiente participante (vivo o no) del bando
     * que está actuando ahora, y cambia el turno al otro bando.
     */
    public void avanzar() {
        if (turnoAlumno) {
            indiceAlumno = (indiceAlumno + 1) % turnosAlumnos.size();
            turnoAlumno = false;
            avanzarEnemigoVivo();
        } else {
            indiceEnemigo = (indiceEnemigo + 1) % turnosEnemigos.size();
            turnoAlumno = true;
            avanzarAlumnoVivo();
        }
    }

    /**
     * Si la entidad actual está muerta, avanza al siguiente vivo de su
     * mismo bando sin cambiar de turno (se usa tras procesar efectos
     * que puedan haber matado a quien iba a actuar).
     */
    public void avanzarSiEntidadActualMurio() {
        Entidad actual = getEntidadActual();
        if (!actual.estaVivo()) {
            if (turnoAlumno) {
                avanzarAlumnoVivo();
            } else {
                avanzarEnemigoVivo();
            }
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

    /**
     * Predice los próximos "cantidad" turnos (sin mutar el estado real
     * de la cola), revalidando primero que los índices actuales sigan
     * apuntando a entidades vivas para cada bando.
     */
    public List<Entidad> predecirProximosTurnos(int cantidad) {
        List<Entidad> resultado = new ArrayList<>();

        boolean hayAlumnoVivo = hayVivo(turnosAlumnos);
        boolean hayEnemigoVivo = hayVivo(turnosEnemigos);

        int idxAlumno = hayAlumnoVivo ? primerVivoDesde(turnosAlumnos, indiceAlumno) : indiceAlumno;
        int idxEnemigo = hayEnemigoVivo ? primerVivoDesde(turnosEnemigos, indiceEnemigo) : indiceEnemigo;
        boolean esTurnoAlumno = turnoAlumno;

        for (int i = 0; i < cantidad && (hayAlumnoVivo || hayEnemigoVivo); i++) {
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

    public int getIndiceAlumnoActual(Equipo alumnos) {
        if (!turnoAlumno || alumnos == null) return -1;
        Entidad actual = getEntidadActual();
        List<Entidad> lista = alumnos.getEntidades();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i) == actual) return i;
        }
        return -1;
    }

    public int getIndiceEnemigoActual(Equipo enemigos) {
        if (turnoAlumno || enemigos == null) return -1;
        Entidad actual = getEntidadActual();
        List<Entidad> lista = enemigos.getEntidades();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i) == actual) return i;
        }
        return -1;
    }

    public int getIndiceAlumno() {
    	return indiceAlumno;
	}

    public List<Entidad> getTurnosAlumnos() {
    	return turnosAlumnos;
	}
}