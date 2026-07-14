package duoc.amaru.reportes.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Solicitud crearSoli(CrearSoliDTO soli, Long userId, boolean esReclamo) {
        // Validar usuario
        sesionClient.validarCliente(userId);

        // Creación de solicitud
        Solicitud newSolicitud = new Solicitud();
        newSolicitud.setIdCliente(userId);
        newSolicitud.setAsunto(soli.getAsunto());
        newSolicitud.setDescripcion(soli.getDescripcion());
        newSolicitud.setFechaCreacion(LocalDateTime.now());
        newSolicitud.setEstado("Sin revisar");
        newSolicitud.setReclamo(esReclamo);

        return solicitudRepo.save(newSolicitud);
    }

    // TODO: Implementar crud y demas casos de uso
    //       consultar diagrama de casos de uso

    // CONSULTAR HISTORIAL
    public List<Solicitud> getHistorial(Long userId, boolean deReclamos) {
        // Validar usuario
        sesionClient.validarCliente(userId);

        if (deReclamos)
            return solicitudRepo.findByClienteAndTipo(userId, deReclamos);

        return solicitudRepo.findByClienteAndTipo(userId, deReclamos);
    }


    // CONSULTAR SOLICITUDES 

}
