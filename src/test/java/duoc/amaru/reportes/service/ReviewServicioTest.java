package duoc.amaru.reportes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import duoc.amaru.reportes.model.Review;
import duoc.amaru.reportes.repository.ReviewRepo;

public class ReviewServicioTest {
    @Mock
    private ReviewRepo reviewRepo;

    @InjectMocks
    private ReviewServicio reviewServicio;
    
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMostrarTodoLleno() {
        // Preparacion
        Review r1 = new Review(1L, 1L, 1L, 5, "Muy buen jabon", LocalDate.now());
        Review r2 = new Review(2L, 1L, 2L, 1, "Pesimo producto", LocalDate.now());

        List<Review> reviews = new ArrayList<>();
        reviews.add(r1);
        reviews.add(r2);

        
        // Configuracion
        when(reviewRepo.findAll()).thenReturn(reviews);

        // Testeo
        List<Review> resultado = reviewServicio.mostrarTodo();

        // Verificacion
        assertEquals(resultado.size(), 2);
        assertEquals(resultado.get(0).getIdReview(), 1L);
        assertEquals(resultado.get(1).getIdReview(), 2L);

        verify(reviewRepo, times(1)).findAll();
    }

    @Test
    void testMostrarTodoVacio() {
        // Preparacion
        List<Review> reviews = new ArrayList<>();

        // Configuracion
        when(reviewRepo.findAll()).thenReturn(reviews);

        // Testeo
        List<Review> resultado = reviewServicio.mostrarTodo();

        // Verificar
        assertEquals(resultado.size(), 0);

        verify(reviewRepo, times(1)).findAll();
    }
}
