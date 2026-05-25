package duoc.amaru.reportes.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SesionClient {
    @Autowired
    private RestTemplate restTemplate;

    private String serviceUrl = "http://localhost:8086/api/v1/sesiones";

    // VERIFICA QUE EL EMPLEADO TENGO UN ACCESO SUPERIOR A FILTRO 
    public ResponseEntity<?> validarAceso(Long ejecutorId, int filtro) {
        String url = serviceUrl + "/access-validation/exe:"+ ejecutorId +"/filter:"+ filtro;
        return restTemplate.getForObject(url, ResponseEntity.class);
    }

    // VERIFICA SI EL USUARIO ESTA LOGUEADO
    public boolean estaLogueado(Long userId) {
        String url = serviceUrl + "/is-logged-in/user:" + userId;
        return restTemplate.getForObject(url, boolean.class);
    }

    // VERIFICAR QUE SE TRATA DE UN CLIENTE Y ESTA LOGUEADO
    public ResponseEntity<?> validarCliente(Long userId) {
        String url = serviceUrl + "/validacion-cliente/user:" + userId;
        return restTemplate.getForObject(url, ResponseEntity.class);
    }
}
