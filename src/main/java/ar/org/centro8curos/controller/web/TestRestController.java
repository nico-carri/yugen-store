package ar.org.centro8curos.controller.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // ⬅️ CLAVE: Convierte el resultado en JSON/XML
@RequestMapping("/api/v1")
public class TestRestController {

    @GetMapping("/status")
    public String checkStatus() {
        return "{\"status\": \"UP\", \"message\": \"API REST operativa\"}";
    }
}