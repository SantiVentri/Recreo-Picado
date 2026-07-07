package modelo.factories;

import modelo.Arquero;
import modelo.Curandera;
import modelo.Guerrero;
import modelo.Habilidad;
import modelo.Jefe;
import modelo.JefeFinal;
import modelo.Mago;
import modelo.Secuaz;

public class PersonajeFactory {
	// Alumnos
    public static Mago crearMago() {
        Habilidad habilidad = new Habilidad("Lanzar Gogo", "Lanza gogos mágicos a todos los enemigos", 35, 45, EfectoFactory.crearAtaqueMultiple());
        return new Mago("Mago", 80, 50, 8, 8, 3, habilidad, 18);
    }
    
    public static Arquero crearArquero() {
        Habilidad habilidad = new Habilidad("Bola de papel", "Lanza una bola de papel con saliva que enferma al rival", 28, 32, EfectoFactory.crearVeneno());
        return new Arquero("Arquero", 90, 45, 9, 12, 4, habilidad, 18);
    }
    public static Curandera crearCurandera() {
        Habilidad habilidad = new Habilidad("Curación", "Usa el turno para curar a un objetivo de su mismo equipo", 35, 35, null);
        return new Curandera("Curandera", 95, 60, 7, 6, 5, habilidad, 22);
    }
    public static Guerrero crearGuerrero() {
        Habilidad habilidad = new Habilidad("Golpe Aplastante", "Lastima a todos los enemigos que tenga cerca", 35, 45, EfectoFactory.crearAtaqueMultiple());
        return new Guerrero("Guerrero", 120, 50, 5, 14, 8, habilidad, 20);
    }
    
    // Secuaces      
    public static Secuaz crearSecuazEnfermera() {
    	return new Secuaz("Secuaz Enfermera", 120, 50, 7, 20, 9, null);
    }
    
    public static Secuaz crearSecuazPatio() {
    	return new Secuaz("Secuaz Patio", 140, 60, 7, 25, 12, null);
    }
    
    public static Secuaz crearSecuazNerd() {
    	return new Secuaz("Secuaz Nerd", 160, 70, 7, 30, 15, null);
    }
    
    // Jefes
    public static Jefe crearJefe1() {
        Habilidad habilidad = new Habilidad("Botellazo químico", "Lanza un producto de limpieza y envenena al objetivo", 25, 36, EfectoFactory.crearVeneno());
        return new Jefe("Jefa del Baño", 190, 90, 6, 30, 20, habilidad);
    }
    public static Jefe crearJefe2() {
        Habilidad habilidad = new Habilidad("Inyección Dolorosa", "Aplica una inyección que envenena al objetivo", 30, 48, EfectoFactory.crearVeneno());
        return new Jefe("Enfermera", 230, 105, 7, 50, 25, habilidad);
    }
    public static Jefe crearJefe3() {
        Habilidad habilidad = new Habilidad("Pelotazo", "Pelotazo que golpea a todos los objetivos", 35, 65, EfectoFactory.crearAtaqueMultiple());
        return new Jefe("Profesor ED", 280, 120, 8, 70, 30, habilidad);
    }
    public static JefeFinal crearJefeFinal() {
        Habilidad habilidad = new Habilidad("Reglazo general", "Un castigo brutal que lastima a todo el equipo", 40, 50, EfectoFactory.crearAtaqueMultiple());
        return new JefeFinal("Director", 360, 145, 9, 90, 35, habilidad);
    }
}