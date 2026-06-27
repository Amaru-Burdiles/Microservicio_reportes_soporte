package duoc.amaru.reportes.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import duoc.amaru.reportes.client.PedidoClient;
import duoc.amaru.reportes.client.ProdClient;
import duoc.amaru.reportes.client.SesionClient;
import duoc.amaru.reportes.dto.ProdDTO;
import duoc.amaru.reportes.error.exceptions.UnknownTipoReporte;
import duoc.amaru.reportes.model.Reporte;
import duoc.amaru.reportes.model.ReporteInventario;
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

    @Autowired // Client Sesion
    private SesionClient sesionClient;
    
    // GENERAR REPORTE VENTAS
    public ReporteVentas generarReporteVenta(Long userId) {
        // Validar usuario ejecutor
        sesionClient.validarAceso(userId, 3);

        // Rellenar especificaciones de reporte
        ReporteVentas ventas = new ReporteVentas();
        ventas.setTipoReporte("Ventas");
        ventas.setFechaGeneracion(LocalDateTime.now());
        ventas.setGeneradoPor(userId);
        ventas.setFormato("JSON");

        // Rellenar estadisticas de ventas
        //double suma = facturaClient.getTotalFacturas();
        //ventas.setTotalVentas(suma);
        
        int pedidos = pedidoClient.getPedidos();
        ventas.setCantPedidos(pedidos);
        
        // Guardar reporte
        reporteRepo.save(ventas);
        return ventas;
    }
    
    // GENERAR REPORTE INVENTARIO
    public ReporteInventario generarReporteInv(Long userId, int umbral, Long invId) {
        // Validar usuario ejecutor
        sesionClient.validarAceso(userId, 3);

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
        reporteRepo.save(inv);
        return inv;
    }

    // GENERAR REPORTE RENDIMIENTO
    // TODO: Corregir modelo e implementar metodos para el calculo de rendimiento

    // OBTENER TODOS LOS REPORTES
    public List<Reporte> mostrarTodos(Long userId) {
        // Validar usuario ejecutor
        sesionClient.validarAceso(userId, 3);

        return reporteRepo.findAll();
    }

    // OBTENER REPORTES POR TIPO
    public List<Reporte> reportesByTipo(String tipo, Long userId) {
        // Validar usuario ejecutor
        sesionClient.validarAceso(userId, 3);

        if (!tipo.equalsIgnoreCase("inventario")
            && !tipo.equalsIgnoreCase("ventas")
            && !tipo.equalsIgnoreCase("rendimiento")) {
            throw new UnknownTipoReporte();
        }

        return reporteRepo.findByTipoReporte(tipo);
    }

}
