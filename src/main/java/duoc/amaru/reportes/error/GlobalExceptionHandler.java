package duoc.amaru.reportes.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

import duoc.amaru.reportes.error.exceptions.ProdNoExiste;
import duoc.amaru.reportes.error.exceptions.ReviewYaExiste;
import duoc.amaru.reportes.error.exceptions.UnknownTipoReporte;
import duoc.amaru.reportes.error.exceptions.OffLimitsException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    // Excepciones del modelo por @Valid en el enpoint Post, Put en controlador 
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> manejoErroresValidacion(MethodArgumentNotValidException ex) {
        @SuppressWarnings("unused")
        Map<String, String> errores = new HashMap<>();

        for (var error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }

        return errores;
    }

    // Excepciones procedentes de *Client
    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<String> restClientException(HttpStatusCodeException ex) {
        String error = ex.getResponseBodyAsString();

        return ResponseEntity.status(ex.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(error);
    }

    // Excepción de reporte servicio -> tipo de reporte desconocido
    @ExceptionHandler(UnknownTipoReporte.class)
    public ResponseEntity<String> reporteServicioEx(UnknownTipoReporte ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // Excepción de reseña servicio -> Id de producto no existe
    @ExceptionHandler(ProdNoExiste.class)
    public ResponseEntity<String> reviewServicioEx(ProdNoExiste ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // Excepción de reseña servicio -> Reseña duplicada por producto
    @ExceptionHandler(ReviewYaExiste.class)
    public ResponseEntity<String> reviewServicioEx(ReviewYaExiste ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // Excepción de solicitud servicio -> Sin permisos suficientes para ver la solicitud
    @ExceptionHandler(OffLimitsException.class)
    public ResponseEntity<String> solicitudServicioEx(OffLimitsException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}