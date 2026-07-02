package duoc.amaru.reportes.model;

import java.time.LocalDateTime;

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
    
    private int ventasTotales;
    private double ticketPromedio;
    private double tasaConversion;
    private double margenBruto;
    private int rotacionInv;

    public ReporteRendimiento(String tipo,
                              LocalDateTime fecha,
                              Long idEmp,
                              String formato,
                              String periodo,
                              int ventasTotales,
                              double ticketPromedio,
                              double tasaConversion,
                              double margenBruto,
                              int rotacionInv
                            ) {
        super(null, tipo, fecha, idEmp, formato, periodo);
        this.ventasTotales = ventasTotales;
        this.ticketPromedio = ticketPromedio;
        this.tasaConversion = tasaConversion;
        this.margenBruto = margenBruto;
        this.rotacionInv = rotacionInv;
    }
}
