package duoc.amaru.reportes.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "reporte_inventario")
public class ReporteInventario extends Reporte {

    private int totalProductos;

    @Column(name = "prod_id_stock_bajo")
    private List<Long> prodBajoStock = new ArrayList<>();

    public ReporteInventario(String tipo, LocalDateTime fecha, Long idEmp, String formato, String periodo, int totalProds, List<Long> prodBajoStock) {
        super(null, tipo, fecha, idEmp, formato, periodo);
        this.totalProductos = totalProds;
        this.prodBajoStock = prodBajoStock;
    }
}
