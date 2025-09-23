package com.proyecto.campoLibre.service;

import com.proyecto.campoLibre.dto.EventoDto;
import com.proyecto.campoLibre.entity.Evento;
import com.proyecto.campoLibre.entity.Usuario;
import com.proyecto.campoLibre.entity.TipoEvento;
import com.proyecto.campoLibre.entity.EstadoEvento;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventoService {

    // Métodos básicos CRUD
    List<Evento> obtenerTodosLosEventos();
    Optional<Evento> obtenerEventoPorId(Long id);
    Evento guardarEvento(Evento evento, Usuario creador);
    Evento guardarEventoDesdeDto(EventoDto eventoDto, Usuario creador);
    Evento actualizarEvento(Long id, EventoDto eventoDto);
    void eliminarEvento(Long id);

    // Métodos específicos por usuario
    List<Evento> obtenerEventosPorCreador(Long idUsuario);
    List<Evento> obtenerEventosFuturos();
    List<Evento> obtenerEventosFuturosAprobados(); // Para consumidores

    // Métodos de estado (para administradores)
    List<Evento> obtenerEventosPendientes();
    List<Evento> obtenerEventosPorEstado(EstadoEvento estado);
    Evento aprobarEvento(Long id);
    Evento rechazarEvento(Long id);
    Evento cancelarEvento(Long id);

    // Métodos de búsqueda y filtrado
    List<Evento> buscarEventosPorTipo(TipoEvento tipoEvento);
    List<Evento> buscarEventosPorUbicacion(String ubicacion);
    List<Evento> buscarEventosConFiltros(String nombre, TipoEvento tipoEvento,
                                         String ubicacion, Date fechaInicio, Date fechaFin, EstadoEvento estado);

    // Método para verificar permisos
    boolean esCreadorDelEvento(Long idEvento, Long idUsuario);
}