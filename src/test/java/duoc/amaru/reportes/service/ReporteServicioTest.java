package duoc.amaru.reportes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import duoc.amaru.reportes.client.PedidoClient;
import duoc.amaru.reportes.client.ProdClient;
import duoc.amaru.reportes.client.SesionClient;
import duoc.amaru.reportes.error.exceptions.UnknownTipoReporte;
import duoc.amaru.reportes.model.Reporte;
import duoc.amaru.reportes.model.ReporteInventario;
import duoc.amaru.reportes.model.ReporteRendimiento;
import duoc.amaru.reportes.model.ReporteVentas;
import duoc.amaru.reportes.repository.ReporteRepo;

public class ReporteServicioTest {
    @Mock // Imitación de Repo Reporte
    private ReporteRepo reporteRepo;

    @Mock // Imitación de Client Producto
    private ProdClient prodClient;

    @Mock // Imitación de Client Pedido
    private PedidoClient pedidoClient;

    @Mock // Imitación de Client Sesion
    private SesionClient sesionClient;
    
    @InjectMocks // Servicio a testear
    private ReporteServicio reporteServicio;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // GENERAR REPORTE VENTAS (EXITOSO)
    @Test
    void testSuccessReporteVentas() {
        // Preparación
        ReporteVentas reporte; // = new ReporteVentas(super(), arg, arg);

    }

    // GENERAR REPORTE VENTAS (FALLIDO)
    @Test
    void testFailReporteVentas() {
        // TODO: Fix super() call on ReporteVentas
    }

    // GENERAR REPORTE INVENTARIO (EXITOSO)
    @Test
    void testSuccessReporteInv() {
        // TODO: Fix super() call on ReporteVentas
    }

    // OBTENER TODOS LOS REPORTES (EXITOSO)
    @Test
    void testSuccessMostrarTodosLleno() {
        // Preparación
        ReporteVentas ventas = new ReporteVentas();
        ReporteInventario inventario = new ReporteInventario();
        ReporteRendimiento rendimiento = new ReporteRendimiento();

        List<Reporte> reportes = new ArrayList<>();
        reportes.add(rendimiento);
        reportes.add(inventario);
        reportes.add(ventas);
        
        // Configuración
        when(sesionClient.validarAceso(1L, 3)).thenReturn(true);
        when(reporteRepo.findAll()).thenReturn(reportes);

        // Testeo
        List<Reporte> resultado = reporteServicio.mostrarTodos(1L);

        // Validación
        assertEquals(reportes, resultado);
        assertEquals(3, resultado.size());

        // Verificación
        verify(sesionClient, times(1)).validarAceso(1L, 3);
        verify(reporteRepo, times(1)).findAll();
    }

    // OBTENER TODOS LOS REPORTES (EXITOSO)
    @Test
    void testSuccessMostrarTodoVacio() {
        // Preparación
        List<Reporte> reportes = new ArrayList<>();

        // Configuración
        when(sesionClient.validarAceso(1L, 3)).thenReturn(true);
        when(reporteRepo.findAll()).thenReturn(reportes);

        // Testeo
        List<Reporte> resultado = reporteServicio.mostrarTodos(1L);

        // Validación
        assertEquals(reportes, resultado);
        assertEquals(0, resultado.size());

        // Verificación
        verify(sesionClient, times(1)).validarAceso(1L, 3);
        verify(reporteRepo, times(1)).findAll();
    }

    // OBTENER TODOS LOS REPORTES (FALLIDO)
    @Test
    void testFailMostrarTodo() {
        // Preparación
        List<Reporte> reportes = new ArrayList<>();

        // Configuración
        when(sesionClient.validarAceso(1L, 3))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        when(reporteRepo.findAll()).thenReturn(reportes);

        // Testeo y Validación
        assertThrows(HttpClientErrorException.class, () -> {
            reporteServicio.mostrarTodos(1L);
        });

        // Verificación
        verify(sesionClient, times(1)).validarAceso(1L, 3);
        verify(reporteRepo, times(0)).findAll();
    }

