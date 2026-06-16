package duoc.amaru.reportes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import duoc.amaru.reportes.model.Solicitud;

public interface SolicitudRepo extends JpaRepository<Solicitud, Long> {
    
}
