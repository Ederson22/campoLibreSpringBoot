package com.proyecto.campoLibre.repository;

import com.proyecto.campoLibre.entity.PQRS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PqrsRepository extends JpaRepository<PQRS, Long> {
    // Busca todas las PQRS donde el id del emisor (usuario) coincida
    List<PQRS> findByEmisorIdUsuario(Long emisorId);

}