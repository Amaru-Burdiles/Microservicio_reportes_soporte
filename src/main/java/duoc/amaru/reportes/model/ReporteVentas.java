package duoc.amaru.reportes.model;

import java.time.LocalDateTime;

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

    public ReporteVentas(String tipo, LocalDateTime fecha, Long idEmp, String formato, String periodo, double totalVentas, int cantPedidos) {
        super(null, tipo, fecha, idEmp, formato, periodo);
        this.totalVentas = totalVentas;
        this.cantPedidos = cantPedidos;
    }
}
