package ar.org.centro8curos.controller.rest;

import ar.org.centro8curos.dto.ItemCarritoDTO;
import ar.org.centro8curos.model.Pedido;
import ar.org.centro8curos.service.IPedidoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para manejar la creación de Pedidos y el historial.
 * Llama a la lógica transaccional del servicio.
 */
@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoRestController {

    private final IPedidoService pedidoService;

    public PedidoRestController(IPedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    /**
     * POST /api/v1/pedidos?userId=1
     * Recibe una lista de items del carrito (JSON) y crea un nuevo pedido,
     * validando stock y realizando la transacción.
     */
    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestParam Integer userId,
            @RequestBody List<ItemCarritoDTO> itemsCarrito) {
        try {
            Pedido nuevoPedido = pedidoService.crearPedido(userId, itemsCarrito);
            // Retorna el pedido creado con estado HTTP 201 (Created)
            return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Retorna un error 400 (Bad Request) con el mensaje de stock/producto.
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET /api/v1/pedidos/historial/1
     * Obtiene el historial de pedidos de un usuario específico.
     */
    @GetMapping("/historial/{userId}")
    public List<Pedido> obtenerHistorial(@PathVariable Integer userId) {
        return pedidoService.findPedidoHistoryByUserId(userId);
    }

    /**
     * GET /api/v1/pedidos/1
     * Obtiene el detalle de un pedido específico.
     */
   //@GetMapping("/{id}")
//public ResponseEntity<Pedido> obtenerDetallePedido(@PathVariable Integer id) {
    // 1. Convertimos el Integer de la URL a Long para el Service
    // 2. Usamos Optional para manejar de forma limpia si el pedido existe o no
    //return Optional.ofNullable(pedidoService.findPedidoById(id.longValue()))
    //        .map(pedido -> new ResponseEntity<>(pedido, HttpStatus.OK))
    //        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Retorna 404 si no existe
//}
}