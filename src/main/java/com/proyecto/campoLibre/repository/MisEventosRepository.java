package com.proyecto.campoLibre.repository;

import com.proyecto.campoLibre.entity.MisEventos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MisEventosRepository extends JpaRepository<MisEventos, Long> {
}