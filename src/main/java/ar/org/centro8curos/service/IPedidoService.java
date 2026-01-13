package ar.org.centro8curos.service;

import java.util.List;
import ar.org.centro8curos.model.Pedido;
import ar.org.centro8curos.dto.ItemCarritoDTO;

public interface IPedidoService {
    Pedido crearPedido(Integer userId, List<ItemCarritoDTO> itemsCarrito);

    Pedido findPedidoById(Integer orderId);

    List<Pedido> findPedidoHistoryByUserId(Integer userId);
}