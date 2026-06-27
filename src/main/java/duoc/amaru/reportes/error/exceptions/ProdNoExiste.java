package duoc.amaru.reportes.error.exceptions;

public class ProdNoExiste extends RuntimeException {
    public ProdNoExiste() {
        super("Producto no encontrado");
    }
}
