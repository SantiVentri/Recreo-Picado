package modelo;

import enums.EFECTOS;

public class Efecto {
    private String nombre;
    private String descripcion;
    private EFECTOS tipo;
    private int duracion;   // turnos que dura (relevante para VENENO)
    private int magnitud;   // % o valor fijo según el tipo

    public Efecto(String nombre, String descripcion, EFECTOS tipo, int duracion, int magnitud) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.duracion = duracion;
        this.magnitud = magnitud;
    }

    /**
     * Aplica el efecto sobre una entidad en cada turno.
     * Retorna true si el efecto sigue activo, false si ya expiró.
     * 
     * VENENO: resta (magnitud% de vidaMax) por turno.
     * CURACION: suma (magnitud% de vidaMax) una vez.
     * REGENERAR_ENERGIA: suma (magnitud% de energiaMax) una vez.

     */
    public boolean aplicarPorTurno(Entidad entidad) {
        if (duracion <= 0) return false;

        switch (tipo) {
        	case VENENO:
        		int dano = Math.max(1, entidad.getVidaMax() * magnitud / 100);
        		entidad.recibirDanoVeneno(dano);
        		break;
            case CURACION:
                int curacion = Math.max(1, entidad.getVidaMax() * magnitud / 100);
                entidad.aumentarVida(curacion);
                break;
            case REGENERAR_ENERGIA:
                int energia = Math.max(1, entidad.getEnergiaMax() * magnitud / 100);
                entidad.setEnergia(Math.min(entidad.getEnergiaMax(), entidad.getEnergia() + energia));
                break;
            case ATAQUE_MULTIPLE:
                // Se ejecuta directamente en usarHabilidad(), no por turno.
                break;
        }

        duracion--;
        return duracion > 0;
    }
    
    public Efecto copiar() {
    	return new Efecto(nombre, descripcion, tipo, duracion, magnitud);
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public EFECTOS getTipo() { return tipo; }
    public int getDuracion() { return duracion; }
    public int getMagnitud() { return magnitud; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    public void setMagnitud(int magnitud) { this.magnitud = magnitud; }
}