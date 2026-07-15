package duoc.amaru.reportes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import duoc.amaru.reportes.model.Solicitud;

public interface SolicitudRepo extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findAllByIdCliente(Long idCliente);

    List<Solicitud> findAllByEstado(String estado);

    List<Solicitud> findByReclamoAndEstado(boolean esReclamo, String estado);
}
