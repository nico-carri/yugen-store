package ar.org.centro8curos.service.implementacion;

import java.util.List;
import org.springframework.stereotype.Service;
import ar.org.centro8curos.repository.ProductoRepository;
import ar.org.centro8curos.service.IProductoService;
import java.util.Optional;
import ar.org.centro8curos.model.Producto;

@Service
public class ProductoServiceImpl implements IProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Optional<Producto> findById(Integer id) {
        return productoRepository.findById(id);
    }

    @Override
    public List<Producto> findAllByCategoria(String categoria) {
        return productoRepository.findByCategoriaAndActivoTrue(categoria);
    }

    @Override
    public List<Producto> findAll() {
        return productoRepository.findByActivoTrue();
    }

    @Override
    public Producto save(Producto producto) {

        // TODO: Enviar actualización a Salesforce CRM vía REST API
    // salesforceService.upsertProduct(guardado);

        return productoRepository.save(producto);
    }

    @Override
    public void deleteById(Integer id) {
        Optional<Producto> optional = productoRepository.findById(id);
        if (optional.isPresent()) {
            Producto p = optional.get();
            p.setActivo(false);
            productoRepository.save(p);
        }
    }

    @Override
    public long countProductos() {
        return productoRepository.count();
    }

    @Override
    public List<Producto> findByNombreContainingIgnoreCase(String keyword) {
        System.out.println("Buscando productos con keyword: " + keyword);
        return productoRepository.findByNombreContainingIgnoreCase(keyword);
    }
}