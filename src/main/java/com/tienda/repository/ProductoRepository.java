package com.tienda.repository;

import com.tienda.domain.Producto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByActivoTrue();

    List<Producto> findByCategoria_IdCategoriaAndActivoTrue(Integer idCategoria);

    // Consulta derivada
    List<Producto> findByPrecioBetweenOrderByPrecioAsc(BigDecimal precioInf, BigDecimal precioSup);

    // Consulta JPQL: utiliza la entidad Producto y sus atributos Java.
    @Query("SELECT p FROM Producto p WHERE p.precio BETWEEN :precioInf AND :precioSup ORDER BY p.precio ASC")
    List<Producto> consultaJPQL(@Param("precioInf") BigDecimal precioInf,
            @Param("precioSup") BigDecimal precioSup);

    // Consulta SQL nativa: utiliza la tabla y las columnas de MySQL.
    @Query(value = "SELECT * FROM producto WHERE precio BETWEEN :precioInf AND :precioSup ORDER BY precio ASC",
            nativeQuery = true)
    List<Producto> consultaSQL(@Param("precioInf") BigDecimal precioInf,
            @Param("precioSup") BigDecimal precioSup);
}
