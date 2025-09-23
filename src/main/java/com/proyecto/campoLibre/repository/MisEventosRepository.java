package com.proyecto.campoLibre.repository;

import com.proyecto.campoLibre.entity.MisEventos;
import com.proyecto.campoLibre.entity.Usuario;
import com.proyecto.campoLibre.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MisEventosRepository extends JpaRepository<MisEventos, Long> {

    List<MisEventos> findByUsuarioIdUsuario(Long idUsuario);

    Optional<MisEventos> findByUsuarioIdUsuarioAndEventoIdEvento(Long idUsuario, Long idEvento);

    boolean existsByUsuarioIdUsuarioAndEventoIdEvento(Long idUsuario, Long idEvento);

    // Cambiar fecha_evento por fechaEvento
    @Query("SELECT me FROM MisEventos me WHERE me.usuario.idUsuario = :idUsuario " +
            "AND me.evento.fechaEvento >= :fechaActual " +
            "AND me.evento.estado = 'APROBADO' " +
            "ORDER BY me.evento.fechaEvento ASC")
    List<MisEventos> findEventosFuturosPorUsuario(@Param("idUsuario") Long idUsuario, @Param("fechaActual") Date fechaActual);

    // Cambiar fecha_evento por fechaEvento
    @Query("SELECT me FROM MisEventos me WHERE me.usuario.idUsuario = :idUsuario " +
            "AND me.evento.fechaEvento < :fechaActual " +
            "ORDER BY me.evento.fechaEvento DESC")
    List<MisEventos> findEventosPasadosPorUsuario(@Param("idUsuario") Long idUsuario, @Param("fechaActual") Date fechaActual);

    @Query("SELECT COUNT(me) FROM MisEventos me WHERE me.evento.idEvento = :idEvento")
    Long contarSuscriptoresPorEvento(@Param("idEvento") Long idEvento);

    @Query("SELECT me.evento, COUNT(me) as suscriptores FROM MisEventos me " +
            "WHERE me.evento.estado = 'APROBADO' " +
            "GROUP BY me.evento " +
            "ORDER BY suscriptores DESC")
    List<Object[]> findEventosMasPopulares();

    void deleteByUsuarioIdUsuarioAndEventoIdEvento(Long idUsuario, Long idEvento);
}