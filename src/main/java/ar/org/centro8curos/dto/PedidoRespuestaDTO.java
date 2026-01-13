package ar.org.centro8curos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRespuestaDTO {

    private Integer idPedido;
    private LocalDateTime fechaPedido;
    private BigDecimal total;
    private String estado;

    List<DetallePedidoRespuestaDTO> detallesPedido;
}
