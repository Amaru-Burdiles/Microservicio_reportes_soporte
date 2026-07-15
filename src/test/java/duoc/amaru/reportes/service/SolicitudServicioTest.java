package duoc.amaru.reportes.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import duoc.amaru.reportes.client.SesionClient;
import duoc.amaru.reportes.dto.CrearSoliDTO;
import duoc.amaru.reportes.error.exceptions.OffLimitsException;
import duoc.amaru.reportes.model.Solicitud;
import duoc.amaru.reportes.repository.SolicitudRepo;

public class SolicitudServicioTest {

    @Mock
    private SolicitudRepo solicitudRepo;

    @Mock
    private SesionClient sesionClient;

    @InjectMocks
    private SolicitudServicio solicitudServicio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearSolicitud_Exito() {
        CrearSoliDTO dto = new CrearSoliDTO();
        dto.setAsunto("Falla de sistema");
        dto.setDescripcion("No puedo entrar");

        Solicitud mockGuardada = new Solicitud();
        mockGuardada.setAsunto("Falla de sistema");
        when(solicitudRepo.save(any(Solicitud.class))).thenReturn(mockGuardada);

        Solicitud resultado = solicitudServicio.crearSolicitud(dto, 1L, true);

        assertNotNull(resultado);
        assertEquals("Falla de sistema", resultado.getAsunto());
        verify(sesionClient, times(1)).validarCliente(1L);
        verify(solicitudRepo, times(1)).save(any(Solicitud.class));
    }

