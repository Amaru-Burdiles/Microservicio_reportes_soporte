package duoc.amaru.reportes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import duoc.amaru.reportes.model.Solicitud;

public interface SolicitudRepo extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findByClienteAndTipo(Long idCliente, boolean esReclamo);
    // TODO: Test that this query is actually doing what it's supposed to
}
