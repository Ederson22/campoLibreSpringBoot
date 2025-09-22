// Archivo: src/main/java/com/proyecto/campoLibre/service/TiendaService.java

package com.proyecto.campoLibre.service;

import com.proyecto.campoLibre.dto.TiendaDto;
import com.proyecto.campoLibre.entity.Tienda;
import jakarta.validation.Valid;

import java.util.Optional;

public interface TiendaService {
    Tienda guardar(TiendaDto tiendaDto, Long idUsuario);
    Optional<Tienda> findById(Long id);
    Optional<Tienda> findByUsuario(Long idUsuario);
    void eliminar(Long id);

    Optional<Tienda> findByDuenoIdUsuario(Long idUsuario);

    void actualizar(@Valid TiendaDto tiendaDto, Long idUsuario);

    Object obtenerTodasLasTiendas();
}