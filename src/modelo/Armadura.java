package modelo;

public class Armadura extends Item {
    private String tipo;
    private int vidaBonus;    
    private int defensaBonus; 
    private int velocidadBonus;

    // Constructor 
    public Armadura(String nombre, String descripcion, int valor, String tipo, int vidaBonus, int defensaBonus, int velocidadBonus, String rutaImagen) {
        super(nombre, descripcion, valor, rutaImagen);
        this.tipo = tipo;
        this.vidaBonus = vidaBonus;
        this.defensaBonus = defensaBonus;
        this.velocidadBonus = velocidadBonus;
    }

    public String getTipo() { return tipo; }
    public int getVidaBonus() { return vidaBonus; }
    public int getDefensaBonus() { return defensaBonus; }
    public int getVelocidadBonus() { return velocidadBonus; }
    
    @Override
    public Armadura clonar() {
        return new Armadura(nombre, descripcion, valor, tipo, vidaBonus, defensaBonus, velocidadBonus, rutaImagen);
    }
}
