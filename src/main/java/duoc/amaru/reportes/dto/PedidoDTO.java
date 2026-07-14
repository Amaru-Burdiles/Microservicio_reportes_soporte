package duoc.amaru.reportes.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PedidoDTO {
    private Long idPedido;
    private Long idCliente;
    private LocalDateTime fechaPedido;
    private String estadoPedido;
    private String direccion;
    private Double total;
    private List<Object> detalles;
}
