package ar.org.centro8curos.service;

import ar.org.centro8curos.model.Pedido;
import ar.org.centro8curos.dto.ItemCarritoDTO;
import java.util.List;
import java.util.Optional;

public interface IPedidoService {
    Pedido crearPedido(Integer userId, List<ItemCarritoDTO> itemsCarrito);

    Optional<Pedido> findPedidoById(Long orderId);

    List<Pedido> findPedidoHistoryByUserId(Integer userId);
    
    List<Pedido> findAll();

    long countPedidos();
}