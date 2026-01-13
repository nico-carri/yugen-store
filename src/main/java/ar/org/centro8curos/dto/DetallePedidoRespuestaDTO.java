package ar.org.centro8curos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoRespuestaDTO {

    private String nombreProducto;
    private String urlImagen;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subTotal;
}