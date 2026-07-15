package duoc.amaru.reportes.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import duoc.amaru.reportes.client.PedidoClient;
import duoc.amaru.reportes.client.ProdClient;
import duoc.amaru.reportes.client.SesionClient;
import duoc.amaru.reportes.client.UsuarioClient;
import duoc.amaru.reportes.client.VentaClient;
import duoc.amaru.reportes.dto.ProdDTO;
import duoc.amaru.reportes.error.exceptions.UnknownTipoReporte;
import duoc.amaru.reportes.model.Reporte;
import duoc.amaru.reportes.model.ReporteInventario;
import duoc.amaru.reportes.model.ReporteRendimiento;
import duoc.amaru.reportes.model.ReporteVentas;
import duoc.amaru.reportes.repository.ReporteRepo;

public class ReporteServicioTest {

    @Mock
    private ReporteRepo reporteRepo;

    @Mock
    private ProdClient prodClient;

    @Mock
    private PedidoClient pedidoClient;

    @Mock
    private VentaClient ventaClient;

    @Mock
    private SesionClient sesionClient;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private ReporteServicio reporteServicio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generarReporteVenta_Exito() {
        when(ventaClient.calcTotalVentas()).thenReturn(50000.0);
        when(pedidoClient.getCantPedidos()).thenReturn(100);
        
        ReporteVentas reporteGuardado = new ReporteVentas();
        reporteGuardado.setTotalVentas(50000.0);
        reporteGuardado.setCantPedidos(100);
        
        when(reporteRepo.save(any(ReporteVentas.class))).thenReturn(reporteGuardado);

        ReporteVentas resultado = reporteServicio.generarReporteVenta(1L);

        assertNotNull(resultado);
        assertEquals(50000.0, resultado.getTotalVentas());
        assertEquals(100, resultado.getCantPedidos());
        verify(sesionClient, times(1)).validarEmpleado(1L, 3);
        verify(reporteRepo, times(1)).save(any(ReporteVentas.class));
    }

    @Test
    void generarReporteInv_Exito() {
        when(prodClient.getTotalProductos()).thenReturn(200);
        
        ProdDTO prod1 = new ProdDTO();
        prod1.setIdProducto(10L);
        ProdDTO prod2 = new ProdDTO();
        prod2.setIdProducto(20L);
        when(prodClient.getProductosLowStock(5, 1L)).thenReturn(List.of(prod1, prod2));
        
        ReporteInventario reporteGuardado = new ReporteInventario();
        reporteGuardado.setTotalProductos(200);
        reporteGuardado.setProdBajoStock(List.of(10L, 20L));
        
        when(reporteRepo.save(any(ReporteInventario.class))).thenReturn(reporteGuardado);

        ReporteInventario resultado = reporteServicio.generarReporteInv(1L, 5, 1L);

        assertNotNull(resultado);
        assertEquals(200, resultado.getTotalProductos());
        assertEquals(2, resultado.getProdBajoStock().size());
        verify(sesionClient, times(1)).validarEmpleado(1L, 3);
        verify(reporteRepo, times(1)).save(any(ReporteInventario.class));
    }

    @Test
    void generarReporteRen_Exito() {
        when(ventaClient.calcTotalVentas()).thenReturn(10000.0);
        when(ventaClient.getCantVentasHechas()).thenReturn(50);
        when(ventaClient.getCantClientesQueCompraron()).thenReturn(25);
        when(usuarioClient.getClientesRegistrados()).thenReturn(100);

        ReporteRendimiento reporteGuardado = new ReporteRendimiento();
        reporteGuardado.setVentasTotales(10000.0);
        reporteGuardado.setTicketPromedio(200.0);
        reporteGuardado.setTasaConversion(25.0);

        when(reporteRepo.save(any(ReporteRendimiento.class))).thenReturn(reporteGuardado);

        ReporteRendimiento resultado = reporteServicio.generarReporteRen(1L);

        assertNotNull(resultado);
        assertEquals(10000.0, resultado.getVentasTotales());
        verify(sesionClient, times(1)).validarEmpleado(1L, 3);
        verify(reporteRepo, times(1)).save(any(ReporteRendimiento.class));
    }

    @Test
    void mostrarTodos_Exito() {
        Reporte r1 = new ReporteVentas();
        Reporte r2 = new ReporteInventario();
        when(reporteRepo.findAll()).thenReturn(List.of(r1, r2));

        List<Reporte> resultado = reporteServicio.mostrarTodos(1L);

        assertEquals(2, resultado.size());
        verify(sesionClient, times(1)).validarEmpleado(1L, 3);
        verify(reporteRepo, times(1)).findAll();
    }

    @Test
    void reportesByTipo_Inventario_Exito() {
        Reporte r1 = new ReporteInventario();
        when(reporteRepo.findByTipoReporte("inventario")).thenReturn(List.of(r1));

        List<Reporte> resultado = reporteServicio.reportesByTipo("inventario", 1L);

        assertEquals(1, resultado.size());
        verify(sesionClient, times(1)).validarEmpleado(1L, 3);
        verify(reporteRepo, times(1)).findByTipoReporte("inventario");
    }

    @Test
    void reportesByTipo_Ventas_Exito() {
        Reporte r1 = new ReporteVentas();
        when(reporteRepo.findByTipoReporte("ventas")).thenReturn(List.of(r1));

        List<Reporte> resultado = reporteServicio.reportesByTipo("ventas", 1L);

        assertEquals(1, resultado.size());
        verify(sesionClient, times(1)).validarEmpleado(1L, 3);
        verify(reporteRepo, times(1)).findByTipoReporte("ventas");
    }

    @Test
    void reportesByTipo_Rendimiento_Exito() {
        Reporte r1 = new ReporteRendimiento();
        when(reporteRepo.findByTipoReporte("rendimiento")).thenReturn(List.of(r1));

        List<Reporte> resultado = reporteServicio.reportesByTipo("rendimiento", 1L);

        assertEquals(1, resultado.size());
        verify(sesionClient, times(1)).validarEmpleado(1L, 3);
        verify(reporteRepo, times(1)).findByTipoReporte("rendimiento");
    }

    @Test
    void reportesByTipo_Desconocido_LanzaExcepcion() {
        assertThrows(UnknownTipoReporte.class, () -> reporteServicio.reportesByTipo("invalido", 1L));
        verify(sesionClient, times(1)).validarEmpleado(1L, 3);
    }

    @Test
    void reporteById_Exito() {
        Reporte r1 = new ReporteVentas();
        r1.setIdReporte(10L);
        when(reporteRepo.findById(10L)).thenReturn(Optional.of(r1));

        Reporte resultado = reporteServicio.reporteById(10L, 1L);

        assertNotNull(resultado);
        verify(sesionClient, times(1)).validarEmpleado(1L, 3);
        verify(reporteRepo, times(1)).findById(10L);
    }
}