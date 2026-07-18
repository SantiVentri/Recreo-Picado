package orquestador;

import modelo.Batalla;
import modelo.Partida;
import modelo.Repositorio;

public class GestorProgreso {
    // Marca la batalla jugada como ganada y desbloquea la siguiente
    public void registrarVictoria(Batalla batallaJugada) {
        Partida partida = Repositorio.getInstance().getPartidaActual();
        partida.desbloquearProximaBatalla(batallaJugada);
    }

    // Devuelve la próxima batalla disponible para jugar (o null si no hay)
    public Batalla getProximaBatallaDisponible() {
        return Repositorio.getInstance().getPartidaActual().getProximaBatalla();
    }

    // Nivel actual del jugador, para pantallas como "Cargar partida"
    public int getNivelActual() {
        return Repositorio.getInstance().getPartidaActual().getNivelActual();
    }

    // Para saber si completo el juego
    public boolean juegoCompletado() {
        return getProximaBatallaDisponible() == null;
    }
}