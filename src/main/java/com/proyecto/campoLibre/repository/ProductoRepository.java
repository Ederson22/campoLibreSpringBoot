// Archivo: src/main/java/com/proyecto/campoLibre/repository/ProductoRepository.java

package com.proyecto.campoLibre.repository;

import com.proyecto.campoLibre.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Método principal para encontrar todos los productos de una tienda.
    // Usamos findByTiendaIdTienda para que Spring sepa cómo navegar a través de la relación.
    List<Producto> findByTiendaIdTienda(Long idTienda);

    // Método para encontrar un producto específico de una tienda
    Optional<Producto> findByIdProductoAndTiendaIdTienda(Long idProducto, Long idTienda);

    @Query("SELECT p FROM Producto p WHERE p.tienda.idTienda = :idTienda " +
            "AND (:nombre IS NULL OR p.nombre LIKE %:nombre%) " +
            "AND (:categoria IS NULL OR p.categoria = :categoria) " +
            "AND (:stock IS NULL OR p.stock <= :stock)")
    List<Producto> buscarProductosMulticriterio(
            @Param("idTienda") Long idTienda,
            @Param("nombre") String nombre,
            @Param("categoria") String categoria,
            @Param("stock") Integer stock
    );
}