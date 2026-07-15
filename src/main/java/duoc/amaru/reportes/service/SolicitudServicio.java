package duoc.amaru.reportes.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import duoc.amaru.reportes.client.SesionClient;
import duoc.amaru.reportes.dto.CrearSoliDTO;
import duoc.amaru.reportes.error.exceptions.OffLimitsException;
import duoc.amaru.reportes.model.Solicitud;
import duoc.amaru.reportes.repository.SolicitudRepo;

@Service
public class SolicitudServicio {
    @Autowired // Repo Solicitud
    private SolicitudRepo solicitudRepo;

    @Autowired // Client Sesion
    private SesionClient sesionClient;

    // Estados de solicitud permitidos
    private Set<String> estadosSolicitud = Set.of("pendiente", "en revision", "resuelto", "cerrado");

    // CREAR SOLICITUD
    public Solicitud crearSolicitud(CrearSoliDTO soli, Long userId, boolean esReclamo) {
        // Validar usuario
        sesionClient.validarCliente(userId);

        // Creación de solicitud
        Solicitud newSolicitud = new Solicitud();
        newSolicitud.setIdCliente(userId);
        newSolicitud.setAsunto(soli.getAsunto());
        newSolicitud.setDescripcion(soli.getDescripcion());
        newSolicitud.setFechaCreacion(LocalDateTime.now());
        newSolicitud.setEstado("Pendiente");
        newSolicitud.setReclamo(esReclamo);

        return solicitudRepo.save(newSolicitud);
    }

    // CONSULTAR HISTORIAL [reclamo XOR soporte]
    public List<Solicitud> verHistorial(Long userId, boolean deReclamos) {
        // Validar usuario
        sesionClient.validarCliente(userId);

        // Todas las solicitudes (reclamos y soporte)
        List<Solicitud> todas = solicitudRepo.findAllByIdCliente(userId);
        return filtrarPorTipo(todas, deReclamos);
    }

    // FILTRAR HISTORIAL POR ESTADO [reclamo XOR soporte]
    public List<Solicitud> findHistorialByEstado(Long userId, boolean esReclamo, String estado) {
        // Validar usuario
        sesionClient.validarCliente(userId);

        // Validar "estado" es valido
        if (!estadosSolicitud.contains(estado.toLowerCase()))
            return null;

        // Todas las solicitudes de userId
        List<Solicitud> todas = solicitudRepo.findAllByIdCliente(userId);
        // Todas las solicitudes de useriId y del tipo requerido
        List<Solicitud> tipoRequerido = filtrarPorTipo(todas, esReclamo);
        List<Solicitud> porEstado = new ArrayList<>();
        
        // Filtra las solicitudes del tipo requerido por estado
        for (Solicitud soli : tipoRequerido) {
            if (soli.getEstado().equalsIgnoreCase(estado))
                porEstado.add(soli);    
        }

        return porEstado;
    }

    // OBTENER SOLICITUD [reclamo XOR soporte]
    public Solicitud getSolicitud(Long userId, Long idSoli) {
        // Validar usuario
        sesionClient.validarUsuario(userId);

        // Validar la solicitud existe
        Solicitud solicitud = solicitudRepo.findById(idSoli).orElse(null);
        if (solicitud == null)
            return null;

        // Validar usuario es empleado y tiene permisos suficientes
        if (sesionClient.validarEmpleado(idSoli, 1))
            return solicitud;

        // Validar que userId es dueño de la solicitud
        if (solicitud.getIdCliente() == userId)
            return solicitud;

        throw new OffLimitsException();
    }

    // FILTRAR RECLAMOS POR ESTADO [reclamo]
    public List<Solicitud> getReclamosByEstado(Long exeId, String estado) {
        // Validar empleado
        sesionClient.validarEmpleado(exeId, 1);

        if (!estadosSolicitud.contains(estado.toLowerCase()))
            return null;

        return solicitudRepo.findByReclamoAndEstado(true, estado.toLowerCase());
    }

    // MARCAR SOLICITUD COMO EN REVISION
    public boolean marcarEnRevision(Long userId, Long idSoli) {
        // Validar empleado
        sesionClient.validarEmpleado(idSoli, 1);

        // Validar la solicitud existe
        Solicitud solicitud = solicitudRepo.findById(idSoli).orElse(null);
        if (solicitud == null)
            return false;

        String estado = solicitud.getEstado();

        if (!estado.equals("pendiente"))
            return false;

        solicitud.setEstado("En Revision");
        solicitudRepo.save(solicitud);
        return true;
    }


    // MARCAR SOLICITUD COMO RESUELTA
    public boolean marcarResulta(Long userId, Long idSoli) {
        // Validar empleado
        sesionClient.validarEmpleado(idSoli, 1);

        // Validar la solicitud existe
        Solicitud solicitud = solicitudRepo.findById(idSoli).orElse(null);
        if (solicitud == null)
            return false;

        String estado = solicitud.getEstado();

        if (estado.equals("cerrado"))
            return false;

        solicitud.setEstado("Resuelto");
        solicitudRepo.save(solicitud);
        return true;
    }


    // CERRAR SOLICITUD
    public boolean cerrarSolicictud(Long userId, Long idSoli) {
        // Validar empleado
        sesionClient.validarEmpleado(idSoli, 1);

        // Validar la solicitud existe
        Solicitud solicitud = solicitudRepo.findById(idSoli).orElse(null);
        if (solicitud == null)
            return false;

        String estado = solicitud.getEstado();

        if (estado.equals("resuelto"))
            return false;

        solicitud.setEstado("Cerrado");
        solicitudRepo.save(solicitud);
        return true;
    }


    // FILTRAR SOLICITUDES POR TIPO (RECLAMO || SOPORTE)
    private List<Solicitud> filtrarPorTipo(List<Solicitud> lista, boolean esReclamo) {
        List<Solicitud> tipoRequerido = new ArrayList<>();
        
        for (Solicitud soli : lista) {
            if (soli.isReclamo())
                tipoRequerido.add(soli);
        }

        return tipoRequerido;
    }
}
