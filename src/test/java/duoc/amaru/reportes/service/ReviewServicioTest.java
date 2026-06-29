package duoc.amaru.reportes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import duoc.amaru.reportes.client.ProdClient;
import duoc.amaru.reportes.client.SesionClient;
import duoc.amaru.reportes.dto.ReviewDTO;
import duoc.amaru.reportes.error.exceptions.ProdNoExiste;
import duoc.amaru.reportes.error.exceptions.ReviewYaExiste;
import duoc.amaru.reportes.model.Review;
import duoc.amaru.reportes.repository.ReviewRepo;

public class ReviewServicioTest {
    @Mock  // Imitación de Review Repo
    private ReviewRepo reviewRepo;

    @Mock  // Imitación de Sesion Client
    private SesionClient sesionClient;

    @Mock // Imitación de Producto Client
    private ProdClient prodClient;

    @InjectMocks
    private ReviewServicio reviewServicio;
    
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // CREAR RESEÑA (EXITOSO; CLIENTE NO HA CREADO NINGUNA RESEÑA)
    @Test
    void testCrearReviewCli() {
        // Preparación
        ReviewDTO dto = new ReviewDTO(5, "    Muy buen jabón    ");
        Review armada = new Review(null, 1L, 1L, 5, "Muy buen jabón", LocalDate.now());
        Review guardada = new Review(1L, 1L, 1L, 5, "Muy buen jabón", LocalDate.now());

        // Configuración
        /* 
            El Cliente existe
            Reseñas de ese Cliente no existen
            Reseña para el Producto existe
            Guardar Reseña "r"
        */
        when(sesionClient.validarCliente(1L)).thenReturn(true);
        when(prodClient.existeProdcuto(1L)).thenReturn(true);
        when(reviewRepo.existsByIdCliente(1L)).thenReturn(false);
        when(reviewRepo.existsByIdProducto(1L)).thenReturn(true);
        when(reviewRepo.save(armada)).thenReturn(guardada);

        // Testeo
        Review resultado = reviewServicio.crearReview(1L, 1L, dto);

        // Verificación
        /*
            Calificación se guardó como 5
            Comentario se guardó sin espacios ni al principio ni al final
            Id Reseña = 1L
            Id Cliente = 1L
            Id Producto = 1L
            Fecha es la actual
        */
        assertEquals(5, resultado.getCalificacion());
        assertEquals("Muy buen jabón", resultado.getComentario());
        assertEquals(1L, resultado.getIdReview());
        assertEquals(1L, resultado.getIdCliente());
        assertEquals(1L, resultado.getIdProducto());
        assertEquals(LocalDate.now(), resultado.getFecha());

        /*
            El Cliente existe y está logeado -> ejecutó 1 vez
            Reseñas deL Cliente existen    -> ejecutó 1 vez 
            Reseña para Producto existe  -> ejecutó 1 vez
            Guardar Reseña             -> ejecutó 1 vez
        */
        verify(sesionClient, times(1)).validarCliente(1L);
        verify(reviewRepo, times(1)).existsByIdCliente(1L);
        verify(reviewRepo, times(0)).existsByIdProducto(1L);
        verify(reviewRepo, times(1)).save(armada);
    }


    // CREAR RESEÑA (EXITOSO; CLIENTE NO HA CREADO RESEÑAS PARA EL PRODUCTO)
    @Test
    void testCrearReviewProd() {
        // Preparación
        ReviewDTO dto = new ReviewDTO(5, "    Muy buen jabón    ");
        Review armada = new Review(null, 1L, 1L, 5, "Muy buen jabón", LocalDate.now());
        Review guardada = new Review(1L, 1L, 1L, 5, "Muy buen jabón", LocalDate.now());

        // Configuración
        /* 
            El Cliente existe
            Existen Reseñas del Cliente 
            No existen Reseñas para el Producto
            Guardar Reseña "r"
        */
        when(sesionClient.validarCliente(1L)).thenReturn(true);
        when(prodClient.existeProdcuto(1L)).thenReturn(true);
        when(reviewRepo.existsByIdCliente(1L)).thenReturn(true);
        when(reviewRepo.existsByIdProducto(1L)).thenReturn(false);
        when(reviewRepo.save(armada)).thenReturn(guardada);

        // Testeo
        Review resultado = reviewServicio.crearReview(1L, 1L, dto);

        // Verificación
        /*
            Calificación se guardó como 5
            Comentario se guardó sin espacios ni al principio ni al final
            Id Reseña = 1L
            Id Cliente = 1L
            Id Producto = 1L
            Fecha es la actual
        */
        assertEquals(5, resultado.getCalificacion());
        assertEquals("Muy buen jabón", resultado.getComentario());
        assertEquals(1L, resultado.getIdReview());
        assertEquals(1L, resultado.getIdCliente());
        assertEquals(1L, resultado.getIdProducto());
        assertEquals(LocalDate.now(), resultado.getFecha());

        /*
            El Cliente existe y está logeado -> ejecutó 1 vez
            El Cliente ha creado reseñas      -> ejecuto 1 vez 
            Existen Reseñas para el Producto   -> ejecutó 1 vez
            Guardar Reseña                   -> ejecutó 1 vez
            */
        verify(sesionClient, times(1)).validarCliente(1L);
        verify(reviewRepo, times(1)).existsByIdCliente(1L);
        verify(reviewRepo, times(1)).existsByIdProducto(1L);
        verify(reviewRepo, times(1)).save(armada);
    }


