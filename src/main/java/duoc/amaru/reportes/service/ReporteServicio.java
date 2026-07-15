package duoc.amaru.reportes.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class ReporteServicio {
    @Autowired // Repo Reporte
    private ReporteRepo reporteRepo;

    @Autowired // Client Producto
    private ProdClient prodClient;

    @Autowired // Client Pedido
    private PedidoClient pedidoClient;

    @Autowired
    private VentaClient ventaClient;

    @Autowired // Client Sesion
    private SesionClient sesionClient;

    @Autowired
    private UsuarioClient usuarioClient;
    
    // GENERAR REPORTE VENTAS
    public ReporteVentas generarReporteVenta(Long userId) {
        // Validar usuario ejecutor
        sesionClient.validarEmpleado(userId, 3);

        // Rellenar especificaciones de reporte
        ReporteVentas ventas = new ReporteVentas();
        ventas.setTipoReporte("Ventas");
        ventas.setFechaGeneracion(LocalDateTime.now());
        ventas.setGeneradoPor(userId);
        ventas.setFormato("JSON");

        // Rellenar estadisticas de ventas
        double suma = ventaClient.calcTotalVentas();
        ventas.setTotalVentas(suma);
        
        int pedidos = pedidoClient.getCantPedidos();
        ventas.setCantPedidos(pedidos);
        
        // Guardar reporte
        return reporteRepo.save(ventas);
    }
    
    // GENERAR REPORTE INVENTARIO
    public ReporteInventario generarReporteInv(Long userId, int umbral, Long invId) {
        // Validar usuario ejecutor
        sesionClient.validarEmpleado(userId, 3);

        // Rellenar especificaciones de reporte
        ReporteInventario inv = new ReporteInventario();
        inv.setTipoReporte("Inventario");
        inv.setFechaGeneracion(LocalDateTime.now());
        inv.setGeneradoPor(userId);
        inv.setFormato("JSON");
        
        // Rellenar estadisticas de inventario
        int totalProds = prodClient.getTotalProductos();
        inv.setTotalProductos(totalProds);
        
        List<ProdDTO> stockBajo = prodClient.getProductosLowStock(umbral, invId);
        List<Long> prodIds = new ArrayList<>();
        for (ProdDTO p : stockBajo) {
            prodIds.add(p.getIdProducto());
        }
        inv.setProdBajoStock(prodIds);
        
        // Guardar reporte
        return reporteRepo.save(inv);
    }

    // GENERAR REPORTE RENDIMIENTO
    public ReporteRendimiento generarReporteRen(Long userId) {
        // Validar usuario ejecutor
        sesionClient.validarEmpleado(userId, 3);

        // Rellenar especificaciones de reporte
        ReporteRendimiento ren = new ReporteRendimiento();
        ren.setTipoReporte("Rendimiento");
        ren.setFechaGeneracion(LocalDateTime.now());
        ren.setGeneradoPor(userId);
        ren.setFormato("JSON");

        // Rellenar especificaciones
        double suma = ventaClient.calcTotalVentas();
        ren.setVentasTotales(suma);

        // Ticket Promedio
        int transacciones = ventaClient.getCantVentasHechas();
        ren.setTicketPromedio(ren.getVentasTotales() / transacciones);

        // Tasa de Conversion
        int clientesQueCompraron = ventaClient.getCantClientesQueCompraron();
        int clientesRegistrados = usuarioClient.getClientesRegistrados();
        ren.setTasaConversion(clientesQueCompraron / clientesRegistrados * 100);

        // Margen Bruto
        /* TODO: Para futuras versiones, crear una sección en microservicio Inventario
           encargada de llevar el registro de los Costos de bienes vendidos (COGS).
           Mandar una petición por este valor desde Client y utilizarlo para el calculo:
           Ventas Totales - COGS / Ventas Totales = Margen Bruto
        */
        return reporteRepo.save(ren);
    }

    // OBTENER TODOS LOS REPORTES
    public List<Reporte> mostrarTodos(Long userId) {
        // Validar usuario ejecutor
        sesionClient.validarEmpleado(userId, 3);

        return reporteRepo.findAll();
    }

    // OBTENER REPORTES POR TIPO
    public List<Reporte> reportesByTipo(String tipo, Long userId) {
        // Validar usuario ejecutor
        sesionClient.validarEmpleado(userId, 3);

        if (!tipo.equalsIgnoreCase("inventario")
            && !tipo.equalsIgnoreCase("ventas")
            && !tipo.equalsIgnoreCase("rendimiento")) {
            throw new UnknownTipoReporte();
        }

        return reporteRepo.findByTipoReporte(tipo);
    }

    // OBTENER REPORTE POR ID
    public Reporte reporteById(Long idReporte, Long userId) {
        // Validar usuario ejecutor
        sesionClient.validarEmpleado(userId, 3);

        return reporteRepo.findById(idReporte).get();
    }

}
