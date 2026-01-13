package ar.org.centro8curos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCarritoDTO {
    private Integer idProducto;
    private String nombre;
    private Double precio;
    private String urlImg;
    private Integer cantidadDeseada;

    public Double getSubTotal() {
        return precio * cantidadDeseada;
    }
}