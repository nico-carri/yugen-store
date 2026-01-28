package ar.org.centro8curos.controller.web;

import java.util.Optional;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ar.org.centro8curos.model.Producto;
import ar.org.centro8curos.service.IProductoService;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/catalogo")
    public String verCatalogo(
            @RequestParam(name = "categoria", required = false) String categoria,
            @RequestParam(name = "search", required = false) String nombre,
            Model modelo) {

        List<Producto> productos;

        if (categoria != null && !categoria.isEmpty()) {
            productos = productoService.findAllByCategoria(categoria);
            modelo.addAttribute("tituloPagina", "Categoría: " + categoria);
        } else if (nombre != null && !nombre.isEmpty()) {
            productos = productoService.findByNombreContainingIgnoreCase(nombre);
            modelo.addAttribute("tituloPagina", "Resultados para: '" + nombre + "'");
        } else {
            productos = productoService.findAll();
            modelo.addAttribute("tituloPagina", "Nuestro Catálogo");
        }

        if (productos.isEmpty()) {
            modelo.addAttribute("mensajeNoResultados", "No se encontraron productos.");
        }

        modelo.addAttribute("productos", productos);
        return "productos/catalogo";
    }

    /**
     * Busca productos por nombre.
     */
    @GetMapping("/buscar-nombre")
    public String buscarPorNombre(@RequestParam("nombre") String nombre, Model modelo) {
        List<Producto> productos = productoService.findByNombreContainingIgnoreCase(nombre);
        modelo.addAttribute("productos", productos);
        modelo.addAttribute("tituloPagina", "Resultados para: " + nombre);
        return "productos/catalogo";
    }

    /**
     * Muestra el formulario de creación.
     * Busca el archivo en templates/productos/crear-producto.html
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model modelo) {
        modelo.addAttribute("producto", new Producto());
        return "productos/crear-producto";
    }

    /**
     * Procesa el formulario y guarda el producto.
     * Recibe el archivo de imagen y lo vincula al objeto.
     */
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute("producto") Producto producto,
            @RequestParam("imgFile") MultipartFile imagen) {

        // Si el usuario subió un archivo físico, priorizamos ese nombre
        if (imagen != null && !imagen.isEmpty()) {
            String nombreImagen = imagen.getOriginalFilename();
            producto.setUrlImg("/img/" + nombreImagen);
            // Nota: Aquí faltaría la lógica de uploadService.save(imagen)
        }

        productoService.save(producto);
        return "redirect:/productos/catalogo";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Integer id, Model modelo) {
        // 1. Recibimos el Optional (Asegúrate de importar java.util.Optional)
        Optional<Producto> optionalProducto = productoService.findById(id);

        // 2. Verificamos si tiene contenido antes de usarlo
        if (optionalProducto.isPresent()) {
            // 3. .get() convierte el Optional<Producto> en un Producto real
            modelo.addAttribute("producto", optionalProducto.get());
            return "productos/crear-producto";
        }

        // Si no existe, volvemos al catálogo
        return "redirect:/productos/catalogo";
    }

    /**
     * Elimina un producto por su ID.
     * Se usa Integer o Long dependiendo de tu entidad Producto.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") Integer id) {
        productoService.deleteById(id); // Hará el borrado lógico
        return "redirect:/productos/catalogo";
    }
}