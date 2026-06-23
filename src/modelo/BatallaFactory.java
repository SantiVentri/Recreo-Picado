package modelo;

import enums.ESTADO_BATALLA;

public class BatallaFactory {
	public static Batalla crearNivel1() {
		Equipo enemigos = new Equipo();
		enemigos.agregarEntidad(PersonajeFactory.crearJefe1());
		
		Batalla nivel1 = new Batalla(enemigos, "Baño");
		nivel1.setEstado(ESTADO_BATALLA.PENDIENTE);
		return nivel1;
	}
	
	public static Batalla crearNivel2() {
		Equipo enemigos = new Equipo();
		enemigos.agregarEntidad(PersonajeFactory.crearJefe2());
		// enemigos.agregarEntidad(PersonajeFactory.crearSecuazBasico(2));
		
		return new Batalla(enemigos, "Enfermeria");
	}
	
	public static Batalla crearNivel3() {
		Equipo enemigos = new Equipo();
		enemigos.agregarEntidad(PersonajeFactory.crearJefe3());
		// enemigos.agregarEntidad(PersonajeFactory.crearSecuazBasico(3));
		// enemigos.agregarEntidad(PersonajeFactory.crearSecuazBasico(3));
		
		return new Batalla(enemigos, "Patio");
	}
	
	public static Batalla crearNivel4() {
		Equipo enemigos = new Equipo();
		enemigos.agregarEntidad(PersonajeFactory.crearJefeFinal());
		// enemigos.agregarEntidad(PersonajeFactory.crearSecuazBasico(4));
		// enemigos.agregarEntidad(PersonajeFactory.crearSecuazBasico(4));
		// enemigos.agregarEntidad(PersonajeFactory.crearSecuazBasico(4));
		
		return new Batalla(enemigos, "Direccion");
	}
}