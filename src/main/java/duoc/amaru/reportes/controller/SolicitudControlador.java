package duoc.amaru.reportes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duoc.amaru.reportes.dto.CrearSoliDTO;
import duoc.amaru.reportes.service.SolicitudServicio;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/solicitudes")
public class SolicitudControlador {
    @Autowired
    private SolicitudServicio solicitudServicio;

    @PostMapping("/crear/{tipo}/exe:{userId}")
    public ResponseEntity<?> postSolicitud(@RequestBody CrearSoliDTO soli, @PathVariable Long userId, @PathVariable String tipo) {
        solicitudServicio.crearSoli(soli, userId, tipo);
        return ResponseEntity.ok("Solicitud creada");
    }
    
}