    // OBTENER TODOS LOS REPORTES POR TIPO INVENTARIO (EXITOSO)
    @Test
    void testSuccessReportesByTipoRendimiento() {
        // Preparación
        ReporteRendimiento rendimiento = new ReporteRendimiento();

        List<Reporte> reportes = new ArrayList<>();
        reportes.add(rendimiento);

        Long userId = 1L;
        int filtro = 3;
        String tipo = "Rendimiento";

        // Configuración
        when(sesionClient.validarAceso(userId, filtro)).thenReturn(true);
        when(reporteRepo.findByTipoReporte(tipo)).thenReturn(reportes);

        // Testeo
        List<Reporte> resultado = reporteServicio.reportesByTipo(tipo, userId);

        // Validación
        assertEquals(reportes, resultado);
        assertEquals(1, resultado.size());

        // Verificación
        verify(sesionClient, times(1)).validarAceso(userId, filtro);
        verify(reporteRepo, times(1)).findByTipoReporte(tipo);
    }

    // OBTENER TODOS LOS REPORTES POR TIPO VENTAS (EXITOSO)
    @Test
    void testSuccessReportesByTipoVentas() {
        // Preparación
        ReporteVentas venta = new ReporteVentas();

        List<Reporte> reportes = new ArrayList<>();
        reportes.add(venta);

        Long userId = 1L;
        int filtro = 3;
        String tipo = "Ventas";

        // Configuración
        when(sesionClient.validarAceso(userId, filtro)).thenReturn(true);
        when(reporteRepo.findByTipoReporte(tipo)).thenReturn(reportes);

        // Testeo
        List<Reporte> resultado = reporteServicio.reportesByTipo(tipo, userId);

        // Validación
        assertEquals(reportes, resultado);
        assertEquals(1, resultado.size());

        // Verificación
        verify(sesionClient, times(1)).validarAceso(userId, filtro);
        verify(reporteRepo, times(1)).findByTipoReporte(tipo);
    }

    // OBTENER TODOS LOS REPORTES POR TIPO INVENTARIO (EXITOSO)
    @Test
    void testSuccessReportesByTipoInv() {
        // Preparación
        ReporteInventario inv = new ReporteInventario();

        List<Reporte> reportes = new ArrayList<>();
        reportes.add(inv);

        Long userId = 1L;
        int filtro = 3;
        String tipo = "Inventario";

        // Configuración
        when(sesionClient.validarAceso(userId, filtro)).thenReturn(true);
        when(reporteRepo.findByTipoReporte(tipo)).thenReturn(reportes);

        // Testeo
        List<Reporte> resultado = reporteServicio.reportesByTipo(tipo, userId);

        // Validación
        assertEquals(reportes, resultado);
        assertEquals(1, resultado.size());

        // Verificación
        verify(sesionClient, times(1)).validarAceso(userId, filtro);
        verify(reporteRepo, times(1)).findByTipoReporte(tipo);
    }

    // OBTENER TODOS LOS REPORTES POR TIPO (FALLIDO; VALIDACIÓN CLIENTE)
    @Test
    void testFailReportesByTipoCli() {
        // Preparación
        List<Reporte> reportes = new ArrayList<>();

        Long userId = 1L;
        int filtro = 3;
        String tipo = "Venta";

        // Configuración
        when(sesionClient.validarAceso(userId, filtro))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        when(reporteRepo.findByTipoReporte(tipo)).thenReturn(reportes);

        // Testeo y Validación
        assertThrows(HttpClientErrorException.class, () -> {
            reporteServicio.reportesByTipo(tipo, userId);
        });

        // Verificación
        verify(sesionClient, times(1)).validarAceso(userId, filtro);
        verify(reporteRepo, times(0)).findByTipoReporte(tipo);
    }

    // OBTENER TODOS LOS REPORTES POR TIPO (FALLIDO; TIPO DESCONOCIDO)
    @Test
    void testFailReportesByTipo() {
        // Preparación
        List<Reporte> reportes = new ArrayList<>();

        Long userId = 1L;
        int filtro = 3;
        String tipo = "Veeeentas";

        // Configuración
        when(sesionClient.validarAceso(userId, filtro)).thenReturn(true);
        when(reporteRepo.findByTipoReporte(tipo)).thenReturn(reportes);

        // Testeo y Validación
        assertThrows(UnknownTipoReporte.class, () -> {
            reporteServicio.reportesByTipo(tipo, userId);
        });

        // Verificación
        verify(sesionClient, times(1)).validarAceso(userId, filtro);
        verify(reporteRepo, times(0)).findByTipoReporte(tipo);
    }
}
