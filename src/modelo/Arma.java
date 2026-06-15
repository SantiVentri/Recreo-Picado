package modelo;

public class Arma extends Item {
    private String tipo;
    private int danioBase; 

    public Arma(String nombre, String descripcion, int valor, String tipo, int danioBase) {
        super(nombre, descripcion, valor);
        this.tipo = tipo;
        this.danioBase = danioBase;
    }

    @Override
    public void usar() {
        System.out.println("Equipando arma: " + nombre);
    }

    public String getTipo() { return tipo; }
    public int getDanioBase() { return danioBase; }
}