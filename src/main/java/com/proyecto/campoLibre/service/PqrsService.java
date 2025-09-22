package com.proyecto.campoLibre.service;

import com.proyecto.campoLibre.dto.PqrsDto;
import com.proyecto.campoLibre.entity.PQRS;

import java.util.List;
import java.util.Optional;

public interface PqrsService {

    void guardarPqrs(PqrsDto pqrsDto, Long idUsuario);
    List<PQRS> obtenerPqrsPorEmisor(Long idUsuario);
    Optional<PQRS> findById(Long id); // <-- ¡Nuevo método!
    void actualizar(Long id, PqrsDto pqrsDto); // <-- ¡Nuevo método!
    void cancelar(Long id);

}