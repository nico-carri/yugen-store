package ar.org.centro8curos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCatalogoDTO {

    private Integer id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String urlImagen;

}