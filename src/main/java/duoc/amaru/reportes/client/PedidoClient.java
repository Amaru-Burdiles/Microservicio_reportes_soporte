package duoc.amaru.reportes.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PedidoClient {
    @Autowired
    private RestTemplate restTemplate;

    // Conexión Dairys Servicio Pedidos
    public int getPedidos() {
        // TODO: Completar url
        return restTemplate.getForObject("http://localhost:8085", int.class);
    }
}
