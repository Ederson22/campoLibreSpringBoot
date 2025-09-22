package com.proyecto.campoLibre.repository;

import com.proyecto.campoLibre.entity.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Long> {

    @Query("SELECT t FROM Tienda t WHERE t.dueno.idUsuario = :idUsuario") // Suponiendo que el campo en Usuario se llama idUsuario
    Optional<Tienda> findByDuenoIdUsuario(@Param("idUsuario") Long idUsuario);
}