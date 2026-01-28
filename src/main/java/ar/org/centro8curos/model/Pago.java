package ar.org.centro8curos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ar.org.centro8curos.model.enums.EstadoPago;
import ar.org.centro8curos.model.enums.MetodoPago;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PAGOS")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PAGO")
    private Integer idPago;

    @NotNull(message = "El método de pago no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "METODO_PAGO", nullable = false)
    private MetodoPago metodoPago;

    @NotNull(message = "El monto no puede ser nulo")
    @Column(name = "MONTO", nullable = false)
    private BigDecimal monto;

    @NotNull(message = "La fecha de pago no puede ser nula")
    @Column(name = "FECHA_PAGO", nullable = false)
    private LocalDateTime fechaPago;

    @Column(name = "TRANSACCION_ID  ", nullable = false, unique = true)
    private String transaccionId;

    @Enumerated(EnumType.STRING)
    private EstadoPago estadoPago;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;
}