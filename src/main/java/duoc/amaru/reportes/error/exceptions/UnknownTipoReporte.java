package duoc.amaru.reportes.error.exceptions;

public class UnknownTipoReporte extends RuntimeException {
    public UnknownTipoReporte() {
        super("Tipo de reporte desconocido");
    }
}
