package modelo;

public abstract class Item {
    protected String nombre;
    protected String descripcion;
    protected int valor; 
    protected String rutaImagen;
    private Entidad equipadoPor;

    public Item(String nombre, String descripcion, int valor, String rutaImagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.valor = valor;
        this.rutaImagen = rutaImagen;
        this.equipadoPor = null;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getValor() { return valor; }
    public String getRutaImagen() { return rutaImagen; } 
    public Entidad getEquipadoPor() { return equipadoPor; }
    public void setEquipadoPor(Entidad equipadoPor) {
    	this.equipadoPor = equipadoPor;
    }
    public abstract void usar();
}
