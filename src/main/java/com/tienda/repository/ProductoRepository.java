package com.tienda.repository;

import com.tienda.domain.Producto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository
        extends JpaRepository<Producto, Integer> {

    List<Producto> findByActivoTrue();

    List<Producto> findByCategoria_IdCategoriaAndActivoTrue(
            Integer idCategoria
    );

    // Consulta derivada
    List<Producto> findByPrecioBetweenOrderByPrecioAsc(
            BigDecimal precioInf,
            BigDecimal precioSup
    );

    // Consulta JPQL
    @Query("""
           SELECT p
           FROM Producto p
           WHERE p.precio BETWEEN :precioInf AND :precioSup
           ORDER BY p.precio ASC
           """)
    List<Producto> consultaJPQL(
            @Param("precioInf") BigDecimal precioInf,
            @Param("precioSup") BigDecimal precioSup
    );

    // Consulta SQL nativa
    @Query(
            value = """
                    SELECT *
                    FROM producto
                    WHERE precio BETWEEN :precioInf AND :precioSup
                    ORDER BY precio ASC
                    """,
            nativeQuery = true
    )
    List<Producto> consultaSQL(
            @Param("precioInf") BigDecimal precioInf,
            @Param("precioSup") BigDecimal precioSup
    );

    /*
     * Práctica #2:
     * consulta ampliada por descripción, categoría
     * y rango de precios.
     */
    @Query("""
           SELECT p
           FROM Producto p
           WHERE (
                    :descripcion IS NULL
                    OR LOWER(p.descripcion)
                       LIKE LOWER(CONCAT('%', :descripcion, '%'))
                 )
             AND (
                    :idCategoria IS NULL
                    OR p.categoria.idCategoria = :idCategoria
                 )
             AND (
                    :precioMin IS NULL
                    OR p.precio >= :precioMin
                 )
             AND (
                    :precioMax IS NULL
                    OR p.precio <= :precioMax
                 )
           ORDER BY p.precio ASC
           """)
    List<Producto> consultaAmpliada(
            @Param("descripcion") String descripcion,
            @Param("idCategoria") Integer idCategoria,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax
    );
}