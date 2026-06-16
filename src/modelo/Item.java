package modelo;

public abstract class Item {
    protected String nombre;
    protected String descripcion;
    protected int valor; 
    protected String rutaImagen;

    public Item(String nombre, String descripcion, int valor, String rutaImagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.valor = valor;
        this.rutaImagen = rutaImagen;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getValor() { return valor; }
    public String getRutaImagen() { return rutaImagen; } 
    
    public abstract void usar();
}
