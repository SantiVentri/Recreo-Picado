package modelo.factories;
import enums.EFECTOS;
import modelo.Arma;
import modelo.Armadura;
import modelo.Pocion;

public class ItemFactory {
    
    // ==========================================
    // POCIONES (Curación y Maná)
    // ==========================================
	// Nombre, Descripción, precio, cantidad curación, cantidad energía, efecto/tipo, imagen 
    
    public static Pocion crearMielcita() {
        return new Pocion("Mielcita", "Pequeño subidón de azúcar", 15, 0, 10, EFECTOS.CURACION, "src/resources/items/mielcita.png");
    }

    public static Pocion crearBaggio() {
        return new Pocion("Baggio", "Curación ligera de vida", 30, 40, 0, EFECTOS.CURACION, "src/resources/items/baggio.png");
    }

    public static Pocion crearTita() {
        return new Pocion("Tita", "Un clásico que levanta el ánimo", 40, 50, 0, EFECTOS.CURACION, "src/resources/items/Tita.png");
    }

    public static Pocion crearAlfajor() {
        return new Pocion("Alfajor", "Gran curación de vida", 60, 75, 0, EFECTOS.CURACION, "src/resources/items/alfajor.png");
    }
    
    public static Pocion crearCocacola() {
        return new Pocion("Coca-Cola", "Recupera vida y da energía", 80, -10, 40, EFECTOS.CURACION, "src/resources/items/cocacola.png");
    }

    public static Pocion crearManaos() {
        return new Pocion("Manaos", "Restauración total, pero de dudosa procedencia", 120, -40, 60, EFECTOS.CURACION, "src/resources/items/manaos.png");
    }

    // ==========================================
    // ARMAS (Escalado de daño y tipos corregidos)
    // ==========================================

    public static Arma crearLapiz() {
        return new Arma("Lápiz", "Arma punzante básica", 25, "Cuerpo a cuerpo", 8, "src/resources/items/lapiz.png");
    }

    public static Arma crearRegla() {
        return new Arma("Regla", "Ideal para dar azotes en las manos", 45, "Cuerpo a cuerpo", 14, "src/resources/items/regla.png");
    }

    public static Arma crearCompas() {
        return new Arma("Compás", "Peligrosamente afilado", 70, "Cuerpo a cuerpo", 20, "src/resources/items/compas.png");
    }

    public static Arma crearTijera() {
        return new Arma("Tijera", "No corras con ella en la mano", 95, "Cuerpo a cuerpo", 26, "src/resources/items/tijera.png");
    }

    public static Arma crearGomera() {
        return new Arma("Gomera", "Arma a distancia letal", 120, "Distancia", 30, "src/resources/items/gomera.png");
    }
    
    public static Arma crearLiquidPaper() {
        // Asumiendo que se puede arrojar o rociar
        return new Arma("Liquid Paper", "Ciega a los enemigos temporalmente", 150, "Distancia", 35, "src/resources/items/liquid-paper.png");
    }

    public static Arma crearBeyblade() {
        return new Arma("Beyblade", "Let it rip! Daño constante", 200, "Cuerpo a cuerpo", 45, "src/resources/items/beyblade.png");
    }

    // ==========================================
    // ARMADURAS (Piezas lógicas y balance de defensa)
    // ==========================================

    public static Armadura crearGuantes() {
        return new Armadura("Guantes de lana", "Protegen un poco del frío y de golpes", 30, "Manos", 0, 5, 0, "src/resources/items/guantes.png");
    }

    public static Armadura crearTopper() {
        // "Topper" lógicamente es calzado, se cambia "Torso" por "Pies"
        return new Armadura("Zapatillas Topper", "Comodidad para correr más rápido", 60, "Pies", 10, 8, 5, "src/resources/items/topper.png");
    }

    public static Armadura crearBotines() {
        return new Armadura("Botines con tapones", "Patadas más fuertes y protección", 110, "Pies", 15, 12, 20, "src/resources/items/botines.png");
    }

    public static Armadura crearGuardapolvo() {
        return new Armadura("Guardapolvo Blanco", "Resiste manchas de tinta y golpes leves", 100, "Torso", 25, 15, 0, "src/resources/items/guardapolvo.png");
    }
    
    public static Armadura crearCamisetaDyJ() {
        return new Armadura("Camiseta D&J", "Te da la fuerza de Varela", 150, "Torso", 40, 20, 0, "src/resources/items/camiseta-dyj.png");
    }
    
    public static Armadura crearCamisetaSeleccion() {
        return new Armadura("Camiseta de la Selección", "Tiene 3 estrellas, defensa legendaria", 250, "Torso", 60, 35, 0, "src/resources/items/camiseta-seleccion.png");
    }

    public static Armadura crearCamperaEgresados() {
        return new Armadura("Campera de Egresados", "Armadura definitiva inquebrantable", 500, "Torso", 100, 60, -10, "src/resources/items/campera-egresados.png");
    }
}
