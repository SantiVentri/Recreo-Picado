package modelo;

import enums.EFECTOS;

public class Pocion extends Item {
    private int cantidadCuracion;
    private int cantidadEnergia; 
    private EFECTOS efecto;

    // Constructor
    public Pocion(String nombre, String descripcion, int valor, int cantidadCuracion, int cantidadEnergia, EFECTOS efecto, String rutaImagen) {
        super(nombre, descripcion, valor, rutaImagen);
        this.cantidadCuracion = cantidadCuracion;
        this.cantidadEnergia = cantidadEnergia;
        this.efecto = efecto;
    }

    public int getCantidadCuracion() { return cantidadCuracion; }
    public int getCantidadEnergia() { return cantidadEnergia; }
    public EFECTOS getEfecto() { return efecto; }
    
    @Override
    public Pocion clonar() {
        return new Pocion(nombre, descripcion, valor, cantidadCuracion, cantidadEnergia, efecto, rutaImagen);
    }
}

