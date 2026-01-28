package ar.org.centro8curos.controller.web;

import ar.org.centro8curos.model.Pedido;
import ar.org.centro8curos.model.Producto;
import ar.org.centro8curos.service.IPedidoService;
import ar.org.centro8curos.service.IProductoService;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final IProductoService productoService;
    private final IPedidoService pedidoService;

    public AdminController(IProductoService productoService, IPedidoService pedidoService) {
        this.productoService = productoService;
        this.pedidoService = pedidoService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("cantProductos", productoService.countProductos());
        model.addAttribute("cantPedidos", pedidoService.countPedidos());
        // Pasamos los últimos 5 para la tabla que agregamos
        model.addAttribute("ultimosPedidos", pedidoService.findAll().stream().limit(5).toList());
        return "admin/dashboard";
    }

    @GetMapping("/productos")
        public String gestionarProductos(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<Producto> productos;
        if (keyword != null && !keyword.isEmpty()) {
            productos = productoService.findByNombreContainingIgnoreCase(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            productos = productoService.findAll();
        }
        long sinStock = productos.stream().filter(p -> p.getStock() == 0).count();
        model.addAttribute("productos", productos);
        model.addAttribute("alertaStock", sinStock);

        return "admin/lista-productos";
    }

    @GetMapping("/productos/nuevo")
    public String formularioNuevo(Model model) {
    model.addAttribute("producto", new Producto());
    model.addAttribute("titulo", "Nuevo Producto");
    return "admin/crear-producto";
    }

    @GetMapping("/productos/editar/{id}")
    public String formularioEditar(@PathVariable("id") Integer id, Model model) {
    Producto producto = productoService.findById(id)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    
    model.addAttribute("producto", producto);
    model.addAttribute("titulo", "Editar Producto: " + producto.getNombre());
    return "admin/crear-producto";
}

    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute("producto") Producto producto) {
        // 1. Lógica para guardar en Oracle
        productoService.save(producto);
        
        // 2. Toque Profesional: Sincronización con Salesforce
        // Aquí dispararías tu lógica: salesforceService.upsertProduct(producto);
        
        // 3. LA CLAVE: Redirigir a la lista de productos
        return "redirect:/admin/productos"; 
    }

    @GetMapping("/pedidos/detalle/{id}")
    public String verDetallePedido(@PathVariable("id") Long id, Model model) {
        Pedido pedido = pedidoService.findPedidoById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
        
        model.addAttribute("pedido", pedido);
        model.addAttribute("titulo", "Gestión de Pedido #" + id);
        
        return "admin/detalle-pedido"; 
    }

    @GetMapping("/graficos")
    public String verGraficos(Model model) {
        List<Pedido> pedidos = pedidoService.findAll();

        // Sumar BigDecimal de forma profesional
        BigDecimal totalVentas = pedidos.stream()
                .map(Pedido::getTotal)
                .filter(total -> total != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long completados = pedidos.stream()
                .filter(p -> p.getEstado() != null && "COMPLETADO".equals(p.getEstado().name()))
                .count();

        long pendientes = pedidos.stream()
                .filter(p -> p.getEstado() != null && "PENDIENTE_PAGO".equals(p.getEstado().name()))
                .count();

        model.addAttribute("totalVentas", totalVentas);
        model.addAttribute("completados", completados);
        model.addAttribute("pendientes", pendientes);

        return "admin/graficos";
    }
}