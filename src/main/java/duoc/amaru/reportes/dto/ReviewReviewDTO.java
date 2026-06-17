package duoc.amaru.reportes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewReviewDTO {
    private double avgCalificacion;
    private int cantReviews;
    private int cantReviews5;
    private int cantReviews0;
}
