package duoc.amaru.reportes.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FacturaClient {
    @Autowired
    private RestTemplate restTemplate;

    // Conexión Dairys Servicio Pagos
    public double getTotalFacturas() {
        // TODO: completar url
        return restTemplate.getForObject("http://localhost:8084", double.class);
    }
}
