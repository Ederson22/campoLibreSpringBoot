// Archivo: src/main/java/com/proyecto/campoLibre/repository/UsuarioRolRepository.java

package com.proyecto.campoLibre.repository;

import com.proyecto.campoLibre.entity.UsuarioRol;
import com.proyecto.campoLibre.entity.UsuarioRolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {
}