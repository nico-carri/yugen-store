package ar.org.centro8curos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.org.centro8curos.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // 1. Buscamos por categoría PERO solo si están activos
    List<Producto> findByCategoriaAndActivoTrue(String categoria);

    // 2. Buscamos por nombre PERO solo si están activos
    List<Producto> findByNombreAndActivoTrue(String nombre);

    // 3. Este es el que usaremos para el catálogo general (Suficiente con uno solo)
    List<Producto> findByActivoTrue();

}
