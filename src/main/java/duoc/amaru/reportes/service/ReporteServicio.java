package duoc.amaru.reportes.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import duoc.amaru.reportes.client.FacturaClient;
import duoc.amaru.reportes.client.PedidoClient;
import duoc.amaru.reportes.client.ProdClient;
import duoc.amaru.reportes.client.SesionClient;
import duoc.amaru.reportes.dto.ProdDTO;
import duoc.amaru.reportes.model.Reporte;
import duoc.amaru.reportes.model.ReporteInventario;
import duoc.amaru.reportes.model.ReporteVentas;
import duoc.amaru.reportes.repository.ReporteRepo;

@Service
public class ReporteServicio {
    @Autowired // Repo Reporte
    private ReporteRepo reporteRepo;

    @Autowired // Client Factura
    private FacturaClient facturaClient;

    @Autowired // Client Producto
    private ProdClient prodClient;

    @Autowired // Client Pedido
    private PedidoClient pedidoClient;

    @Autowired
    private SesionClient sesionClient;
    
    // GENERAR REPORTE VENTAS
    public ResponseEntity<?> generarReporteVenta(Long userId) {
        // Validar usuario ejecutor
        ResponseEntity<?> reply = sesionClient.validarAceso(userId, 3);
        if (reply != null)
            return reply;

        ReporteVentas ventas = new ReporteVentas();
        ventas.setTipoReporte("Ventas");
        ventas.setFechaGeneracion(LocalDateTime.now());
        ventas.setGeneradoPor(userId);
        ventas.setFormato("JSON");

        double suma = facturaClient.getTotalFacturas();
        ventas.setTotalVentas(suma);
        
        int pedidos = pedidoClient.getPedidos();
        ventas.setCantPedidos(pedidos);
        
        reporteRepo.save(ventas);
        return ResponseEntity.ok("Reporte generado con Id #"+ ventas.getIdReporte());
    }
    
    // GENERAR REPORTE INVENTARIO
    public ResponseEntity<?> generarReporteInv(Long userId, int umbral, Long invId) {
        // Validar usuario ejecutor
        ResponseEntity<?> reply = sesionClient.validarAceso(userId, 3);
        if (reply != null)
            return reply;

        ReporteInventario inv = new ReporteInventario();
        inv.setTipoReporte("Inventario");
        inv.setFechaGeneracion(LocalDateTime.now());
        inv.setGeneradoPor(userId);
        inv.setFormato("JSON");
        
        int totalProds = prodClient.getTotalProductos();
        inv.setTotalProductos(totalProds);
        
        List<ProdDTO> stockBajo = prodClient.getProductosLowStock(umbral, invId);
        inv.setProdBajoStock(stockBajo);
        
        reporteRepo.save(inv);
        return ResponseEntity.ok("Reporte generado con Id #"+ inv.getIdReporte());
    }

    // OBTENER TODOS LOS REPORTES
    public ResponseEntity<?> mostrarTodos(Long userId) {
        // Validar usuario ejecutor
        ResponseEntity<?> reply = sesionClient.validarAceso(userId, 3);
        if (reply != null)
            return reply;

        List<Reporte> reportes = reporteRepo.findAll();
        if (reportes.isEmpty())
            return ResponseEntity.status(404).body("No se ha creado ningún reporte aún");

        return ResponseEntity.ok(reportes);
    }

    // OBTENER REPORTES POR TIPO
    public ResponseEntity<?> reportesByTipo(String tipo, Long userId) {
        // Validar usuario ejecutor
        ResponseEntity<?> reply = sesionClient.validarAceso(userId, 3);
        if (reply != null)
            return reply;

        if (!tipo.equalsIgnoreCase("inventario") || !tipo.equalsIgnoreCase("ventas"))
            return ResponseEntity.badRequest().body("Tipo de reporte desconocido");

        return ResponseEntity.ok(reporteRepo.findByTipoReporte(tipo));
    }

}
