package orquestador;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import interfaces.IOrquestador;
import modelo.Batalla;
import modelo.Entidad;
import modelo.Equipo;

public class Orquestador implements IOrquestador {
    private static Orquestador instancia;

    private Equipo alumnos;
    private Batalla batalla;
    private List<Entidad> turnos;
    private int turnoActual;

    private Orquestador() {
        this.turnos = new ArrayList<>();
        this.turnoActual = 0;
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
        this.turnoActual = 0;
        this.turnos.clear();

        // Agregar todos: alumnos + enemigos
        turnos.addAll(alumnos.getEntidades());
        turnos.addAll(batalla.getEnemigos().getEntidades());

        // Ordenar por velocidad descendente
        turnos.sort(Comparator.comparingInt(Entidad::getVelocidad).reversed());
    }

    @Override
    public void proximoTurno() {
        if (turnos.isEmpty()) return;
        do {
            turnoActual = (turnoActual + 1) % turnos.size();
        } while (!getEntidadActual().estaVivo() && !batallaTerminada());
    }

    @Override
    public Entidad getEntidadActual() {
        return turnos.get(turnoActual);
    }

    @Override
    public boolean esTurnoDeAlumno() {
        Entidad actual = getEntidadActual();
        return alumnos.getEntidades().contains(actual);
    }

    @Override
    public boolean batallaTerminada() {
        boolean alumnosMuertos = alumnos.getEntidades().stream().noneMatch(Entidad::estaVivo);
        boolean enemigosMuertos = batalla.getEnemigos().getEntidades().stream().noneMatch(Entidad::estaVivo);
        return alumnosMuertos || enemigosMuertos;
    }

    @Override
    public boolean alumnosGanaron() {
        return batalla.getEnemigos().getEntidades().stream().noneMatch(Entidad::estaVivo);
    }

    // Getters
    public Equipo getAlumnos() {
    	return alumnos;
	}
    
    public Batalla getBatalla() {
    	return batalla;
    }
    
    public int getTurnoActual() {
    	return turnoActual;
	}
    
    public List<Entidad> getTurnos() {
    	return turnos;
	}
}