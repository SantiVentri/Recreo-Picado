package modelo.factories;

import modelo.Arma;
import modelo.Armadura;
import modelo.Pocion;
import modelo.Recompensa;
import modelo.Repositorio;

public class RecompensaFactory {
	private static Pocion pocionAleatoria() {
	    int i = (int) (Math.random() * 6);
	    return switch (i) {
	        case 0 -> ItemFactory.crearMielcita();
	        case 1 -> ItemFactory.crearBaggio();
	        case 2 -> ItemFactory.crearTita();
	        case 3 -> ItemFactory.crearAlfajor();
	        case 4 -> ItemFactory.crearCocacola();
	        default -> ItemFactory.crearManaos();
	    };
	}

	private static Arma armaAleatoria() {
	    int i = (int) (Math.random() * 7);
	    return switch (i) {
	        case 0 -> ItemFactory.crearLapiz();
	        case 1 -> ItemFactory.crearRegla();
	        case 2 -> ItemFactory.crearCompas();
	        case 3 -> ItemFactory.crearTijera();
	        case 4 -> ItemFactory.crearGomera();
	        case 5 -> ItemFactory.crearLiquidPaper();
	        default -> ItemFactory.crearBeyblade();
	    };
	}

	private static Armadura armaduraAleatoria() {
		int i = (int) (Math.random() * 7);
	    return switch (i) {
	        case 0 -> ItemFactory.crearGuantes();
	        case 1 -> ItemFactory.crearTopper();
	        case 2 -> ItemFactory.crearBotines();
	        case 3 -> ItemFactory.crearGuardapolvo();
	        case 4 -> ItemFactory.crearCamisetaDyJ();
	        case 5 -> ItemFactory.crearCamisetaSeleccion();
	        default -> ItemFactory.crearCamperaEgresados();
	    };
	}
	
	public static Recompensa crearRecompensa1() {
	    return new Recompensa(250, 400, pocionAleatoria(), armaAleatoria(), armaduraAleatoria());
	}

	public static Recompensa crearRecompensa2() {
	    return new Recompensa(320, 1000, pocionAleatoria(), armaAleatoria(), armaduraAleatoria());
	}

	public static Recompensa crearRecompensa3() {
	    return new Recompensa(450, 2000, pocionAleatoria(), armaAleatoria(), armaduraAleatoria());
	}

	public static Recompensa crearRecompensa4() {
	    return new Recompensa(700, 4000, pocionAleatoria(), armaAleatoria(), armaduraAleatoria());
	}


	public static Recompensa obtenerRecompensaPorBatalla(modelo.Batalla batalla) {
		java.util.List<modelo.Batalla> batallas = Repositorio.getInstance().getPartidaActual().getBatallas();
		int indice = batallas.indexOf(batalla);
		
		return switch (indice) {
			case 0 -> crearRecompensa1();
			case 1 -> crearRecompensa2();
			case 2 -> crearRecompensa3();
			case 3 -> crearRecompensa4();
			default -> crearRecompensa1();
		};
	}
}
