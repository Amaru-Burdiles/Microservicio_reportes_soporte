package duoc.amaru.reportes.controller;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import duoc.amaru.reportes.dto.CrearSoliDTO;
import duoc.amaru.reportes.model.Solicitud;
import duoc.amaru.reportes.service.SolicitudServicio;

public class SoporteControladorTest {

    @Mock
    private SolicitudServicio solicitudServicio;

    @InjectMocks
    private SoporteControlador soporteControlador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void postSoporte_Exito() {
        CrearSoliDTO dto = new CrearSoliDTO();
        Solicitud s = new Solicitud();
        s.setIdSolicitud(5L);

        when(solicitudServicio.crearSolicitud(any(CrearSoliDTO.class), eq(1L), eq(false))).thenReturn(s);

        ResponseEntity<?> response = soporteControlador.postSoporte(dto, 1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Solicitud de soporte creada y publicada con id #5", response.getBody());
    }

    @Test
    void getHistorial_Vacio() {
        when(solicitudServicio.verHistorial(1L, false)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = soporteControlador.getHistorial(1L);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("No tienes solicitudes de soporte registradas", response.getBody());
    }

    @Test
    void getHistorial_ConDatos() {
        Solicitud s = new Solicitud();
        List<Solicitud> lista = List.of(s);
        when(solicitudServicio.verHistorial(1L, false)).thenReturn(lista);

        ResponseEntity<?> response = soporteControlador.getHistorial(1L);

        assertEquals(404, response.getStatusCode().value());
        assertEquals(lista, response.getBody());
    }

    @Test
    void getHistorialPorEstado_Vacio() {
        when(solicitudServicio.findHistorialByEstado(1L, false, "pendiente")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = soporteControlador.getHistorialPorEstado(1L, "pendiente");

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Sin resultados", response.getBody());
    }

    @Test
    void getHistorialPorEstado_ConDatos() {
        Solicitud s = new Solicitud();
        List<Solicitud> lista = List.of(s);
        when(solicitudServicio.findHistorialByEstado(1L, false, "pendiente")).thenReturn(lista);

        ResponseEntity<?> response = soporteControlador.getHistorialPorEstado(1L, "pendiente");

        assertEquals(404, response.getStatusCode().value());
        assertEquals(lista, response.getBody());
    }

    @Test
    void getReclamoById_Encontrado() {
        Solicitud s = new Solicitud();
        when(solicitudServicio.getSolicitud(1L, 2L)).thenReturn(s);

        ResponseEntity<?> response = soporteControlador.getReclamoById(1L, 2L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(s, response.getBody());
    }

    @Test
    void getReclamoById_NoEncontrado() {
        when(solicitudServicio.getSolicitud(1L, 2L)).thenReturn(null);

        ResponseEntity<?> response = soporteControlador.getReclamoById(1L, 2L);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Solicitud no encontrada", response.getBody());
    }

    @Test
    void putRevisarSoporte_Exito() {
        when(solicitudServicio.marcarEnRevision(1L, 2L)).thenReturn(true);

        ResponseEntity<?> response = soporteControlador.putRevisarSoporte(1L, 2L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Se cambió el estado de la solicitud", response.getBody());
    }

    @Test
    void putRevisarSoporte_Fallo() {
        when(solicitudServicio.marcarEnRevision(1L, 2L)).thenReturn(false);

        ResponseEntity<?> response = soporteControlador.putRevisarSoporte(1L, 2L);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Error: Solicitud no encontrada o la acción es imposible", response.getBody());
    }

    @Test
    void putResolverSoporte_Exito() {
        when(solicitudServicio.marcarResulta(1L, 2L)).thenReturn(true);

        ResponseEntity<?> response = soporteControlador.putResolverSoporte(1L, 2L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Se marcó la solicitud como resuelta", response.getBody());
    }

    @Test
    void putResolverSoporte_Fallo() {
        when(solicitudServicio.marcarResulta(1L, 2L)).thenReturn(false);

        ResponseEntity<?> response = soporteControlador.putResolverSoporte(1L, 2L);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Error: La solicitud no existe o está cerrada", response.getBody());
    }

    @Test
    void putCerrarSoporte_Exito() {
        when(solicitudServicio.cerrarSolicictud(1L, 2L)).thenReturn(true);

        ResponseEntity<?> response = soporteControlador.putCerrarSoporte(1L, 2L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Se cerró la solicitud", response.getBody());
    }

    @Test
    void putCerrarSoporte_Fallo() {
        when(solicitudServicio.cerrarSolicictud(1L, 2L)).thenReturn(false);

        ResponseEntity<?> response = soporteControlador.putCerrarSoporte(1L, 2L);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Error: La solicitud no existe o está resuelta", response.getBody());
    }
}