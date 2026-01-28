package ar.org.centro8curos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.org.centro8curos.model.Pedido;
import ar.org.centro8curos.model.enums.EstadoDePedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    Optional<Pedido> findByUsuarioIdUserAndEstado(Long idUser, EstadoDePedido estado);

    List<Pedido> findByUsuarioIdUserAndEstadoNot(Long idUser, EstadoDePedido estadoExcluir);

    List<Pedido> findByUsuarioIdUserOrderByFechaDesc(Integer userId);

}