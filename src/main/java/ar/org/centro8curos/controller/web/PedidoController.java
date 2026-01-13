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

    /**
     * Proceso de finalización de compra desde el Modal.
     */
    @GetMapping("/finalizar")
    public String finalizarPedidoProceso(Principal principal, RedirectAttributes redirectAttributes) {
        // 1. Validar Sesión
        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para finalizar la compra.");
            return "redirect:/usuario/login";
        }

        try {
            // 2. Obtener Usuario
            String email = principal.getName();
            Usuario usuario = usuarioService.findByEmail(email);

            if (usuario == null) {
                throw new RuntimeException("Usuario no encontrado.");
            }

            // 3. Validar Carrito
            List<ItemCarritoDTO> items = carritoService.getCarritoItems();
            if (items == null || items.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El carrito está vacío.");
                return "redirect:/home"; // O a la url del catálogo
            }

            // 4. Crear Pedido en DB
            Pedido nuevoPedido = pedidoService.crearPedido(usuario.getIdUser(), items);

            // 5. Limpiar Carrito de la sesión
            carritoService.clearCarrito();

            redirectAttributes.addFlashAttribute("mensaje", "¡Compra realizada con éxito!");

            // Redirige al detalle del pedido recién creado
            return "redirect:/pedidos/detalle/" + nuevoPedido.getIdPedido();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error: " + e.getMessage());
            return "redirect:/home";
        }
    }

    /**
     * Ver el detalle de un pedido específico (Ticket).
     */
    @GetMapping("/detalle/{idPedido}")
    public String verDetallePedido(@PathVariable Integer idPedido, Model model, RedirectAttributes redirectAttributes) {
        try {
            Pedido pedido = pedidoService.findPedidoById(idPedido);
            model.addAttribute("pedido", pedido);

            // CAMBIO: Debe decir "pedidos" con S al final
            return "pedidos/detalle_pedido";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al buscar el detalle");
            return "redirect:/productos/catalogo";
        }
    }

    /**
     * Historial de pedidos para el usuario logueado.
     */
    @GetMapping("/historial")
    public String verHistorial(Principal principal, Model model, RedirectAttributes redirectAttributes) {
        if (principal == null)
            return "redirect:/usuario/login";

        try {
            Usuario usuario = usuarioService.findByEmail(principal.getName());
            List<Pedido> pedidos = pedidoService.findPedidoHistoryByUserId(usuario.getIdUser());
            model.addAttribute("pedidos", pedidos);
            return "historial";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar el historial.");
            return "redirect:/home";
        }
    }
}