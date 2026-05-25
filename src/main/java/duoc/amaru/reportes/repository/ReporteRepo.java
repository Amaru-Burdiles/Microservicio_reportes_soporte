package duoc.amaru.reportes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import duoc.amaru.reportes.model.Reporte;

public interface ReporteRepo extends JpaRepository<Reporte, Long> {
    
}
