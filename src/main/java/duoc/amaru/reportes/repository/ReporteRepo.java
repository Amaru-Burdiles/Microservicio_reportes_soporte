package duoc.amaru.reportes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import duoc.amaru.reportes.model.Reporte;
import java.util.List;


public interface ReporteRepo extends JpaRepository<Reporte, Long> {
    List<Reporte> findByTipoReporte(String tipoReporte);
}
