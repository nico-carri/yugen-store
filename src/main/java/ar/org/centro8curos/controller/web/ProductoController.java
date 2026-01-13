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

    /**
     * Muestra la lista completa de productos.
     * Busca el archivo en templates/productos/catalogo.html
     */
    @GetMapping("/catalogo")
    public String verCatalogo(Model modelo) {
        List<Producto> productos = productoService.findAll();
        modelo.addAttribute("productos", productos);
        return "productos/catalogo";
    }

    /**
     * Busca productos por categoría.
     * Reutiliza la vista templates/productos/catalogo.html
     */
    @GetMapping("/buscar")
    public String buscarPorCategoria(@RequestParam("categoria") String categoria, Model modelo) {
        List<Producto> productos = productoService.findAllByCategory(categoria);
        modelo.addAttribute("categoriaBuscada", categoria);
        if (productos.isEmpty()) {
            String mensaje = "No se encontraron productos en la categoría: " + categoria;
            modelo.addAttribute("mensajeNoResultados", mensaje);
        }
        modelo.addAttribute("productos", productos);
        return "productos/catalogo";
    }

    /**
     * Busca productos por nombre.
     */
    @GetMapping("/buscar-nombre")
    public String buscarPorNombre(@RequestParam("nombre") String nombre, Model modelo) {
        List<Producto> productos = productoService.buscarPorNombre(nombre);
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