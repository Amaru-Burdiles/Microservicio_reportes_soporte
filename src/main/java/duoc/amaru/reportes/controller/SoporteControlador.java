package duoc.amaru.reportes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duoc.amaru.reportes.dto.CrearSoliDTO;
import duoc.amaru.reportes.model.Solicitud;
import duoc.amaru.reportes.service.SolicitudServicio;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/soporte")
public class SoporteControlador {
    @Autowired
    private SolicitudServicio solicitudServicio;

    @PostMapping("/crear/exe:{userId}")
    public ResponseEntity<?> postSolicitud(@RequestBody CrearSoliDTO soli, @PathVariable Long userId) {
        Solicitud reply = solicitudServicio.crearSoli(soli, userId, false);
        return ResponseEntity.ok("Solicitud de soporte creada y publicada con id #"+ reply.getIdSolicitud());
    }
    
}
