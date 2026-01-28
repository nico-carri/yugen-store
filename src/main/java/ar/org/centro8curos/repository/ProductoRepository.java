package ar.org.centro8curos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.org.centro8curos.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByCategoriaAndActivoTrue(String categoria);

    List<Producto> findByNombreAndActivoTrue(String nombre);

    List<Producto> findByActivoTrue();

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

}
