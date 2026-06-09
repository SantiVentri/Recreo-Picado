package modelo;

import enums.EFECTOS;

public class PersonajeFactory {
    public static Mago crearMago() {
        Habilidad habilidad = new Habilidad("Lanzar Gogo", "Lanza gogos mágicos a todos los enemigos", 25, 22, EFECTOS.ATAQUE_MULTIPLE);
        return new Mago("Mago", 80, 150, 8, 8, 3, habilidad, 18);
    }
    
    public static Arquero crearArquero() {
        Habilidad habilidad = new Habilidad("Bola de papel", "Lanza una bola de papel con saliva que enferma al rival", 15, 20, EFECTOS.VENENO);
        return new Arquero("Arquero", 90, 110, 9, 12, 4, habilidad, 18);
    }

    public static Curandera crearCurandera() {
        Habilidad habilidad = new Habilidad("Curación", "Usa el turno para curar a un objetivo de su mismo equipo", 20, 28, EFECTOS.CURACION);
        return new Curandera("Curandera", 95, 120, 7, 6, 5, habilidad, 22);
    }

    public static Guerrero crearGuerrero() {
        Habilidad habilidad = new Habilidad("Golpe Aplastante", "Lastima a todos los enemigos que tenga cerca", 20, 25, EFECTOS.ATAQUE_MULTIPLE);
        return new Guerrero("Guerrero", 120, 100, 5, 14, 8, habilidad, 20);
    }
}