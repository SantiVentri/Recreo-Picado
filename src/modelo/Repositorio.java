package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Repositorio {
	private static Repositorio instancia;
	
	// Carpeta y archivo donde se guarda el progreso de la partida en curso
	private static final String CARPETA_GUARDADO = "saves";
	private static final String ARCHIVO_GUARDADO = CARPETA_GUARDADO + File.separator + "partida.sav";
	
	private Partida partidaActual;
	
	private Repositorio() { }
	
	public static Repositorio getInstance() {
		if (instancia == null) {
			instancia = new Repositorio();
		}
		
		return instancia;
	}
	
	/**
	 * Crea una partida nueva (equipo inicial, 200 pesos, batallas reiniciadas)
	 * y la deja como la partida actual en memoria.
	 */
	public void crearPartida() {
		this.partidaActual = new Partida();
	}
	
	/**
	 * Establece una partida (por ejemplo, una recién cargada desde disco)
	 * como la partida actual en memoria.
	 */
	public void cargarPartida(Partida partida) {
		this.partidaActual = partida;
	}
	
	public Partida getPartidaActual() {
		return partidaActual;
	}
	
	// ───────────────────────── Persistencia en disco ─────────────────────────
	
	/**
	 * Guarda en disco el estado actual de la partida en curso.
	 * Debe llamarse cada vez que ocurre un evento que deba persistirse:
	 * ganar/perder una batalla o comprar un objeto en el kiosko.
	 */
	public void guardarPartidaActual() {
		if (partidaActual == null) {
			return;
		}
		
		try {
			File carpeta = new File(CARPETA_GUARDADO);
			if (!carpeta.exists()) {
				carpeta.mkdirs();
			}
			
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_GUARDADO))) {
				oos.writeObject(partidaActual);
			}
		} catch (IOException e) {
			System.out.println("Error al guardar la partida: " + e.getMessage());
		}
	}
	
	/**
	 * Indica si existe una partida guardada en disco.
	 */
	public boolean hayPartidaGuardada() {
		return new File(ARCHIVO_GUARDADO).exists();
	}
	
	/**
	 * Borra la partida guardada en disco (si existe). No afecta a la partida
	 * actual en memoria si ya se está jugando una.
	 */
	public void borrarPartidaGuardada() {
		File archivo = new File(ARCHIVO_GUARDADO);
		if (archivo.exists()) {
			archivo.delete();
		}
	}
	
	/**
	 * Lee la partida guardada en disco y la devuelve, sin modificar la partida actual en memoria.
	 * Devuelve null si no hay ninguna partida guardada o si ocurre un error al leerla.
	 */
	public Partida leerPartidaGuardada() {
		if (!hayPartidaGuardada()) {
			return null;
		}
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_GUARDADO))) {
			return (Partida) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error al leer la partida guardada: " + e.getMessage());
			return null;
		}
	}
}
