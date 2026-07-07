package modelo.factories;

import enums.ESTADO_BATALLA;
import modelo.Batalla;
import modelo.Equipo;
import modelo.Recompensa;

public class BatallaFactory {
	public static Batalla crearNivel1() {
		Equipo enemigos = new Equipo();
		enemigos.agregarEntidad(PersonajeFactory.crearJefe1());
		
		Recompensa recompensa = RecompensaFactory.crearRecompensa1();
		
		Batalla nivel1 = new Batalla(enemigos, "Baño", recompensa);
		nivel1.setEstado(ESTADO_BATALLA.PENDIENTE);
		return nivel1;
	}
	
	public static Batalla crearNivel2() {
		Equipo enemigos = new Equipo();
		enemigos.agregarEntidad(PersonajeFactory.crearJefe2());
		enemigos.agregarEntidad(PersonajeFactory.crearSecuazEnfermera());
		
		Recompensa recompensa = RecompensaFactory.crearRecompensa2();
		
		return new Batalla(enemigos, "Enfermeria", recompensa);
	}
	
	public static Batalla crearNivel3() {
		Equipo enemigos = new Equipo();
		enemigos.agregarEntidad(PersonajeFactory.crearSecuazPatio());
		enemigos.agregarEntidad(PersonajeFactory.crearJefe3());
		enemigos.agregarEntidad(PersonajeFactory.crearSecuazPatio());
		
		Recompensa recompensa = RecompensaFactory.crearRecompensa3();
		
		return new Batalla(enemigos, "Patio", recompensa);
	}
	
	public static Batalla crearNivel4() {
		Equipo enemigos = new Equipo();
		enemigos.agregarEntidad(PersonajeFactory.crearSecuazNerd());
		enemigos.agregarEntidad(PersonajeFactory.crearJefeFinal());
		enemigos.agregarEntidad(PersonajeFactory.crearSecuazNerd());
		enemigos.agregarEntidad(PersonajeFactory.crearSecuazNerd());
		
		Recompensa recompensa = RecompensaFactory.crearRecompensa4();
		
		return new Batalla(enemigos, "Direccion", recompensa);
	}
}