package duoc.amaru.reportes.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "queja_soporte")
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSolicitud;
    private Long idCliente;

    @NotBlank(message = "El asunto de la solicitud es obligatorio")
    @Size(max = 50)
    private String asunto;

    private String descripcion;
    private LocalDateTime fechaCreacion;
    private String estado;
    private boolean reclamo;
}
