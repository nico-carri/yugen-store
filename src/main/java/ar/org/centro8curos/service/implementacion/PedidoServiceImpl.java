package ar.org.centro8curos.service.implementacion;

import ar.org.centro8curos.dto.ItemCarritoDTO;
import ar.org.centro8curos.model.DetallesPedido;
import ar.org.centro8curos.model.Pedido;
import ar.org.centro8curos.model.Producto;
import ar.org.centro8curos.model.Usuario;
import ar.org.centro8curos.model.enums.EstadoDePedido;
import ar.org.centro8curos.repository.PedidoRepository;
import ar.org.centro8curos.service.IPedidoService;
import ar.org.centro8curos.service.IProductoService;
import ar.org.centro8curos.service.IUsuarioService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoServiceImpl implements IPedidoService {

    private final PedidoRepository pedidoRepository;
    private final IUsuarioService usuarioService;
    private final IProductoService productoService;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, IUsuarioService usuarioService,
            IProductoService productoService) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    @Override
    @Transactional
    public Pedido crearPedido(Integer userId, List<ItemCarritoDTO> itemsCarrito) {

        Usuario usuario = usuarioService.getUsuarioById(userId);

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setUsuario(usuario);
        nuevoPedido.setFecha(LocalDateTime.now());
        nuevoPedido.setEstado(EstadoDePedido.PENDIENTE_PAGO);

        List<DetallesPedido> detalles = new ArrayList<>();
        BigDecimal totalPedido = BigDecimal.ZERO;

        for (ItemCarritoDTO item : itemsCarrito) {
            Producto producto = productoService.findById(item.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + item.getIdProducto()));

            if (producto.getStock() < item.getCantidadDeseada()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            DetallesPedido detalle = new DetallesPedido();
            detalle.setPedido(nuevoPedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidadDeseada());
            detalle.setPrecioUnitario(producto.getPrecio());

            BigDecimal subTotal = producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidadDeseada()));
            detalle.setSubTotal(subTotal);

            detalles.add(detalle);
            totalPedido = totalPedido.add(subTotal);

            producto.setStock(producto.getStock() - item.getCantidadDeseada());
        }

        nuevoPedido.setDetallesPedido(detalles);
        nuevoPedido.setTotal(totalPedido);

        return pedidoRepository.save(nuevoPedido);
    }

    @Override
    public Pedido findPedidoById(Integer orderId) {
        return pedidoRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + orderId));
    }

    @Override
    public List<Pedido> findPedidoHistoryByUserId(Integer userId) {
        return pedidoRepository.findByUsuarioIdUser(userId);
    }
}