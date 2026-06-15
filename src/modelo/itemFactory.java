package modelo;
import enums.EFECTOS;

public class itemFactory {
    
    public static Pocion crearAlfajor() {
        return new Pocion("Alfajor", "Gran curación de vida", 50, 60, 0, EFECTOS.CURACION);
    }
    
    public static Pocion crearManaos() {
        return new Pocion("Manaos", "Recupera vida pero dudosa", 100, 100, 20, EFECTOS.CURACION);
    }
    
    public static Arma crearGomera() {
        return new Arma("Gomera", "Arma a distancia letal", 120, "Distancia", 25);
    }
    
    public static Armadura crearGuardapolvo() {
        return new Armadura("Guardapolvo Blanco", "Resiste manchas y golpes", 100, "Torso", 25, 15);
    }
    
    public static Armadura crearCamperaEgresados() {
        return new Armadura("Campera de Egresados", "Armadura definitiva", 400, "Torso", 100, 50);
    }
}
