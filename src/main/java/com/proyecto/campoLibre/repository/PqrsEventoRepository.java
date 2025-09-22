package com.proyecto.campoLibre.repository;

import com.proyecto.campoLibre.entity.PqrsEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PqrsEventoRepository extends JpaRepository<PqrsEvento, Long> {
}