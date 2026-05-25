package duoc.amaru.reportes.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import duoc.amaru.reportes.model.ReporteInventario;
import duoc.amaru.reportes.model.ReporteVentas;
import duoc.amaru.reportes.repository.ReporteRepo;

@Service
public class ReporteServicio {
    @Autowired
    private ReporteRepo reporteRepo;

    @Autowired
    private FacturaClient facturaClient;

    @Autowired
    private ProdClient prodClient;

    
    
    // GENERAR REPORTE VENTAS
    public ResponseEntity<?> generarReporteVenta(Long userId) {
        ReporteVentas ventas = new ReporteVentas();
        ventas.setTipoReporte("Ventas");
        ventas.setFechaGeneracion(LocalDateTime.now());
        ventas.setGeneradoPor(userId);
        ventas.setFormato("JSON");

        double suma = facturaClient.getFacturas();
        ventas.setTotalVentas(suma);
        
        int pedidos = pedidoClient.getPedidos();
        ventas.setCantPedidos(pedidos);
        
        reporteRepo.save(ventas);
        return ResponseEntity.ok("Reporte generado con Id #"+ ventas.getIdReporte());
    }
    
    // GENERAR REPORTE INVENTARIO
    public ResponseEntity<?> generarReporteInv(Long userId, int umbral) {
        ReporteInventario inv = new ReporteInventario();
        inv.setTipoReporte("Inventario");
        inv.setFechaGeneracion(LocalDateTime.now());
        inv.setGeneradoPor(userId);
        inv.setFormato("JSON");
        
        int totalProds = prodClient.getTotalProductos();
        inv.setTotalProductos(totalProds);
        
        List<ProdDTO> stockBajo = prodClient.getProductosLowStock(umbral);
        inv.setProdBajoStock(stockBajo);
        
        reporteRepo.save(inv);
        return ResponseEntity.ok("Reporte generado con Id #"+ inv.getIdReporte());
    }
    
    
    // TODO: Validar usuario ejecutor
}
