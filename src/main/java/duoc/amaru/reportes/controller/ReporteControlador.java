package duoc.amaru.reportes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duoc.amaru.reportes.model.Reporte;
import duoc.amaru.reportes.model.ReporteInventario;
import duoc.amaru.reportes.model.ReporteVentas;
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
    @PostMapping("/generar/ventas/{userId}")
    public ResponseEntity<?> postReporteVentas(@PathVariable Long userId) {
        ReporteVentas reply = reporteServicio.generarReporteVenta(userId);
        return ResponseEntity.ok("Reporte generado con Id #"+ reply.getIdReporte() +'\n'+ reply);
    }
    
    // GENERAR REPORTE INVENTARIO
    @PostMapping("/generar/inventario{invId}:{umbral}/{userId}")
    public ResponseEntity<?> postReporteInv(@PathVariable Long invId, @PathVariable int umbral, @PathVariable Long userId) {   
        ReporteInventario reply = reporteServicio.generarReporteInv(userId, umbral, invId);
        return ResponseEntity.ok("Reporte generado con Id #"+ reply.getIdReporte() +'\n'+ reply);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getReportes(@PathVariable Long userId) {
        List<Reporte> reply = reporteServicio.mostrarTodos(userId);
        if (reply.isEmpty())
            return ResponseEntity.status(404).body("No se ha creado ningún reporte aún");

        return ResponseEntity.ok(reply);
    }

    @GetMapping("/tipo:{tipo}/{userId}")
    public ResponseEntity<?> getReportesByTipo(@PathVariable String tipo, @PathVariable Long userId) {
        List<Reporte> reply = reporteServicio.reportesByTipo(tipo, userId);
        if (reply.isEmpty())
            return ResponseEntity.status(404).body("Sin resultados");

        return ResponseEntity.ok(reply);
    }
}
