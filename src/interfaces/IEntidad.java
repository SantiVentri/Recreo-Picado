package interfaces;

import modelo.*;

import views.EntidadView;

public interface IEntidad {
	void recibirDaño(int daño);
	boolean estaVivo();
	
	void realizarAtaque(IEntidad objetivo);
	void realizarDefensa();
	void usarHabilidad(IEntidad objetivo);
	void usarItem(Item item);
	void agregarItem(Item item);
	void quitarItem(Item item);
	void aplicarEfecto(Efecto efecto);
	void quitarEfecto(Efecto efecto);
	void aumentarVida(int cantidad);
	void quitarVida(int cantidad);	
	void recibirXp(int xp);	
	
	EntidadView toView();
}
