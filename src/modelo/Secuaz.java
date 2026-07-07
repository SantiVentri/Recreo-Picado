package modelo;

import java.util.List;

import interfaces.IEntidad;

public class Secuaz extends Entidad {

    public Secuaz(String nombre, int vidaMax, int energiaMax, int velocidad, int ataque, int defensa, Habilidad habilidad) {
        super(nombre, vidaMax, energiaMax, velocidad, ataque, defensa, habilidad);
    }

    @Override
    public void usarHabilidad(IEntidad objetivo, List<Entidad> alumnos, List<Entidad> enemigos) { }
}