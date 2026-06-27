package duoc.amaru.reportes.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reporte_rendimiento")
public class ReporteRendimiento extends Reporte {
    private double ventasPorTienda;

    @Column(name = "prod_id_mas_vendido")
    private List<Long> itemsMasVendidos;
}

// TODO: Corregir de acuerdo a casos de uso adaptar para los calculos correspondientes
