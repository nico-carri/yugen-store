package ar.org.centro8curos.controller.web;

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
        modelo.addAttribute("total", carritoService.getCarritoItems().stream()
                .mapToDouble(ItemCarritoDTO::getSubTotal).sum());
        return "carrito/ver-carrito";
    }

    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam("id") Integer id,
            @RequestParam("cantidad") Integer cantidad) {
        Optional<Producto> pOptional = productoService.findById(id);
        if (pOptional.isPresent()) {
            Producto p = pOptional.get();
            ItemCarritoDTO item = new ItemCarritoDTO(
                    p.getIdProducto(),
                    p.getNombre(),
                    p.getPrecio().doubleValue(),
                    p.getUrlImg(),
                    cantidad);
            carritoService.addItem(item);
        }
        return "redirect:/productos/catalogo";
    }

    // --- MÉTODOS PARA SUMAR Y RESTAR ---

    @GetMapping("/sumar/{id}")
    public String sumar(@PathVariable Integer id, HttpServletRequest request) {
        carritoService.getCarritoItems().stream()
                .filter(i -> i.getIdProducto().equals(id))
                .findFirst()
                .ifPresent(i -> i.setCantidadDeseada(i.getCantidadDeseada() + 1));

        // Esto te devuelve a la página exacta donde hiciste click
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/home");
    }

    @GetMapping("/restar/{id}")
    public String restar(@PathVariable Integer id, HttpServletRequest request) {
        carritoService.getCarritoItems().stream()
                .filter(i -> i.getIdProducto().equals(id))
                .findFirst()
                .ifPresent(i -> {
                    if (i.getCantidadDeseada() > 1) {
                        i.setCantidadDeseada(i.getCantidadDeseada() - 1);
                    } else {
                        carritoService.removeItem(id);
                    }
                });
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/home");
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Integer id, HttpServletRequest request) {
        carritoService.removeItem(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/home");
    }
}