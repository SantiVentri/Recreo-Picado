package modelo;

import java.io.Serializable;

public class Habilidad implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String descripcion;
    private int costoEnergia;   // energía que consume al usarla
    private int potencia;       // daño o curación base que produce
    private Efecto efecto;      // efecto de estado que puede aplicar (puede ser null)

    public Habilidad(String nombre, String descripcion, int costoEnergia, int potencia) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costoEnergia = costoEnergia;
        this.potencia = potencia;
        this.efecto = null;
    }

    public Habilidad(String nombre, String descripcion, int costoEnergia, int potencia, Efecto efecto) {
        this(nombre, descripcion, costoEnergia, potencia);
        this.efecto = efecto;
    }

    /**
     * Indica si la entidad tiene suficiente energía para usar esta habilidad.
     */
    public boolean sePuedeUsar(Entidad entidad) {
        return entidad.getEnergia() >= costoEnergia;
    }
    
    public boolean esHabilidadMultiple() {
        return this.efecto != null && this.efecto.getTipo() == enums.EFECTOS.ATAQUE_MULTIPLE;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getCostoEnergia() { return costoEnergia; }
    public int getPotencia() { return potencia; }
    public Efecto getEfecto() { return efecto; }
    public void setEfecto(Efecto efecto) { this.efecto = efecto; }
}