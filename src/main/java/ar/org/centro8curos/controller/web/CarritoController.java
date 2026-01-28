package ar.org.centro8curos.controller.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ar.org.centro8curos.dto.ItemCarritoDTO;
import ar.org.centro8curos.model.Producto;
import ar.org.centro8curos.service.ICarritoService;
import ar.org.centro8curos.service.IProductoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final ICarritoService carritoService;
    private final IProductoService productoService;

    public CarritoController(ICarritoService carritoService, IProductoService productoService) {
        this.carritoService = carritoService;
        this.productoService = productoService;
    }

    @GetMapping
    public String verCarrito(Model modelo) {
        modelo.addAttribute("items", carritoService.getCarritoItems());
        modelo.addAttribute("total", carritoService.getTotalPrecio());
        modelo.addAttribute("totalUnidades", carritoService.getTotalItems());
        return "carrito/ver-carrito";
    }

    @PostMapping("/agregar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> agregarAlCarrito(@RequestParam("id") Integer id,
            @RequestParam("cantidad") Integer cantidad) {

        Map<String, Object> response = new HashMap<>();
        Optional<Producto> pOptional = productoService.findById(id);

        if (pOptional.isPresent()) {
            Producto p = pOptional.get();
            ItemCarritoDTO item = new ItemCarritoDTO(
                    p.getIdProducto(), p.getNombre(),
                    p.getPrecio().doubleValue(), p.getUrlImg(), cantidad);

            carritoService.addItem(item);

            int totalItems = carritoService.getTotalItems();

            response.put("status", "success");
            response.put("message", "¡" + p.getNombre() + " añadido al carrito!");
            response.put("totalCarrito", totalItems);

            return ResponseEntity.ok(response);
        }

        response.put("status", "error");
        response.put("message", "Producto no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Integer id, HttpServletRequest request) {
        carritoService.removeItem(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/home");
    }

    @GetMapping("/sumar/{id}")
    public String sumar(@PathVariable Integer id, HttpServletRequest request) {
        carritoService.incrementarCantidad(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/home");
    }

    @GetMapping("/restar/{id}")
    public String restar(@PathVariable Integer id, HttpServletRequest request) {
        carritoService.decrementarCantidad(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/home");
    }

    @GetMapping("/resumen")
    public String resumenCarrito(Model modelo) {
        modelo.addAttribute("items", carritoService.getCarritoItems());
        modelo.addAttribute("total", carritoService.getTotalPrecio());
        return "carrito/fragmento-carrito :: lista";
    }
}