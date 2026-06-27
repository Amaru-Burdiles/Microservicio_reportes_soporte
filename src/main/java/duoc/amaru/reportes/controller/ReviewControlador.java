package duoc.amaru.reportes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duoc.amaru.reportes.dto.ReviewDTO;
import duoc.amaru.reportes.model.Review;
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
        List<Review> resultado = reviewServicio.mostrarTodo();
        if (resultado.isEmpty())
            return ResponseEntity.status(404).body("No hay reseñas registradas");
        
        return ResponseEntity.ok(resultado);
    }
    
    // MOSTRAR RESEÑAS POR PRODUCTO
    @GetMapping("/producto:{prodId}")
    public ResponseEntity<?> getReviewsByProducto(@RequestParam Long prodId) {
        List<Review> reply = reviewServicio.filtrarPorProducto(prodId);
        if (reply.isEmpty())
            return ResponseEntity.status(404).body("No hay reseñas para este producto");

        return ResponseEntity.ok(reply);
    }
    
    // CREAR RESEÑA
    @PostMapping("/producto:{prodId}/user:{userId}")
    public ResponseEntity<?> postReview(@Valid @RequestBody ReviewDTO r, @PathVariable Long prod, @PathVariable Long user) {
        Review reply = reviewServicio.crearReview(user, prod, r);
        return ResponseEntity.ok("Reseña creada y publicada!\n"+ reply);
    }
    
    // CAMBIAR COMENTARIO
    @PutMapping("/editar/review:{id}/{userId}")
    public ResponseEntity<?> putComentarioReview(@PathVariable Long id, @PathVariable Long userId, @RequestBody String comentario) {
        Review r = reviewServicio.editarComentario(comentario, userId, id);
        if (r == null)
            return ResponseEntity.status(404).body("No se hayó la reseña");

        String reply = "Reseña editada!\n"+ "Calificación: "+ r.getCalificacion() +"\nComentario: "+ r.getComentario();
        return ResponseEntity.ok(reply);
    }

    // ELIMINAR RESEÑA
    @DeleteMapping("/eliminar:{reviewId}/userId")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId, @PathVariable Long userId) {
        boolean reply = reviewServicio.eliminarReview(reviewId, userId);
        if (reply)
            return ResponseEntity.ok("Reseña eliminada");
        
        return ResponseEntity.status(404).body("No se hayó la reseña");
    }
}
