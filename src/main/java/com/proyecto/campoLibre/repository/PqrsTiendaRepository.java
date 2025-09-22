package com.proyecto.campoLibre.repository;

import com.proyecto.campoLibre.entity.PqrsTienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PqrsTiendaRepository extends JpaRepository<PqrsTienda, Long> {
}