package modelo;
import enums.EFECTOS; 

public class Pocion extends Item {
    private int cantidadCuracion;
    private int cantidadMana; 
    private EFECTOS efecto; 

    public Pocion(String nombre, String descripcion, int valor, int cantidadCuracion, int cantidadMana, EFECTOS efecto) {
        super(nombre, descripcion, valor);
        this.cantidadCuracion = cantidadCuracion;
        this.cantidadMana = cantidadMana;
        this.efecto = efecto;
    }

    @Override
    public void usar() {
        System.out.println("Usando poción: " + nombre);
    }

    public int getCantidadCuracion() { return cantidadCuracion; }
    public int getCantidadMana() { return cantidadMana; }
    public EFECTOS getEfecto() { return efecto; }
}
