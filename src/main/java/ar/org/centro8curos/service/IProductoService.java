package ar.org.centro8curos.service;

import java.util.List;
import java.util.Optional;

import ar.org.centro8curos.model.Producto;

public interface IProductoService {
    Optional<Producto> findById(Integer id);

    List<Producto> findAllByCategoria(String categoria);


    List<Producto> findAll();

    Producto save(Producto producto);

    void deleteById(Integer id);

    long countProductos();

    List<Producto> findByNombreContainingIgnoreCase(String keyword);

}