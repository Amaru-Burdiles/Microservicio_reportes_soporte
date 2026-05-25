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
    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private ProdClient prodClient;

    @Autowired
    private SesionClient sesionClient;

    // MOSTRAR RESEÑAS
    public ResponseEntity<?> mostrarTodo() {
        List<Review> reviews = reviewRepo.findAll();
        if (reviews.isEmpty())
            return ResponseEntity.status(404).body("No hay reseñas registradas");
        
        return ResponseEntity.ok(reviews);
    }

    // MOSTRAR RESEÑAS POR PRODUCTO
    public ResponseEntity<?> filtrarPorProducto(Long prodId) {
        List<Review> reviews = reviewRepo.findAllByIdProducto(prodId);
        if (reviews.isEmpty())
            return ResponseEntity.status(404).body("No hay reseñas para este producto");

        return ResponseEntity.ok(reviews);
    }

    // CREAR RESEÑA
    public ResponseEntity<?> crearReview(Long userId, Long prodId, ReviewDTO review) {
        // Validar usuario ejecutor
        ResponseEntity<?> reply = sesionClient.validarCliente(userId);
        if (reply != null)
            return reply;

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

    // EDITAR COMENTARIO
    public ResponseEntity<?> editarComentario(String comentario, Long user, Long reviewId) {
        // Validar usuario
        ResponseEntity<?> reply = sesionClient.validarCliente(user);
        if (reply != null)
            return reply;

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
        ResponseEntity<?> reply = sesionClient.validarCliente(user);
        if (reply != null)
            return reply;

        if (!reviewRepo.existsById(reviewId))
            return ResponseEntity.status(404).body("No se hayó la reseña");

        reviewRepo.deleteById(reviewId);
        return ResponseEntity.ok("Reseña eliminada");
    }

}
