package duoc.amaru.reportes.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import duoc.amaru.reportes.dto.PedidoDTO;
import duoc.amaru.reportes.dto.ResponseDTO;
import duoc.amaru.reportes.dto.VentaDTO;

@Component
public class VentaClient {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PedidoClient pedidoClient;

    private String url = "http://localhost:8084/api/ventas";

    // OBTENER LA SUMA TOTAL EN VENTAS PAGADAS
    public double calcTotalVentas() {
        // Petición a microservicio Ventas obtener todas 
        List<VentaDTO> ventas = getVentas();

        // Ids de los Pedidos pagados
        List<VentaDTO> pagadas = getVentasByEstado(ventas, "pagada");

        // Almacena la suma de los totales de los Pedidos
        double totalVentas = 0d;
        for (VentaDTO v : pagadas) {
            totalVentas += pedidoClient.getTotalPorPedido(v.getIdPedido());
        }

        return totalVentas;
    }

    // OBTENER CANTIDAD DE TRANSACCIONES
    public int getCantVentasHechas() {
        List<VentaDTO> ventas = getVentas();
        return getVentasByEstado(ventas, "pagada").size();
    }


    // OBTENER LISTA DE TRANSACCIONES (VENTAS)
    public List<VentaDTO> getVentas() {
        return restTemplate.getForObject(url, ResponseDTO.class).getData();
    }


    // FILTRAR VENTAS POR ESTADO
    public List<VentaDTO> getVentasByEstado(List<VentaDTO> original, String estado) {
        // Lista filtrada de Ventas
        List<VentaDTO> filtrada = new ArrayList<>();

        // Guarda la Venta con el estado indicado
        for (VentaDTO venta : original) {
            if (venta.getEstado().equalsIgnoreCase(estado))
                filtrada.add(venta);
        }
        return filtrada;
    }


    // OBTENER LA CANTIDAD DE CLIENTES QUE REGISTRARON UNA VENTA PAGADA
    public int getCantClientesQueCompraron() {
        // Todas las ventas
        List<VentaDTO> ventas = getVentas();

        // Todas las ventas completadas
        List<VentaDTO> filtrada = getVentasByEstado(ventas, "pagada");



        // Usuarios que registraron ventas
        Set<Long> userIds = new HashSet<>();

        // Para cada Venta se saca idPedido,
        // lo usa para sacar Pedido, que usa para sacar idCliente
        for (VentaDTO v : filtrada) {
            PedidoDTO pedido = pedidoClient.getPedidoById(v.getIdPedido()); 
            userIds.add(pedido.getIdCliente());
        }

        return userIds.size();
    }
}