    @Test
    void verHistorial_Exito() {
        Solicitud s1 = new Solicitud();
        s1.setReclamo(true);
        Solicitud s2 = new Solicitud();
        s2.setReclamo(false);

        when(solicitudRepo.findAllByIdCliente(1L)).thenReturn(List.of(s1, s2));

        List<Solicitud> resultado = solicitudServicio.verHistorial(1L, true);

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isReclamo());
        verify(sesionClient, times(1)).validarCliente(1L);
    }

    @Test
    void findHistorialByEstado_EstadoInvalido() {
        List<Solicitud> resultado = solicitudServicio.findHistorialByEstado(1L, true, "INVENTADO");

        assertNull(resultado);
        verify(sesionClient, times(1)).validarCliente(1L);
    }

    @Test
    void findHistorialByEstado_Exito() {
        Solicitud s1 = new Solicitud();
        s1.setReclamo(true);
        s1.setEstado("Pendiente");

        Solicitud s2 = new Solicitud();
        s2.setReclamo(true);
        s2.setEstado("Resuelto");

        when(solicitudRepo.findAllByIdCliente(1L)).thenReturn(List.of(s1, s2));

        List<Solicitud> resultado = solicitudServicio.findHistorialByEstado(1L, true, "pendiente");

        assertEquals(1, resultado.size());
        assertEquals("Pendiente", resultado.get(0).getEstado());
    }

    @Test
    void getSolicitud_NoExiste() {
        when(solicitudRepo.findById(10L)).thenReturn(Optional.empty());

        Solicitud resultado = solicitudServicio.getSolicitud(1L, 10L);

        assertNull(resultado);
        verify(sesionClient, times(1)).validarUsuario(1L);
    }

    @Test
    void getSolicitud_EsEmpleado() {
        Solicitud s = new Solicitud();
        when(solicitudRepo.findById(10L)).thenReturn(Optional.of(s));
        when(sesionClient.validarEmpleado(10L, 1)).thenReturn(true);

        Solicitud resultado = solicitudServicio.getSolicitud(1L, 10L);

        assertNotNull(resultado);
    }

    @Test
    void getSolicitud_EsDueno() {
        Solicitud s = new Solicitud();
        s.setIdCliente(1L);
        when(solicitudRepo.findById(10L)).thenReturn(Optional.of(s));
        when(sesionClient.validarEmpleado(10L, 1)).thenReturn(false);

        Solicitud resultado = solicitudServicio.getSolicitud(1L, 10L);

        assertNotNull(resultado);
    }

    @Test
    void getSolicitud_OffLimits() {
        Solicitud s = new Solicitud();
        s.setIdCliente(99L);
        when(solicitudRepo.findById(10L)).thenReturn(Optional.of(s));
        when(sesionClient.validarEmpleado(10L, 1)).thenReturn(false);

        assertThrows(OffLimitsException.class, () -> solicitudServicio.getSolicitud(1L, 10L));
    }

    @Test
    void getReclamosByEstado_EstadoInvalido() {
        List<Solicitud> resultado = solicitudServicio.getReclamosByEstado(1L, "INVALIDO");

        assertNull(resultado);
        verify(sesionClient, times(1)).validarEmpleado(1L, 1);
    }

    @Test
    void getReclamosByEstado_Exito() {
        Solicitud s = new Solicitud();
        when(solicitudRepo.findByReclamoAndEstado(true, "pendiente")).thenReturn(List.of(s));

        List<Solicitud> resultado = solicitudServicio.getReclamosByEstado(1L, "pendiente");

        assertEquals(1, resultado.size());
    }

    @Test
    void marcarEnRevision_NoExiste() {
        when(solicitudRepo.findById(10L)).thenReturn(Optional.empty());

        boolean resultado = solicitudServicio.marcarEnRevision(1L, 10L);

        assertFalse(resultado);
    }

    @Test
    void marcarEnRevision_EstadoIncorrecto() {
        Solicitud s = new Solicitud();
        s.setEstado("Resuelto");
        when(solicitudRepo.findById(10L)).thenReturn(Optional.of(s));

        boolean resultado = solicitudServicio.marcarEnRevision(1L, 10L);

        assertFalse(resultado);
    }

    @Test
    void marcarEnRevision_Exito() {
        Solicitud s = new Solicitud();
        s.setEstado("pendiente");
        when(solicitudRepo.findById(10L)).thenReturn(Optional.of(s));

        boolean resultado = solicitudServicio.marcarEnRevision(1L, 10L);

        assertTrue(resultado);
        verify(solicitudRepo, times(1)).save(s);
    }

    @Test
    void marcarResulta_NoExiste() {
        when(solicitudRepo.findById(10L)).thenReturn(Optional.empty());

        boolean resultado = solicitudServicio.marcarResulta(1L, 10L);

        assertFalse(resultado);
    }

    @Test
    void marcarResulta_EstadoIncorrecto() {
        Solicitud s = new Solicitud();
        s.setEstado("cerrado");
        when(solicitudRepo.findById(10L)).thenReturn(Optional.of(s));

        boolean resultado = solicitudServicio.marcarResulta(1L, 10L);

        assertFalse(resultado);
    }

    @Test
    void marcarResulta_Exito() {
        Solicitud s = new Solicitud();
        s.setEstado("En Revision");
        when(solicitudRepo.findById(10L)).thenReturn(Optional.of(s));

        boolean resultado = solicitudServicio.marcarResulta(1L, 10L);

        assertTrue(resultado);
        verify(solicitudRepo, times(1)).save(s);
    }

    @Test
    void cerrarSolicictud_NoExiste() {
        when(solicitudRepo.findById(10L)).thenReturn(Optional.empty());

        boolean resultado = solicitudServicio.cerrarSolicictud(1L, 10L);

        assertFalse(resultado);
    }

    @Test
    void cerrarSolicictud_EstadoIncorrecto() {
        Solicitud s = new Solicitud();
        s.setEstado("resuelto");
        when(solicitudRepo.findById(10L)).thenReturn(Optional.of(s));

        boolean resultado = solicitudServicio.cerrarSolicictud(1L, 10L);

        assertFalse(resultado);
    }

    @Test
    void cerrarSolicictud_Exito() {
        Solicitud s = new Solicitud();
        s.setEstado("En Revision");
        when(solicitudRepo.findById(10L)).thenReturn(Optional.of(s));

        boolean resultado = solicitudServicio.cerrarSolicictud(1L, 10L);

        assertTrue(resultado);
        verify(solicitudRepo, times(1)).save(s);
    }
}