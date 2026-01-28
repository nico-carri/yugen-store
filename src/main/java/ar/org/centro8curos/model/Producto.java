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
@Table(name = "PRODUCTOS")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRODUCTO")
    private Integer idProducto;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "PRECIO", nullable = false)
    @NotNull(message = "El precio no puede ser nulo")
    private BigDecimal precio;

    @NotNull(message = "El stock no puede ser nulo")
    @Column(name = "STOCK", nullable = false)
    private Integer stock;

    @Column(name = "DESCRIPCION", nullable = true)
    private String descripcion;

    @NotBlank(message = "La categoria no puede estar vacia")
    @Column(name = "CATEGORIA", nullable = false)
    private String categoria;

    @NotBlank(message = "La imagen no puede estar vacia")
    @Column(name = "URL_IMG", nullable = false)
    private String urlImg;

    @Column(name = "ACTIVO", nullable = false)
    @Convert(converter = org.hibernate.type.NumericBooleanConverter.class)
    private boolean activo = true;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<DetallesPedido> detallesPedido;

}
