package duoc.amaru.reportes.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import duoc.amaru.reportes.dto.PedidoDTO;
import duoc.amaru.reportes.dto.ResponseDTO;

@Component
public class PedidoClient {
    @Autowired
    private RestTemplate restTemplate;

    private String url = "http://localhost:8085/api/ecomarket/v1/pedido";

    // Conexión Dairys Servicio Pedidos
    public int getCantPedidos() {
        List<PedidoDTO> pedidos = restTemplate.getForObject(url, ResponseDTO.class).getData();

        int size = 0;
        for (PedidoDTO pedido : pedidos) {
            if (pedido.getEstadoPedido().equalsIgnoreCase("entregado"))
                size ++;
        }
        
        return size;
    }

    public double getTotalPorPedido(Long idPedido) {
        return restTemplate.getForObject(url +'/'+ idPedido, PedidoDTO.class).getTotal();
    }


    // OBTENER PEDIDO POR ID
    public PedidoDTO getPedidoById(Long idPedido) {
        return restTemplate.getForObject(url +'/'+ idPedido, PedidoDTO.class);
    }
}
