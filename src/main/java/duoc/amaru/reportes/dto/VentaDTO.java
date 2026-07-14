package duoc.amaru.reportes.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VentaDTO {
    private Long idVenta;
    private Long idPedido;
    private LocalDateTime fechaVenta;
    private String estado;
}
