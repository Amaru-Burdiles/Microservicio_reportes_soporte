package duoc.amaru.reportes.controller;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import duoc.amaru.reportes.dto.CrearSoliDTO;
import duoc.amaru.reportes.model.Solicitud;
import duoc.amaru.reportes.service.SolicitudServicio;

@WebMvcTest(ReclamoControlador.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReclamoControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SolicitudServicio solicitudServicio;

    private ObjectMapper objectMapper;

    @Test
    void postReclamo_Exito() throws Exception {
        CrearSoliDTO dto = new CrearSoliDTO();
        dto.setAsunto("Falla");
        dto.setDescripcion("Error en login");

        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(100L);

        when(solicitudServicio.crearSolicitud(any(CrearSoliDTO.class), eq(1L), eq(true))).thenReturn(solicitud);

        mockMvc.perform(post("/api/v1/reclamos/crear/exe:1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Reclamo creado y publicado con id #100"));
    }

    @Test
    void getHistorial_Vacio() throws Exception {
        when(solicitudServicio.verHistorial(1L, true)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/reclamos/cliente:1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No tienes reclamos registrados"));
    }

    @Test
    void getHistorial_ConDatos() throws Exception {
        Solicitud s = new Solicitud();
        s.setIdSolicitud(1L);
        List<Solicitud> lista = List.of(s);

        when(solicitudServicio.verHistorial(1L, true)).thenReturn(lista);

        mockMvc.perform(get("/api/v1/reclamos/cliente:1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(lista)));
    }

    @Test
    void getHistorialPorEstado_Vacio() throws Exception {
        when(solicitudServicio.findHistorialByEstado(1L, true, "pendiente")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/reclamos/cliente:1/pendiente"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Sin resultados"));
    }

    @Test
    void getHistorialPorEstado_ConDatos() throws Exception {
        Solicitud s = new Solicitud();
        s.setIdSolicitud(1L);
        List<Solicitud> lista = List.of(s);

        when(solicitudServicio.findHistorialByEstado(1L, true, "pendiente")).thenReturn(lista);

        mockMvc.perform(get("/api/v1/reclamos/cliente:1/pendiente"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(objectMapper.writeValueAsString(lista)));
    }

    @Test
    void getReclamoById_Encontrado() throws Exception {
        Solicitud s = new Solicitud();
        s.setIdSolicitud(10L);

        when(solicitudServicio.getSolicitud(1L, 10L)).thenReturn(s);

        mockMvc.perform(get("/api/v1/reclamos/usuario:1/reclamo:10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(s)));
    }

    @Test
    void getReclamoById_NoEncontrado() throws Exception {
        when(solicitudServicio.getSolicitud(1L, 10L)).thenReturn(null);

        mockMvc.perform(get("/api/v1/reclamos/usuario:1/reclamo:10"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Solicitud no encontrada"));
    }

    @Test
    void getReclamosByEstado_Invalido() throws Exception {
        when(solicitudServicio.getReclamosByEstado(eq(1L), anyString())).thenReturn(null);

        mockMvc.perform(get("/api/v1/reclamos/empleado:1/invalido"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Estado inválido"));
    }

    @Test
    void getReclamosByEstado_Vacio() throws Exception {
        when(solicitudServicio.getReclamosByEstado(1L, "pendiente")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/reclamos/empleado:1/pendiente"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Sin resultados"));
    }

    @Test
    void getReclamosByEstado_ConDatos() throws Exception {
        Solicitud s = new Solicitud();
        s.setIdSolicitud(10L);
        List<Solicitud> lista = List.of(s);

        when(solicitudServicio.getReclamosByEstado(1L, "pendiente")).thenReturn(lista);

        mockMvc.perform(get("/api/v1/reclamos/empleado:1/pendiente"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(lista)));
    }

    @Test
    void putRevisarReclamo_Exito() throws Exception {
        when(solicitudServicio.marcarEnRevision(1L, 10L)).thenReturn(true);

        mockMvc.perform(put("/api/v1/reclamos/empleado:1/en-revision/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Se cambió el estado de la solicitud"));
    }

    @Test
    void putRevisarReclamo_Fallo() throws Exception {
        when(solicitudServicio.marcarEnRevision(1L, 10L)).thenReturn(false);

        mockMvc.perform(put("/api/v1/reclamos/empleado:1/en-revision/10"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Solicitud no encontrada o la acción es imposible"));
    }

    @Test
    void putResolverReclamo_Exito() throws Exception {
        when(solicitudServicio.marcarResulta(1L, 10L)).thenReturn(true);

        mockMvc.perform(put("/api/v1/reclamos/empleado:1/resolver/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Se marcó la solicitud como resuelta"));
    }

    @Test
    void putResolverReclamo_Fallo() throws Exception {
        when(solicitudServicio.marcarResulta(1L, 10L)).thenReturn(false);

        mockMvc.perform(put("/api/v1/reclamos/empleado:1/resolver/10"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: La solicitud no existe o está cerrada"));
    }

    @Test
    void putCerrarReclamo_Exito() throws Exception {
        when(solicitudServicio.cerrarSolicictud(1L, 10L)).thenReturn(true);

        mockMvc.perform(put("/api/v1/reclamos/empleado:1/cerrar/10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Se cerró la solicitud"));
    }

    @Test
    void putCerrarReclamo_Fallo() throws Exception {
        when(solicitudServicio.cerrarSolicictud(1L, 10L)).thenReturn(false);

        mockMvc.perform(put("/api/v1/reclamos/empleado:1/cerrar/10"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: La solicitud no existe o está resuelta"));
    }
}