package ar.org.centro8curos.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/nosotros")
public class NosotrosController {

    @GetMapping("/historia")
    public String historia() {
        return "nosotros/historia";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "nosotros/contacto";
    }

    @GetMapping("/empleo")
    public String empleo() {
        return "nosotros/empleo";
    }
}
