package ar.org.centro8curos.repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ar.org.centro8curos.model.DetallesPedido;

public interface DetallesPedidoRepository extends JpaRepository<DetallesPedido, Integer> {
    List<DetallesPedido> findByPedidoIdPedido(Integer idPedido);

    Optional<DetallesPedido> findByPedidoIdPedidoAndProductoIdProducto(Integer idPedido, Integer idProducto);
}