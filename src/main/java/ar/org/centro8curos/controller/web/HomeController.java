package ar.org.centro8curos.controller.web;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ar.org.centro8curos.service.IProductoService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private IProductoService productoService;

    @GetMapping({ "/", "/index" })
    public String index(@RequestParam(name = "loginSuccess", required = false) String loginSuccess, Model model) {
        model.addAttribute("productos", productoService.findAll());

        if (loginSuccess != null) {
            model.addAttribute("mensaje", "¡Bienvenidos a Yugen Store!");
        }

        Map<String, String> categorias = new LinkedHashMap<>();
        String baseUrl = "https://axmjtpthgvgy.objectstorage.sa-vinhedo-1.oci.customer-oci.com/n/axmjtpthgvgy/b/yugen-imagenes/o/assets/";

        categorias.put("Manga", baseUrl + "cat-manga.jpg");
        categorias.put("Figuras", baseUrl + "cat-figuras.jpg");
        categorias.put("Accesorios", baseUrl + "cat-accesorios.jpg");
        categorias.put("Peluches", baseUrl + "cat-peluches.jpg");
        categorias.put("Indumentaria", baseUrl + "cat-indumentaria.jpg");
        categorias.put("Escolar", baseUrl + "cat-escolar.jpg");
        categorias.put("Multimedia", baseUrl + "cat-multimedia.jpg");
        categorias.put("Otros", baseUrl + "cat-otros.jpg");
        model.addAttribute("mapaCategorias", categorias);

        return "index";
    }
}