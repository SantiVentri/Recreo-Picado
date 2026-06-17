package modelo;

public class Kiosko {

    // El catálogo de objetos a vender
    private Item[] itemsDisponibles;

    public Kiosko() {
        this.itemsDisponibles = new Item[] {
            // --- POCIONES ---
            ItemFactory.crearMielcita(),
            ItemFactory.crearBaggio(),
            ItemFactory.crearTita(),
            ItemFactory.crearAlfajor(),
            ItemFactory.crearCocacola(),
            ItemFactory.crearManaos(),
            
            // --- ARMAS ---
            ItemFactory.crearLapiz(),
            ItemFactory.crearRegla(),
            ItemFactory.crearCompas(),
            ItemFactory.crearTijera(),
            ItemFactory.crearGomera(),
            ItemFactory.crearLiquidPaper(),
            ItemFactory.crearBeyblade(),
            
            // --- ARMADURAS ---
            ItemFactory.crearGuantes(),
            ItemFactory.crearTopper(),
            ItemFactory.crearBotines(),
            ItemFactory.crearGuardapolvo(),
            ItemFactory.crearCamisetaDyJ(),
            ItemFactory.crearCamisetaSeleccion(),
            ItemFactory.crearCamperaEgresados()
        };
    }

    public Item[] getItemsDisponibles() {
        return itemsDisponibles;
    }

    public boolean comprarItem(Item item, Equipo equipo) {
        int costoTotal = item.getValor();

        // Valida si hay plata suficiente
        if (Repositorio.getInstance().getPartidaActual().getPesos() >= costoTotal) {
            
            // Descuenta los pesos de la partida
        	Repositorio.getInstance().getPartidaActual().quitarPesos(costoTotal);
            
            // Agrega el ítem al inventario de la partida
            Repositorio.getInstance().getPartidaActual().agregarItem(item);
            
            // Compra exitosa
            return true;
        }

        //Fondos insuficientes
        return false;
    }
}