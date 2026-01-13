package ar.org.centro8curos.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ar.org.centro8curos.model.Pago;

public interface PagoRepository extends JpaRepository<Pago, Integer> {

    Optional<Pago> findByTransaccionId(String transaccionId);

    List<Pago> findByPedidoIdPedido(Integer idPedido);
}