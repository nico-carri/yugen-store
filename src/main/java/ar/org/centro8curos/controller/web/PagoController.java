package ar.org.centro8curos.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ar.org.centro8curos.model.Pago;
import ar.org.centro8curos.service.IPagoService;

@Controller
@RequestMapping("/pagos")
public class PagoController {

    private final IPagoService pagoService;

    public PagoController(IPagoService pagoService) {
        this.pagoService = pagoService;
    }

    /**
     * POST /pagos/confirmacion
     * Simula la recepción del objeto Pago finalizado (ej. webhook de pasarela de
     * pago).
     * 
     * @param pago               El objeto Pago que contiene el ID del Pedido y el
     *                           estado final.
     * @param redirectAttributes Para enviar mensajes de éxito/error después de la
     *                           redirección.
     * @return Redirección a la vista de detalle del pedido actualizado.
     */
    @PostMapping("/confirmacion")
    public String confirmarPago(@ModelAttribute Pago pago, RedirectAttributes redirectAttributes) {

        try {
            Pago pagoFinalizado = pagoService.registrarPago(pago);
            redirectAttributes.addFlashAttribute("exito",
                    "Pago ID " + pagoFinalizado.getIdPago() + " procesado con estado: "
                            + pagoFinalizado.getEstadoPago());
            return "redirect:/pedidos/detalle/" + pagoFinalizado.getPedido().getIdPedido();

        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute("error", "Error en la confirmación del pago: " + e.getMessage());
            return "redirect:/productos/catalogo";
        }
    }
}