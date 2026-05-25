package duoc.amaru.reportes.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    @NotNull(message = "La calificación es obligatoria")
    @Max(value = 5, message = "La calificación máxima es 5")
    @Min(value = 0, message = "La calificación mínima es 0")
    private int calificacion;

    private String comentario;
}
