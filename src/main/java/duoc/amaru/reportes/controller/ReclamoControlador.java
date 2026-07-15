package duoc.amaru.reportes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duoc.amaru.reportes.dto.CrearSoliDTO;
import duoc.amaru.reportes.model.Solicitud;
import duoc.amaru.reportes.service.SolicitudServicio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("api/v1/reclamos")
public class ReclamoControlador {
    @Autowired
    private SolicitudServicio solicitudServicio;

    // CREAR RECLAMO
    @PostMapping("/crear/exe:{userId}")
    public ResponseEntity<?> postReclamo(@RequestBody CrearSoliDTO soli, @PathVariable Long userId) {
        Solicitud reply = solicitudServicio.crearSolicitud(soli, userId, true);
        return ResponseEntity.ok("Reclamo creado y publicado con id #"+ reply.getIdSolicitud());
    }

    // VER HISTORIAL DE RECLAMOS
    @GetMapping("/cliente:{id}")
    public ResponseEntity<?> getHistorial(@PathVariable Long id) {
        List<Solicitud> reply = solicitudServicio.verHistorial(id, true);
        if (reply.isEmpty())
            return ResponseEntity.status(404).body("No tienes reclamos registrados");

        return ResponseEntity.status(404).body(reply);
    }

    // FILTRAR HISTORIAL POR ESTADO DEL RECLAMO
    @GetMapping("/cliente:{id}/{estado}")
    public ResponseEntity<?> getHistorialPorEstado(@PathVariable Long id, @PathVariable String estado) {
        List<Solicitud> reply = solicitudServicio.findHistorialByEstado(id, true, estado);
        if (reply.isEmpty())
            return ResponseEntity.status(404).body("Sin resultados");

        return ResponseEntity.status(404).body(reply);
    }

    // OBTENER RECLAMO POR ID
    @GetMapping("/usuario:{userId}/reclamo:{soliId}")
    public ResponseEntity<?> getReclamoById(@PathVariable Long userId, @PathVariable Long soliId) {
        Solicitud reply = solicitudServicio.getSolicitud(userId, soliId);
        if (reply == null)
            return ResponseEntity.badRequest().body("Solicitud no encontrada");
        return ResponseEntity.ok(reply);
    }

    // FILTRAR RECLAMOS POR ESTADO
    @GetMapping("/empleado:{id}/{estado}")
    public ResponseEntity<?> getReclamosByEstado(@PathVariable Long id, @PathVariable String estado) {
        List<Solicitud> reply = solicitudServicio.getReclamosByEstado(id, estado);
        if (reply == null)
            return ResponseEntity.badRequest().body("Estado inválido");

        if (reply.isEmpty())
            return ResponseEntity.status(404).body("Sin resultados");

        return ResponseEntity.ok(reply);
    }

    // MARCAR RECLAMO COMO REVISADO
    @PutMapping("empleado:{userId}/en-revision/{soliId}")
    public ResponseEntity<?> putRevisarReclamo(@PathVariable Long userId, @PathVariable Long soliId) {
        boolean reply = solicitudServicio.marcarEnRevision(userId, soliId);
        if (reply)
            return ResponseEntity.ok("Se cambió el estado de la solicitud");

        return ResponseEntity.badRequest().body("Error: Solicitud no encontrada o la acción es imposible");
    }
    
    // MARCAR RECLAMO COMO RESUELTO
    @PutMapping("empleado:{userId}/resolver/{soliId}")
    public ResponseEntity<?> putResolverReclamo(@PathVariable Long userId, @PathVariable Long soliId) {
        boolean reply = solicitudServicio.marcarResulta(userId, soliId);
        if (reply)
            return ResponseEntity.ok("Se marcó la solicitud como resuelta");

        return ResponseEntity.badRequest().body("Error: La solicitud no existe o está cerrada");
    }

    // MARCAR RECLAMO COMO CERRADO
    @PutMapping("empleado:{userId}/cerrar/{soliId}")
    public ResponseEntity<?> putCerrarReclamo(@PathVariable Long userId, @PathVariable Long soliId) {
        boolean reply = solicitudServicio.cerrarSolicictud(userId, soliId);
        if (reply)
            return ResponseEntity.ok("Se cerró la solicitud");

        return ResponseEntity.badRequest().body("Error: La solicitud no existe o está resuelta");
    }
}
