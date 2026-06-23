package modelo;

public class PersonajeFactory {
	// Alumnos
    public static Mago crearMago() {
        Habilidad habilidad = new Habilidad("Lanzar Gogo", "Lanza gogos mágicos a todos los enemigos", 30, 25, EfectoFactory.crearAtaqueMultiple());
        return new Mago("Mago", 80, 50, 8, 8, 3, habilidad, 18);
    }
    
    public static Arquero crearArquero() {
        Habilidad habilidad = new Habilidad("Bola de papel", "Lanza una bola de papel con saliva que enferma al rival", 25, 20, EfectoFactory.crearVeneno());
        return new Arquero("Arquero", 90, 45, 9, 12, 4, habilidad, 18);
    }

    public static Curandera crearCurandera() {
        Habilidad habilidad = new Habilidad("Curación", "Usa el turno para curar a un objetivo de su mismo equipo", 35, 35, null);
        return new Curandera("Curandera", 95, 60, 7, 6, 5, habilidad, 22);
    }

    public static Guerrero crearGuerrero() {
        Habilidad habilidad = new Habilidad("Golpe Aplastante", "Lastima a todos los enemigos que tenga cerca", 30, 25, EfectoFactory.crearAtaqueMultiple());
        return new Guerrero("Guerrero", 120, 50, 5, 14, 8, habilidad, 20);
    }
    
    // Jefes y Secuaces
    public static Secuaz crearSecuazBasico(int numeroBatalla) {
    	String nombre;
    	switch (numeroBatalla) {
    		case 2: nombre = "Secuaz de la enfermería"; break;
    		case 3: nombre = "Secuaz del patio"; break;
    		case 4: nombre = "Secuaz del director"; break;
    		default: nombre = "Secuaz"; break;
    	}
        return new Secuaz(nombre, 50, 40, 7, 12, 2, null);
    }
    
    public static Jefe crearJefe1() {
        Habilidad habilidad = new Habilidad("Botellazo químico", "Lanza un producto de limpieza y envenena al objetivo", 25, 30, EfectoFactory.crearVeneno());
        return new Jefe("Jefa del Baño", 160, 90, 6, 25, 7, habilidad);
    }

    public static Jefe crearJefe2() {
        Habilidad habilidad = new Habilidad("Inyección Dolorosa", "Aplica una inyección que envenena al objetivo", 30, 35, EfectoFactory.crearVeneno());
        return new Jefe("Enfermera", 180, 100, 7, 28, 8, habilidad);
    }

    public static Jefe crearJefe3() {
        Habilidad habilidad = new Habilidad("Patada Voladora", "Una patada que golpea con fuerza al objetivo", 35, 45, EfectoFactory.crearVeneno());
        return new Jefe("Profesor ED", 210, 110, 8, 32, 9, habilidad);
    }

    public static JefeFinal crearJefeFinal() {
        Habilidad habilidad = new Habilidad("Castigo Ejemplar", "Un castigo brutal que pone en jaque a todo el equipo", 40, 35, EfectoFactory.crearVeneno());
        return new JefeFinal("Director", 260, 130, 9, 30, 10, habilidad);
    }
}