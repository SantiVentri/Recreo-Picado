package modelo;

import java.io.Serializable;

public abstract class Item implements Serializable {
    private static final long serialVersionUID = 1L;

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
    public abstract Item clonar();
}
