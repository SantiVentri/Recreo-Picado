package modelo;

public class Efecto {

    public enum TipoEfecto {
        VENENO,           // quita vida turno a turno
        ATAQUE_MULTIPLE,  // ataca a todos los enemigos
        CURACION,         // regenera un % de la vida máxima
        REGENERAR_ENERGIA // regenera un % de la energía máxima
    }

    private String nombre;
    private String descripcion;
    private TipoEfecto tipo;
    private int duracion;   // turnos que dura (relevante para VENENO)
    private int magnitud;   // % o valor fijo según el tipo

    public Efecto(String nombre, String descripcion, TipoEfecto tipo, int duracion, int magnitud) {
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
                entidad.quitarVida(dano);
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
                // El Orquestador es responsable de repartir el daño a todos los enemigos.
                break;
        }

        duracion--;
        return duracion > 0;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public TipoEfecto getTipo() { return tipo; }
    public int getDuracion() { return duracion; }
    public int getMagnitud() { return magnitud; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    public void setMagnitud(int magnitud) { this.magnitud = magnitud; }
}