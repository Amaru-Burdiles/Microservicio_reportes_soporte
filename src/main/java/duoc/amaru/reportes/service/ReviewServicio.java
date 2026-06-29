package duoc.amaru.reportes.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import duoc.amaru.reportes.client.ProdClient;
import duoc.amaru.reportes.client.SesionClient;
import duoc.amaru.reportes.dto.ReviewDTO;
import duoc.amaru.reportes.error.exceptions.ProdNoExiste;
import duoc.amaru.reportes.error.exceptions.ReviewYaExiste;
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
    public Review crearReview(Long userId, Long prodId, ReviewDTO review) {
        // Validar usuario ejecutor
        sesionClient.validarCliente(userId);

        // Validar producto id
        if (!prodClient.existeProdcuto(prodId))
            throw new ProdNoExiste();

        // Validar reseña duplicada
        if (reviewRepo.existsByIdCliente(userId) && reviewRepo.existsByIdProducto(prodId))
            throw new ReviewYaExiste();

        // Creacion de la reseña
        Review r = new Review();
        r.setIdCliente(userId);
        r.setIdProducto(prodId);
        r.setCalificacion(review.getCalificacion());
        r.setComentario(review.getComentario().strip());
        r.setFecha(LocalDate.now());

        // Guardar y respuesta a controlador
        return reviewRepo.save(r);
    }
    
    // MOSTRAR RESEÑAS
    public List<Review> mostrarTodo() {
        return reviewRepo.findAll();
    }

    // MOSTRAR RESEÑAS POR PRODUCTO
    public List<Review> filtrarPorProducto(Long prodId) {
        return reviewRepo.findAllByIdProducto(prodId);
    }


    // EDITAR COMENTARIO
    public Review editarComentario(String comentario, Long user, Long reviewId) {
        // Validar usuario
        sesionClient.validarCliente(user);

        // Validar reseña existe
        if (!reviewRepo.existsById(reviewId))
            return null;

        // Edición de reseña
        Review review = reviewRepo.findById(reviewId).orElse(null);
        review.setComentario(comentario.strip());
        return reviewRepo.save(review);
    }

    // ELIMINAR RESEÑA
    public boolean eliminarReview(Long reviewId, Long user) {
        // Validar cliente
        sesionClient.validarCliente(user);

        if (!reviewRepo.existsById(reviewId))
            return false;

        reviewRepo.deleteById(reviewId);
        return true;
    }

}
