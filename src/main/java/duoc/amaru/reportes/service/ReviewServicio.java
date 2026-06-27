package duoc.amaru.reportes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import duoc.amaru.reportes.client.ProdClient;
import duoc.amaru.reportes.client.SesionClient;
import duoc.amaru.reportes.dto.ReviewDTO;
import duoc.amaru.reportes.model.Review;
import duoc.amaru.reportes.repository.ReviewRepo;

@Service
public class ReviewServicio {
    @Autowired // Repo Review
    private ReviewRepo reviewRepo;

    @Autowired // Client Producto
    private ProdClient prodClient;

    @Autowired // Client Sesion
    private SesionClient sesionClient;

    // CREAR RESEÑA
    public ResponseEntity<?> crearReview(Long userId, Long prodId, ReviewDTO review) {
        // Validar usuario ejecutor
        sesionClient.validarCliente(userId);

        // Validar producto id
        if (!prodClient.existeProdcuto(prodId))
            return ResponseEntity.badRequest().body("Producto no encontrado");

        if (reviewRepo.existsByIdCliente(userId) && reviewRepo.existsByIdProducto(prodId))
            return ResponseEntity.badRequest().body("Ya creaste una reseña para este producto");

        Review r = new Review();
        r.setIdCliente(userId);
        r.setIdProducto(prodId);
        r.setCalificacion(review.getCalificacion());
        r.setComentario(review.getComentario().strip());
        reviewRepo.save(r);
        return ResponseEntity.ok("Reseña creada y publicada");
    }
    
    // MOSTRAR RESEÑAS
    public List<Review> mostrarTodo() {
        //reviewRepo.findAll();
        return reviewRepo.findAll();
    }

    // MOSTRAR RESEÑAS POR PRODUCTO
    public ResponseEntity<?> filtrarPorProducto(Long prodId) {
        List<Review> reviews = reviewRepo.findAllByIdProducto(prodId);
        if (reviews.isEmpty())
            return ResponseEntity.status(404).body("No hay reseñas para este producto");

        return ResponseEntity.ok(reviews);
    }


    // EDITAR COMENTARIO
    public ResponseEntity<?> editarComentario(String comentario, Long user, Long reviewId) {
        // Validar usuario
        sesionClient.validarCliente(user);

        if (!reviewRepo.existsById(reviewId))
            return ResponseEntity.status(404).body("No se hayó la reseña");

        Review review = reviewRepo.findById(reviewId).orElse(null);
        review.setComentario(comentario.strip());
        reviewRepo.save(review);
        return ResponseEntity.ok("Reseña editada");
    }

    // ELIMINAR RESEÑA
    public ResponseEntity<?> eliminarReview(Long reviewId, Long user) {
        // Validar cliente
        sesionClient.validarCliente(user);

        if (!reviewRepo.existsById(reviewId))
            return ResponseEntity.status(404).body("No se hayó la reseña");

        reviewRepo.deleteById(reviewId);
        return ResponseEntity.ok("Reseña eliminada");
    }

}
