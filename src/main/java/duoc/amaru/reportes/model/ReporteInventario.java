package duoc.amaru.reportes.model;

import java.util.List;

import jakarta.persistence.Column;
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
@Table(name = "reporte_inventario")
public class ReporteInventario extends Reporte {

    private int totalProductos;

    @Column(name = "prod_id_stock_bajo")
    private List<ProdDTO> prodBajoStock;
    private String periodo;
}
