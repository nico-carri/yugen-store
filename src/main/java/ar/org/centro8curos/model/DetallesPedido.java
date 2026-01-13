package ar.org.centro8curos.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "detalles_pedido")
public class DetallesPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetallesPedido;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    @NotNull(message = "El pedido no puede estar vacio")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    @NotNull(message = "El producto no puede estar vacio")
    private Producto producto;

    @NotNull(message = "La cantidad no puede estar vacia")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull(message = "El precio unitario no puede estar vacio")
    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    @Column(name = "sub_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal subTotal;

    public BigDecimal getSubTotal() {
        if (precioUnitario == null || cantidad == null)
            return BigDecimal.ZERO;
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

}
