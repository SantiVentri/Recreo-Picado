package modelo;

public class Arma extends Item {
    private String tipo;
    private int danioBase;

    // Constructor
    public Arma(String nombre, String descripcion, int valor, String tipo, int danioBase, String rutaImagen) {
        super(nombre, descripcion, valor, rutaImagen);
        this.tipo = tipo;
        this.danioBase = danioBase;
    }

    public String getTipo() { return tipo; }
    public int getDanioBase() { return danioBase; }
    
    @Override
    public Arma clonar() {
        return new Arma(nombre, descripcion, valor, tipo, danioBase, rutaImagen);
    }
}
