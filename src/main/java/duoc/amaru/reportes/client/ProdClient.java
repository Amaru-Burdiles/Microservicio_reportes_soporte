package duoc.amaru.reportes.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import duoc.amaru.reportes.dto.ProdDTO;

@Component
public class ProdClient {
    @Autowired
    private RestTemplate restTemplate;
    
    // url del servicio
    private String serviceUrl = "http://localhost:8081/api/ecomarket/v1/productos";

    // CONEXIÓN OBTENER TOTAL PRODUCTOS
    public int getTotalProductos() {
        // url especifica
        String url = serviceUrl + "";
        return restTemplate.getForObject(url, int.class);
    }

    // CONEXIÓN OBTENER LISTA PRODUCTOS BAJO STOCK
    public List<ProdDTO> getProductosLowStock(int umbral, Long inv) {
        // url especifica
        String url = serviceUrl +"/inventario/"+ inv +"/"+ umbral;
        ProdDTO[] reply = restTemplate.getForObject(url, ProdDTO[].class);

        // Conversión Array a List<ProdDTO>
        List<ProdDTO> lista = Arrays.asList(reply);

        return lista;
    }
}
