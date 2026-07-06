package interfaces;

import modelo.*;

public interface IEntidad {
	void recibirDano(int daño);
	boolean estaVivo();
	
	void realizarAtaque(IEntidad objetivo);
	void realizarDefensa();
	void usarHabilidad(IEntidad objetivo);
	void usarItem(Pocion item);
	void equiparArma(Arma arma);
	void desequiparArma();
	void equiparArmadura(Armadura armadura);
	void desequiparArmadura();
	void aplicarEfecto(Efecto efecto);
	void quitarEfecto(Efecto efecto);
	void aumentarVida(int cantidad);
	void quitarVida(int cantidad);
	void aumentarEnergia();
	void recibirDanoVeneno(int cantidad); 
	void procesarEfectos();
	void resetearParaBatalla();
}
