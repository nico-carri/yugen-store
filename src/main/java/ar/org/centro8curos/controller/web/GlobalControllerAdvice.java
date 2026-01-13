package ar.org.centro8curos.controller.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import ar.org.centro8curos.dto.ItemCarritoDTO;
import ar.org.centro8curos.service.ICarritoService;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final ICarritoService carritoService;

    public GlobalControllerAdvice(ICarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @ModelAttribute
    public void addCarritoToModel(Model model) {
        model.addAttribute("items", carritoService.getCarritoItems());
        model.addAttribute("total", carritoService.getCarritoItems().stream()
                .mapToDouble(ItemCarritoDTO::getSubTotal).sum());
    }
}