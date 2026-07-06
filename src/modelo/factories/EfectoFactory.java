package modelo.factories;

import enums.EFECTOS;
import modelo.Efecto;

public class EfectoFactory {
    public static Efecto crearVeneno() {
        return new Efecto("Veneno", "Envenena al objetivo, quitándole vida durante 3 turnos", EFECTOS.VENENO, 3, 8);
    }
    
    public static Efecto crearCuracion() {
        return new Efecto("Curación", "Regenera vida al objetivo al 85%", EFECTOS.CURACION, 1, 85);
    }
    
    public static Efecto crearRegenerarEnergia() {
        return new Efecto("Regenerar Energía", "Recupera energía al objetivo al 100%", EFECTOS.REGENERAR_ENERGIA, 1, 100);
    }
    
    public static Efecto crearAtaqueMultiple() {
        return new Efecto("Ataque Múltiple", "El ataque impacta a todos los enemigos simultáneamente", EFECTOS.ATAQUE_MULTIPLE, 1, 0);
    }
}
