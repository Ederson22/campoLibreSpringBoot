package com.proyecto.campoLibre.repository;

import com.proyecto.campoLibre.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByCreadorIdUsuario(Long idUsuario); // <-- ¡Cambia el nombre aquí!
}