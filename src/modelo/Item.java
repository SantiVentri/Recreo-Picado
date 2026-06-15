package modelo;

public abstract class Item {
    protected String nombre;
    protected String descripcion;
    protected int valor; 

    public Item(String nombre, String descripcion, int valor) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.valor = valor;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getValor() { return valor; }
    
    public abstract void usar();
}
