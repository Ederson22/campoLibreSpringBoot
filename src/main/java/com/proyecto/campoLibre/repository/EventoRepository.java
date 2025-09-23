package com.proyecto.campoLibre.repository;

import com.proyecto.campoLibre.entity.Evento;
import com.proyecto.campoLibre.entity.TipoEvento;
import com.proyecto.campoLibre.entity.EstadoEvento;
import com.proyecto.campoLibre.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByCreadoPor(Usuario creador);
    List<Evento> findByCreadoPorIdUsuario(Long idUsuario);
    List<Evento> findByTipoEvento(TipoEvento tipoEvento);
    List<Evento> findByEstado(EstadoEvento estado);
    List<Evento> findByCreadoPorIdUsuarioAndEstado(Long idUsuario, EstadoEvento estado);

    @Query("SELECT e FROM Evento e WHERE e.fechaEvento >= :fechaActual ORDER BY e.fechaEvento ASC")
    List<Evento> findEventosFuturos(@Param("fechaActual") Date fechaActual);

    @Query("SELECT e FROM Evento e WHERE e.fechaEvento >= :fechaActual AND e.estado = :estado ORDER BY e.fechaEvento ASC")
    List<Evento> findEventosFuturosAprobados(@Param("fechaActual") Date fechaActual, @Param("estado") EstadoEvento estado);

    @Query("SELECT e FROM Evento e WHERE e.ubicacion LIKE %:ubicacion% ORDER BY e.fechaEvento ASC")
    List<Evento> findByUbicacionContaining(@Param("ubicacion") String ubicacion);

    @Query("SELECT e FROM Evento e WHERE " +
            "(:nombre IS NULL OR e.nombre LIKE %:nombre%) AND " +
            "(:tipoEvento IS NULL OR e.tipoEvento = :tipoEvento) AND " +
            "(:ubicacion IS NULL OR e.ubicacion LIKE %:ubicacion%) AND " +
            "(:fechaInicio IS NULL OR e.fechaEvento >= :fechaInicio) AND " +
            "(:fechaFin IS NULL OR e.fechaEvento <= :fechaFin) AND " +
            "(:estado IS NULL OR e.estado = :estado) " +
            "ORDER BY e.fechaEvento ASC")
    List<Evento> buscarEventosConFiltros(
            @Param("nombre") String nombre,
            @Param("tipoEvento") TipoEvento tipoEvento,
            @Param("ubicacion") String ubicacion,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin,
            @Param("estado") EstadoEvento estado
    );

    @Query("SELECT e FROM Evento e WHERE e.estado = 'PENDIENTE' ORDER BY e.fechaEvento ASC")
    List<Evento> findEventosPendientes();
}