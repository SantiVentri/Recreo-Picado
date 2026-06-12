package modelo;

public class Repositorio {
	private static Repositorio instancia;
	private static final int cantidadMaxima = 3;
	
	private Partida[] partidas;
	private int idPartidaActual;
	
	private Repositorio() {
		partidas = new Partida[3];
	}
	
	public static Repositorio getInstance() {
		if (instancia == null) {
			instancia = new Repositorio();
		}
		
		return instancia;
	}
	
	public void cargarPartida(int id) {
		this.idPartidaActual = id;
	}
	
	public void crearPartida() {
		for (int i = 0; i < partidas.length; i++) {
			if (partidas[i] == null) {
				Partida nuevaPartida = new Partida();
				partidas[i] = nuevaPartida;
				idPartidaActual = i;
				return;
			}
		}
		System.out.println("No hay slots disponibles para nueva partida");
	}
	
	public Partida getPartidaActual() {
		return partidas[idPartidaActual];
	}
}