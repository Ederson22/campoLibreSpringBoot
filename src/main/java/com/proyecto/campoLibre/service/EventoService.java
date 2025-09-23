package com.proyecto.campoLibre.service;

import com.proyecto.campoLibre.dto.EventoDto;
import com.proyecto.campoLibre.entity.Evento;
import com.proyecto.campoLibre.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface EventoService {
    // Método para obtener todos los eventos (necesario para las PQRS)
    List<Evento> obtenerTodosLosEventos();

    // Método para obtener un evento por ID
    Optional<Evento> obtenerEventoPorId(Long id);

    // Método para guardar un evento
    Evento guardarEvento(Evento evento, Usuario creador);

    // Método para eliminar un evento
    void eliminarEvento(Long id);

    // Método para actualizar un evento
    void actualizarEvento(Long id, EventoDto eventoDto);

    // Nuevo método para filtrar eventos por el ID del usuario creador
    List<Evento> obtenerEventosPorCreador(Long idUsuarioCreador);

    // Nuevo método para cambiar el estado a 'CANCELADO'
    void cancelarEvento(Long id);
}