package ar.org.centro8curos.controller.web;

import java.security.Principal;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.org.centro8curos.dto.ItemCarritoDTO;
import ar.org.centro8curos.model.Pedido;
import ar.org.centro8curos.model.Usuario;
import ar.org.centro8curos.service.ICarritoService;
import ar.org.centro8curos.service.IPedidoService;
import ar.org.centro8curos.service.IUsuarioService;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final IPedidoService pedidoService;
    private final IUsuarioService usuarioService;
    private final ICarritoService carritoService;

    public PedidoController(IPedidoService pedidoService,
            IUsuarioService usuarioService,
            ICarritoService carritoService) {
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
        this.carritoService = carritoService;
    }

    @GetMapping("/finalizar")
    public String finalizarPedidoProceso(Principal principal, RedirectAttributes redirectAttributes) {
    // 1. Validar sesión de forma limpia
    if (principal == null) return "redirect:/usuario/login";

    try {
            Usuario usuario = usuarioService.findByEmail(principal.getName());
            List<ItemCarritoDTO> items = carritoService.getCarritoItems();

            if (items.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Carrito vacío");
                return "redirect:/carrito";
            }
            Pedido nuevoPedido = pedidoService.crearPedido(usuario.getIdUser(), items);
            carritoService.clearCarrito();

            return "redirect:/pedidos/detalle/" + nuevoPedido.getIdPedido();

        } catch (Exception e) {
            System.out.println("ERROR EN FINALIZAR: " + e.getMessage()); 
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/detalle/{idPedido}")
    public String verDetallePedido(@PathVariable Integer idPedido, Model model, RedirectAttributes redirectAttributes) {
    try {
            Pedido pedido = pedidoService.findPedidoById(idPedido.longValue())
                    .orElseThrow(() -> new RuntimeException("El pedido no existe"));

            model.addAttribute("pedido", pedido);
            return "pedidos/detalle-pedido"; 

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al buscar el detalle: " + e.getMessage());
            return "redirect:/productos/catalogo";
        }
    }

    @GetMapping("/historial")
    public String verHistorial(Principal principal, Model model, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/usuario/login";
        }

        try {
            Usuario usuario = usuarioService.findByEmail(principal.getName());
            List<Pedido> pedidos = pedidoService.findPedidoHistoryByUserId(usuario.getIdUser());
            model.addAttribute("pedidos", pedidos);
            return "pedidos/historial";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar el historial.");
            return "redirect:/";
        }
    }
}