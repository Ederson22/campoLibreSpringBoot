// Archivo: src/main/java/com/proyecto/campoLibre/service/ProductoService.java

package com.proyecto.campoLibre.service;

import com.proyecto.campoLibre.dto.ProductoDto;
import com.proyecto.campoLibre.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {

    void guardar(ProductoDto productoDto, Long idUsuario);

    void actualizar(Long idProducto, ProductoDto productoDto);

    // Método para buscar todos los productos de una tienda
    List<Producto> findByTiendaId(Long idTienda);

    Optional<Producto> findById(Long idProducto);

    void eliminar(Long idProducto, Long idUsuario);

    // Firma del método para la búsqueda multicriterio
    List<Producto> buscarProductos(Long idTienda, String nombre, String categoria, Integer stock);
}