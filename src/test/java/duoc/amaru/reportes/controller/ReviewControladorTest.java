package duoc.amaru.reportes.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import duoc.amaru.reportes.dto.ReviewDTO;
import duoc.amaru.reportes.model.Review;
import duoc.amaru.reportes.service.ReviewServicio;

public class ReviewControladorTest {

    @Mock
    private ReviewServicio reviewServicio;

    @InjectMocks
    private ReviewControlador reviewControlador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getReviews_Vacio() {
        when(reviewServicio.mostrarTodo()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = reviewControlador.getReviews();

        assertEquals(404, response.getStatusCode().value());
        assertEquals("No hay reseñas registradas", response.getBody());
    }

    @Test
    void getReviews_ConDatos() {
        Review r = new Review();
        when(reviewServicio.mostrarTodo()).thenReturn(List.of(r));

        ResponseEntity<?> response = reviewControlador.getReviews();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(List.of(r), response.getBody());
    }

    @Test
    void getReviewsByProducto_Vacio() {
        when(reviewServicio.filtrarPorProducto(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = reviewControlador.getReviewsByProducto(1L);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("No hay reseñas para este producto", response.getBody());
    }

    @Test
    void getReviewsByProducto_ConDatos() {
        Review r = new Review();
        when(reviewServicio.filtrarPorProducto(1L)).thenReturn(List.of(r));

        ResponseEntity<?> response = reviewControlador.getReviewsByProducto(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(List.of(r), response.getBody());
    }

    @Test
    void postReview_Exito() {
        ReviewDTO dto = new ReviewDTO();
        Review r = new Review();
        r.setCalificacion(5);
        r.setComentario("Excelente");
        
        when(reviewServicio.crearReview(2L, 1L, dto)).thenReturn(r);

        ResponseEntity<?> response = reviewControlador.postReview(dto, 1L, 2L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Reseña creada y publicada!\n" + r, response.getBody());
    }

    @Test
    void putComentarioReview_Exito() {
        Review r = new Review();
        r.setCalificacion(5);
        r.setComentario("Excelente");
        
        when(reviewServicio.editarComentario("Excelente", 2L, 1L)).thenReturn(r);

        ResponseEntity<?> response = reviewControlador.putComentarioReview(1L, 2L, "Excelente");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Reseña editada!\nCalificación: 5\nComentario: Excelente", response.getBody());
    }

    @Test
    void putComentarioReview_Fallo() {
        when(reviewServicio.editarComentario("Malo", 2L, 1L)).thenReturn(null);

        ResponseEntity<?> response = reviewControlador.putComentarioReview(1L, 2L, "Malo");

        assertEquals(404, response.getStatusCode().value());
        assertEquals("No se hayó la reseña", response.getBody());
    }

    @Test
    void deleteReview_Exito() {
        when(reviewServicio.eliminarReview(1L, 2L)).thenReturn(true);

        ResponseEntity<?> response = reviewControlador.deleteReview(1L, 2L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Reseña eliminada", response.getBody());
    }

    @Test
    void deleteReview_Fallo() {
        when(reviewServicio.eliminarReview(1L, 2L)).thenReturn(false);

        ResponseEntity<?> response = reviewControlador.deleteReview(1L, 2L);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("No se hayó la reseña", response.getBody());
    }
}