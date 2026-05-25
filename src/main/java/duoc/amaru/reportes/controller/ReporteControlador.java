package duoc.amaru.reportes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duoc.amaru.reportes.service.ReporteServicio;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("api/v1/reportes")
public class ReporteControlador {
    @Autowired
    private ReporteServicio reporteServicio;

    // GENERAR REPORTE
    @PostMapping("/generar/ventas/{id}")
    public ResponseEntity<?> postReporteVentas(@PathVariable Long userId) {
        return reporteServicio.generarReporteVenta(userId);
    }
    
    @PostMapping("/generar/inv:{umbral}/{id}")
    public ResponseEntity<?> postReporteInv(@PathVariable int umbral, @PathVariable Long userId) {   
        return reporteServicio.generarReporteInv(userId, umbral);
    }
    
}
