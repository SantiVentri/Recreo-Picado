package modelo;

public class Armadura extends Item {
    private String tipo;
    private int vidaBonus;    
    private int defensaBonus; 

    public Armadura(String nombre, String descripcion, int valor, String tipo, int vidaBonus, int defensaBonus) {
        super(nombre, descripcion, valor);
        this.tipo = tipo;
        this.vidaBonus = vidaBonus;
        this.defensaBonus = defensaBonus;
    }

    @Override
    public void usar() {
        System.out.println("Equipando armadura: " + nombre);
    }

    public String getTipo() { return tipo; }
    public int getVidaBonus() { return vidaBonus; }
    public int getDefensaBonus() { return defensaBonus; }
}
