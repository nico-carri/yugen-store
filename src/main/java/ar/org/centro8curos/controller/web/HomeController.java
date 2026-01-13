package ar.org.centro8curos.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ar.org.centro8curos.service.IProductoService;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private IProductoService productoService;

    @GetMapping("")
    public String home(Model model) {
        // Obtenemos todos los productos de la DB
        model.addAttribute("productos", productoService.findAll());
        return "home"; // Nombre de tu archivo HTML
    }
}