    // CREAR RESEÑA (FALLIDO; EL CLIENTE FALLÓ LA VALIDACIÓN)
    @Test
    void testCrearReviewValidarCliente() {
        // Preparación
        ReviewDTO nueva = new ReviewDTO(5, "    Muy buen jabón    ");
        Review r = new Review(1L, 1L, 1L, 5, "Muy buen jabón", LocalDate.now());

        // Configuración
        /* 
            El Cliente no existe
            El Cliente no tiene Reseñas creadas
            Reseña para el Producto existe
            Guardar Reseña
        */
        when(sesionClient.validarCliente(1L))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        when(prodClient.existeProdcuto(1L)).thenReturn(true);
        when(reviewRepo.existsByIdCliente(1L)).thenReturn(false);
        when(reviewRepo.existsByIdProducto(1L)).thenReturn(true);
        when(reviewRepo.save(r)).thenReturn(r);

        // Testeo y Verificación
        assertThrows(HttpClientErrorException.class, () -> {
            reviewServicio.crearReview(1L, 1L, nueva);
        });

        /*
            Solo validarCliente se ejecuta */
        verify(sesionClient, times(1)).validarCliente(1L);
        verify(reviewRepo, times(0)).existsByIdCliente(1L);
        verify(reviewRepo, times(0)).existsByIdProducto(1L);
        verify(reviewRepo, times(0)).save(r);
    }

    
    // CREAR RESEÑA (FALLIDO; EL PRODUCTO NO EXISTE)
    @Test
    void testCrearReviewProdNoExiste() {
        // Preparación
        ReviewDTO dto = new ReviewDTO(5, "    Muy buen jabón    ");
        Review armada = new Review(null, 1L, 1L, 5, "Muy buen jabón", LocalDate.now());
        Review guardada = new Review(1L, 1L, 1L, 5, "Muy buen jabón", LocalDate.now());

        // Configuración
        /* */
        when(sesionClient.validarCliente(1L)).thenReturn(true);
        when(prodClient.existeProdcuto(1L)).thenReturn(false);
        when(reviewRepo.existsByIdCliente(1L)).thenReturn(false);
        when(reviewRepo.existsByIdProducto(1L)).thenReturn(false);
        when(reviewRepo.save(armada)).thenReturn(guardada);

        // Testeo y Verificación
        assertThrows(ProdNoExiste.class, () -> {
            reviewServicio.crearReview(1L, 1L, dto);    
        });

        /**/
        verify(sesionClient, times(1)).validarCliente(1L);
        verify(prodClient, times(1)).existeProdcuto(1L);
        verify(reviewRepo, times(0)).existsByIdCliente(1L);
        verify(reviewRepo, times(0)).existsByIdProducto(1L);
        verify(reviewRepo, times(0)).save(armada);
    }

    // CREAR RESEÑA (FALLIDO; RESEÑA DUPLICADA)
    @Test
    void testCrearReviewFDuplicado() {
        // Preparación
        ReviewDTO dto = new ReviewDTO(5, "    Muy buen jabón    ");
        Review armada = new Review(null, 1L, 1L, 5, "Muy buen jabón", LocalDate.now());
        Review guardada = new Review(1L, 1L, 1L, 5, "Muy buen jabón", LocalDate.now());

        // Configuración
        /* 
            Cliente 1L existe;              Retorna "true"
            Reseña de Cliente 1L existe;    Retorna "true"
            Reseña para Producto 1L existe; Retorna "true"
            Guardar Reseña "r";             Retorna "r"
        */
        when(sesionClient.validarCliente(1L)).thenReturn(true);
        when(prodClient.existeProdcuto(1L)).thenReturn(true);
        when(reviewRepo.existsByIdCliente(1L)).thenReturn(true);
        when(reviewRepo.existsByIdProducto(1L)).thenReturn(true);
        when(reviewRepo.save(armada)).thenReturn(guardada);

        // Testeo y Verificación
        assertThrows(ReviewYaExiste.class, () -> {
            reviewServicio.crearReview(1L, 1L, dto);    
        });

        /*
            Solo NO se ejecuta el guardado */
        verify(sesionClient, times(1)).validarCliente(1L);
        verify(reviewRepo, times(1)).existsByIdCliente(1L);
        verify(reviewRepo, times(1)).existsByIdProducto(1L);
        verify(reviewRepo, times(0)).save(armada);
    }


