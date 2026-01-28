package ar.org.centro8curos.controller.web;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import ar.org.centro8curos.dto.UsuarioRegistroDTO;
import ar.org.centro8curos.model.Pedido;
import ar.org.centro8curos.model.Usuario;
import ar.org.centro8curos.model.enums.Role;
import ar.org.centro8curos.service.IPedidoService;
import ar.org.centro8curos.service.IUsuarioService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final IUsuarioService usuarioService;
    private final IPedidoService pedidoService;

    public UsuarioController(IUsuarioService usuarioService, IPedidoService pedidoService) {
        this.usuarioService = usuarioService;
        this.pedidoService = pedidoService;
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new UsuarioRegistroDTO());
        return "usuario/registro";
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin() {
        return "usuario/login";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("usuario") UsuarioRegistroDTO registroDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "usuario/registro";
        }
        try {
            if (!registroDTO.getPassword().equals(registroDTO.getConfirmarPassword())) {
                redirectAttributes.addFlashAttribute("error", "¡Las contraseñas no coinciden!");
                return "redirect:/usuario/registro";
            }
            usuarioService.save(registroDTO);
            redirectAttributes.addFlashAttribute("mensaje", "¡Registro exitoso!");
            return "redirect:/usuario/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuario/registro";
        }
    }

@GetMapping("/perfil")
    public String mostrarPerfil(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioService.findByEmail(email);

        if (usuario == null) {
            return "redirect:/usuario/login";
        }

        if (usuario.getRole() == Role.ADMIN) {
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("usuario", usuario);
        List<Pedido> pedidos = pedidoService.findPedidoHistoryByUserId(usuario.getIdUser());
        model.addAttribute("pedidos", pedidos);
        
        return "usuario/perfil"; 
    }

    @GetMapping("/home")
    public String redireccionInicial() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ADMIN"));

        if (isAdmin) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/";
    }

}

