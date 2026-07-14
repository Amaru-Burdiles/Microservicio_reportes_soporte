package duoc.amaru.reportes.client;

import java.lang.reflect.Array;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UsuarioClient {
    @Autowired
    private RestTemplate restTemplate;

    private String url = "http://localhost:8086/api/v1/usuarios";

    // OBTENER CANTIDAD DE CLIENTES REGISTRADOS
    public int getClientesRegistrados() {
        Object[] reply = restTemplate.getForObject(url, Object[].class);
        return Array.getLength(reply);
        // TODO: Implement on the other side and fix if necessary
    }
}