    // MOSTRAR RESEÑAS (HAY RESEÑAS)
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

    // MOSTRAR RESEÑAS (SIN RESEÑAS)
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

    // MOSTRAR RESEÑAS POR PRODUCTO (HAY RESEÑAS)
    @Test
    void testMostrarByProdLleno() {
        // Preparación
        Review r1 = new Review(1L, 1L, 1L, 5, "Muy buen jabon", LocalDate.now());
        Review r2 = new Review(2L, 2L, 1L, 1, "Pesimo producto", LocalDate.now());

        List<Review> reviews = new ArrayList<>();
        reviews.add(r1);
        reviews.add(r2);

        // Configuración
        when(reviewRepo.findAllByIdProducto(1L)).thenReturn(reviews);

        // Testeo
        List<Review> resultado = reviewServicio.filtrarPorProducto(1L);

        // Verificar
        assertEquals(resultado.size(), 2);
        assertEquals(resultado.get(0).getIdReview(), 1L);
        assertEquals(resultado.get(1).getIdReview(), 2L);

        verify(reviewRepo, times(1)).findAllByIdProducto(1L);
    }

    // MOSTRAR RESEÑAS POR PRODUCTO (SIN RESEÑAS)
    @Test
    void testMostrarByProdVacio() {
        // Preparación
        List<Review> reviews = new ArrayList<>();

        // Configuración
        when(reviewRepo.findAllByIdProducto(2L)).thenReturn(reviews);

        // Testeo
        List<Review> resultado = reviewServicio.filtrarPorProducto(2L);

        // Verificar
        assertEquals(resultado.size(), 0);

        verify(reviewRepo, times(1)).findAllByIdProducto(2L);
    }

    // EDITAR RESEÑA (EXITOSO)
    @Test
    void testEditarReviewTrue() {
        // Preparación
        String comentario = "        Muy buen jabón, recomendado!    ";
        Review former = new Review(1L, 1L, 1L, 5, "Muy buen jabón", LocalDate.now());
        Review editado = new Review(1L, 1L, 1L, 5, "Muy buen jabón, recomendado!", LocalDate.now());

        // Configuración
        when(sesionClient.validarCliente(1L)).thenReturn(true);
        when(reviewRepo.existsById(1L)).thenReturn(true);
        when(reviewRepo.findById(1L)).thenReturn(Optional.of(former));
        when(reviewRepo.save(editado)).thenReturn(editado);

        // Testeo
        Review resultado = reviewServicio.editarComentario(comentario, 1L, 1L);

        // Verificar
        assertEquals(resultado.getComentario(), "Muy buen jabón, recomendado!");
        assertEquals(resultado, editado);

        verify(sesionClient, times(1)).validarCliente(1L);
        verify(reviewRepo, times(1)).existsById(1L);
        verify(reviewRepo, times(1)).findById(1L);
        verify(reviewRepo, times(1)).save(editado);
    }

    // EDITAR RESEÑA (FALLIDO)
    @Test
    void testEditarReviewFalse() {
        // Preparación
        Review editado = new Review(1L, 1L, 1L, 5, "Muy buen jabón, recomendado!", LocalDate.now());
        String comentario = "        Muy buen jabón, recomendado!    ";

        // Configuración
        when(reviewRepo.save(editado)).thenReturn(editado);
        when(reviewRepo.existsById(1L)).thenReturn(false);

        // Testeo
        Review resultado = reviewServicio.editarComentario(comentario, 1L, 1L);
        
        // Verificar
        assertNull(resultado);

        verify(reviewRepo, times(1)).existsById(1L);
        verify(reviewRepo, times(0)).save(editado);
    }

    // ELIMINAR RESEÑA (EXITOSO)
    @Test
    void testEliminarReviewTrue() {
        // Configuración
        when(reviewRepo.existsById(1L)).thenReturn(true);

        // Testeo
        boolean resultado = reviewServicio.eliminarReview(1L, 1L);

        // Verificar
        assertTrue(resultado);

        verify(reviewRepo, times(1)).existsById(1L);
        verify(reviewRepo, times(1)).deleteById(1L);
    }

    // ELIMINAR RESEÑA (FALLIDO)
    @Test
    void testEliminarReviewFalse() {
        // Configuración
        when(reviewRepo.existsById(1L)).thenReturn(false);

        // Testeo
        boolean resultado = reviewServicio.eliminarReview(1L, 1L);

        // Verificar
        assertFalse(resultado);

        verify(reviewRepo, times(1)).existsById(1L);
        verify(reviewRepo, times(0)).deleteById(1L);
    }

}
