package ar.org.centro8curos.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "precio", nullable = false)
    @NotNull(message = "El precio no puede ser nulo")
    private BigDecimal precio;

    @NotNull(message = "El stock no puede ser nulo")
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "descripcion", nullable = true)
    private String descripcion;

    @NotBlank(message = "La categoria no puede estar vacia")
    @Column(name = "categoria", nullable = false)
    private String categoria;

    @NotBlank(message = "La imagen no puede estar vacia")
    @Column(name = "urlImg", nullable = false)
    private String urlImg;

    private boolean activo = true;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<DetallesPedido> detallesPedido;

}
