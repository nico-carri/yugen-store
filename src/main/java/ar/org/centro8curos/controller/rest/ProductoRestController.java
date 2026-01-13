package ar.org.centro8curos.controller.rest;

import ar.org.centro8curos.model.Producto;
import ar.org.centro8curos.service.IProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para manejar operaciones CRUD de Productos (API V1).
 * Ideal para ser consumido por un frontend usando Axios/Fetch.
 */
@RestController
@RequestMapping("/api/v1/productos")
public class ProductoRestController {

    private final IProductoService productoService;

    public ProductoRestController(IProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * GET /api/v1/productos
     * Obtener la lista completa de productos.
     */
    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.findAll();
    }

    /**
     * GET /api/v1/productos/{id}
     * Obtener los detalles de un producto específico.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Producto>> obtenerProducto(@PathVariable Integer id) {
        try {
            // El findById del servicio ya maneja la excepción si no lo encuentra
            Optional<Producto> producto = productoService.findById(id);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            // Devuelve 404 Not Found si el producto no existe
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * POST /api/v1/productos
     * Crear un nuevo producto (Normalmente restringido a Admin).
     */
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        // Asumiendo que IProductoService tiene un método save()
        // producto.setIdProducto(null); // Asegura que se genere un nuevo ID
        Producto nuevoProducto = productoService.save(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    // NOTA: Para implementar PUT y DELETE, asegúrate de que tu IProductoService
    // tenga los métodos correspondientes (update y deleteById).
}