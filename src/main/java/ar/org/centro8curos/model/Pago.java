package ar.org.centro8curos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ar.org.centro8curos.model.enums.EstadoPago;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pagos")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @NotBlank(message = "El metodo de pago no puede estar vacio")
    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;

    @NotBlank(message = "El monto no puede estar vacio")
    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @NotBlank(message = "La fecha de pago no puede estar vacia")
    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Column(name = "transaccion_id", nullable = false, unique = true)
    private String transaccionId;

    @Enumerated(EnumType.STRING)
    private EstadoPago estadoPago;
}