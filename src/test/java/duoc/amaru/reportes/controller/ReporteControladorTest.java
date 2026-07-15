package duoc.amaru.reportes.controller;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import duoc.amaru.reportes.model.Reporte;
import duoc.amaru.reportes.model.ReporteInventario;
import duoc.amaru.reportes.model.ReporteVentas;
import duoc.amaru.reportes.service.ReporteServicio;

@WebMvcTest(ReporteControlador.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReporteControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReporteServicio reporteServicio;

    private ObjectMapper objectMapper;

    @Test
    void postReporteVentas_Exito() throws Exception {
        ReporteVentas reporte = new ReporteVentas();
        reporte.setIdReporte(10L);

        when(reporteServicio.generarReporteVenta(1L)).thenReturn(reporte);

        mockMvc.perform(post("/api/v1/reportes/generar/ventas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reporte generado con Id #10\n" + reporte.toString()));
    }

    @Test
    void postReporteInv_Exito() throws Exception {
        ReporteInventario reporte = new ReporteInventario();
        reporte.setIdReporte(20L);

        when(reporteServicio.generarReporteInv(1L, 50, 5L)).thenReturn(reporte);

        mockMvc.perform(post("/api/v1/reportes/generar/inventario5:50/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reporte generado con Id #20\n" + reporte.toString()));
    }

    @Test
    void getReportes_Vacio() throws Exception {
        when(reporteServicio.mostrarTodos(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/reportes/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se ha creado ningún reporte aún"));
    }

    @Test
    void getReportes_ConDatos() throws Exception {
        ReporteVentas reporte = new ReporteVentas();
        reporte.setIdReporte(1L);
        List<Reporte> lista = List.of(reporte);

        when(reporteServicio.mostrarTodos(1L)).thenReturn(lista);

        mockMvc.perform(get("/api/v1/reportes/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(lista)));
    }

    @Test
    void getReportesByTipo_Vacio() throws Exception {
        when(reporteServicio.reportesByTipo("ventas", 1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/reportes/tipo:ventas/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Sin resultados"));
    }

    @Test
    void getReportesByTipo_ConDatos() throws Exception {
        ReporteVentas reporte = new ReporteVentas();
        reporte.setIdReporte(1L);
        List<Reporte> lista = List.of(reporte);

        when(reporteServicio.reportesByTipo("ventas", 1L)).thenReturn(lista);

        mockMvc.perform(get("/api/v1/reportes/tipo:ventas/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(lista)));
    }
}