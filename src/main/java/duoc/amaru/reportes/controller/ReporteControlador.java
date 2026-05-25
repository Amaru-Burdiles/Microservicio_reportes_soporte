package duoc.amaru.reportes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duoc.amaru.reportes.service.ReporteServicio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("api/v1/reportes")
public class ReporteControlador {
    @Autowired
    private ReporteServicio reporteServicio;

    // GENERAR REPORTE VENTAS
    @PostMapping("/generar/ventas/{id}")
    public ResponseEntity<?> postReporteVentas(@PathVariable Long userId) {
        return reporteServicio.generarReporteVenta(userId);
    }
    
    // GENERAR REPORTE INVENTARIO
    @PostMapping("/generar/inventario{invId}:{umbral}/{id}")
    public ResponseEntity<?> postReporteInv(@PathVariable Long invId, @PathVariable int umbral, @PathVariable Long userId) {   
        return reporteServicio.generarReporteInv(userId, umbral, invId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getReportes(@PathVariable Long userId) {
        return reporteServicio.mostrarTodos(userId);
    }

    @GetMapping("/tipo:{tipo}/{userId}")
    public ResponseEntity<?> getReportesByTipo(@PathVariable String tipo, @PathVariable Long userId) {
        return reporteServicio.reportesByTipo(tipo, userId);
    }
}
