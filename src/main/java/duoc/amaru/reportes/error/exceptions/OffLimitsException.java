package duoc.amaru.reportes.error.exceptions;

public class OffLimitsException extends RuntimeException {
    public OffLimitsException() {
        super("No tienes permisos suficientes para ver este contenido.");
    }
}
