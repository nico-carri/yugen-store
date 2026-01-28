package ar.org.centro8curos.service.implementacion;

import ar.org.centro8curos.dto.ItemCarritoDTO;
import ar.org.centro8curos.service.ICarritoService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CarritoServiceImpl implements ICarritoService {

    private List<ItemCarritoDTO> cartItems = new ArrayList<>();

    @Override
    public void addItem(ItemCarritoDTO newItem) {
        Optional<ItemCarritoDTO> existingItem = cartItems.stream()
                .filter(i -> i.getIdProducto().equals(newItem.getIdProducto()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get()
                    .setCantidadDeseada(existingItem.get().getCantidadDeseada() + newItem.getCantidadDeseada());
        } else {
            cartItems.add(newItem);
        }
    }

    @Override
    public void removeItem(Integer productId) {
        cartItems.removeIf(i -> i.getIdProducto().equals(productId));
    }

    @Override
    public List<ItemCarritoDTO> getCarritoItems() {
        return cartItems;
    }

    @Override
    public void clearCarrito() {
        cartItems.clear();
    }

    @Override
    public int getTotalItems() {
        return cartItems.stream().mapToInt(ItemCarritoDTO::getCantidadDeseada).sum();
    }

    @Override
    public double getTotalPrecio() {
        double total = cartItems.stream()
                .mapToDouble(item -> item.getPrecio() * item.getCantidadDeseada())
                .sum();
        return Math.round(total * 100.0) / 100.0;
    }

    @Override
    public void incrementarCantidad(Integer productId) {
        cartItems.stream()
                .filter(i -> i.getIdProducto().equals(productId))
                .findFirst()
                .ifPresent(i -> {
                    i.setCantidadDeseada(i.getCantidadDeseada() + 1);
                });
    }

    @Override
    public void decrementarCantidad(Integer productId) {
        getCarritoItems().stream()
                .filter(i -> i.getIdProducto().equals(productId))
                .findFirst()
                .ifPresent(i -> {
                    if (i.getCantidadDeseada() > 1) {
                        i.setCantidadDeseada(i.getCantidadDeseada() - 1);
                    } else {
                        removeItem(productId);
                    }
                });
    }
}