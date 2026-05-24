package duoc.amaru.reportes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reporte_venta")
public class ReporteVentas extends Reporte {

    private double totalVentas;
    private int cantPedidos;
    private String periodo;
}
