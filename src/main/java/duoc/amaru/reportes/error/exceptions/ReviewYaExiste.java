package duoc.amaru.reportes.error.exceptions;

public class ReviewYaExiste extends RuntimeException {
    public ReviewYaExiste() {
        super("Ya creaste una reseña para este producto");
    }
}
