package ar.org.centro8curos.service;

import java.util.List;
import ar.org.centro8curos.dto.ItemCarritoDTO;

public interface ICarritoService {
    void addItem(ItemCarritoDTO item);

    void removeItem(Integer productId);

    List<ItemCarritoDTO> getCarritoItems();

    void clearCarrito();

    int getTotalItems();

    double getTotalPrecio();

    void incrementarCantidad(Integer productId);

    void decrementarCantidad(Integer productId);
}