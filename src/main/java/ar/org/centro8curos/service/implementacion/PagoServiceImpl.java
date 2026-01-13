package ar.org.centro8curos.service.implementacion;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ar.org.centro8curos.model.*;
import ar.org.centro8curos.model.enums.EstadoDePedido;
import ar.org.centro8curos.model.enums.EstadoPago; // Importamos el nuevo Enum
import ar.org.centro8curos.repository.*;
import ar.org.centro8curos.service.IPagoService;

@Service
public class PagoServiceImpl implements IPagoService {

    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public PagoServiceImpl(PagoRepository pagoRepository,
            PedidoRepository pedidoRepository,
            ProductoRepository productoRepository) {
        this.pagoRepository = pagoRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional
    public Pago registrarPago(Pago pago) {

        if (pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDateTime.now());
        }

        Pedido pedido = pago.getPedido();

        // CAMBIO: Ahora comparamos usando el Enum EstadoPago
        if (pago.getEstadoPago() == EstadoPago.APROBADO) {

            pedido.setEstado(EstadoDePedido.COMPLETADO);
            reducirStock(pedido);

        } else if (pago.getEstadoPago() == EstadoPago.RECHAZADO) {

            pedido.setEstado(EstadoDePedido.CANCELADO);

        } else {
            // Caso PENDIENTE
            pedido.setEstado(EstadoDePedido.PENDIENTE_PAGO);
        }

        pedidoRepository.save(pedido);
        return pagoRepository.save(pago);
    }

    private void reducirStock(Pedido pedido) {
        if (pedido.getDetallesPedido() == null || pedido.getDetallesPedido().isEmpty()) {
            throw new RuntimeException("No se encontraron detalles para actualizar el stock.");
        }

        for (DetallesPedido detalle : pedido.getDetallesPedido()) {
            Producto producto = detalle.getProducto();
            int cantidadComprada = detalle.getCantidad();
            int nuevoStock = producto.getStock() - cantidadComprada;

            if (nuevoStock < 0) {
                throw new RuntimeException("Error: Stock insuficiente para " + producto.getNombre());
            }

            producto.setStock(nuevoStock);
            productoRepository.save(producto);
        }
    }
}