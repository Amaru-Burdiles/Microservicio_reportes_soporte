package duoc.amaru.reportes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duoc.amaru.reportes.dto.ReviewDTO;
import duoc.amaru.reportes.service.ReviewServicio;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewControlador {
    @Autowired
    private ReviewServicio reviewServicio;

    // MOSTRAR RESEÑAS
    @GetMapping
    public ResponseEntity<?> getReviews() {
        return reviewServicio.mostrarTodo();
    }
    
    // MOSTRAR RESEÑAS POR PRODUCTO
    @GetMapping("/producto:{prodId}")
    public ResponseEntity<?> getReviewsByProducto(@RequestParam Long prodId) {
        return reviewServicio.filtrarPorProducto(prodId);
    }
    
    // CREAR RESEÑA
    @PostMapping("/producto:{prodId}/user:{userId}")
    public ResponseEntity<?> postReview(@Valid @RequestBody ReviewDTO r, @PathVariable Long prod, @PathVariable Long user) {
        return reviewServicio.crearReview(user, prod, r);
    }
    
    // CAMBIAR COMENTARIO
    @PutMapping("/editar/review:{id}/{userId}")
    public ResponseEntity<?> putComentarioReview(@PathVariable Long id, @PathVariable Long userId, @RequestBody String comentario) {
        return reviewServicio.editarComentario(comentario, userId, id);
    }

    // ELIMINAR RESEÑA
    @DeleteMapping("/eliminar:{reviewId}/userId")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId, @PathVariable Long userId) {
        return reviewServicio.eliminarReview(reviewId, userId);
    }
}
