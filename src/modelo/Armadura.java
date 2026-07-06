package modelo;

public class Armadura extends Item {
    private String tipo;
    private int vidaBonus;    
    private int defensaBonus; 

    // Constructor 
    public Armadura(String nombre, String descripcion, int valor, String tipo, int vidaBonus, int defensaBonus, String rutaImagen) {
        super(nombre, descripcion, valor, rutaImagen);
        this.tipo = tipo;
        this.vidaBonus = vidaBonus;
        this.defensaBonus = defensaBonus;
    }

    public String getTipo() { return tipo; }
    public int getVidaBonus() { return vidaBonus; }
    public int getDefensaBonus() { return defensaBonus; }
    
    @Override
    public Armadura clonar() {
        return new Armadura(nombre, descripcion, valor, tipo, vidaBonus, defensaBonus, rutaImagen);
    }
}
