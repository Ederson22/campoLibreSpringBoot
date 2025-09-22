// Archivo: src/main/java/com/proyecto.campoLibre/repository/UsuarioRepository.java

package com.proyecto.campoLibre.repository;

import com.proyecto.campoLibre.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método personalizado para buscar un usuario por email y cargar sus roles de manera inmediata
    @Query("SELECT u FROM Usuario u JOIN FETCH u.usuarioRoles ur JOIN FETCH ur.rol WHERE u.email = :email")
    Optional<Usuario> findByEmailWithRoles(@Param("email") String email);

    Optional<Usuario> findByEmail(String email); // Mantén este si lo usas en otras partes

    Optional<Object> findByDocumento(String documento);
}