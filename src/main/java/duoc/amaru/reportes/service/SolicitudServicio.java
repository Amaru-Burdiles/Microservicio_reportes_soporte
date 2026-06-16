package duoc.amaru.reportes.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import duoc.amaru.reportes.client.SesionClient;
import duoc.amaru.reportes.dto.CrearSoliDTO;
import duoc.amaru.reportes.model.Solicitud;
import duoc.amaru.reportes.repository.SolicitudRepo;

@Service
public class SolicitudServicio {
    @Autowired // Repo Solicitud
    private SolicitudRepo solicitudRepo;

    @Autowired // Client Sesion
    private SesionClient sesionClient;

    // CREAR SOLICITUD
    public ResponseEntity<?> crearSoli(CrearSoliDTO soli, Long userId, String tipoSoli) {
        // Validar usuario
        ResponseEntity<?> reply = sesionClient.validarCliente(userId);
        if (reply != null)
            return reply;

        // Creación de solicitud
        Solicitud newSolicitud = new Solicitud();
        newSolicitud.setIdCliente(userId);
        newSolicitud.setAsunto(soli.getAsunto());
        newSolicitud.setDescripcion(soli.getDescripcion());
        newSolicitud.setFechaCreacion(LocalDateTime.now());
        newSolicitud.setEstado("Sin revisar");
        newSolicitud.setTipoSoli(tipoSoli);

        solicitudRepo.save(newSolicitud);
        return ResponseEntity.ok("Solicitud creada");
    }
}
