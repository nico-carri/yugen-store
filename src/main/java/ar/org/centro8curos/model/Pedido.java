package ar.org.centro8curos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import ar.org.centro8curos.model.enums.EstadoDePedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PEDIDOS")
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PEDIDO")
    private Long idPedido;

    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado no puede ser nulo")
    @Column(name = "ESTADO", nullable = false)
    private EstadoDePedido estado;

    @NotNull(message = "El total no puede ser nulo")
    @Column(name = "TOTAL", nullable = false)
    private BigDecimal total;

    @Column(name = "SALESFORCE_ORDER_ID", unique = true)
    private String salesforceOrderId;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    @NotNull(message = "El usuario no puede ser nulo")
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallesPedido> detallesPedido;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<Pago> pagos;


}
