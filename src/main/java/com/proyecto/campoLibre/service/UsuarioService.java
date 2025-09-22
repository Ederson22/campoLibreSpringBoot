// Archivo: src/main/java/com/proyecto.campoLibre/service/UsuarioService.java

package com.proyecto.campoLibre.service;

import com.proyecto.campoLibre.dto.UsuarioRegistroDto;
import com.proyecto.campoLibre.entity.Usuario;
import java.util.Optional;

public interface UsuarioService {
    Usuario guardar(UsuarioRegistroDto registroDto);
    Optional<Usuario> findById(Long id);
    boolean isEmailAlreadyInUse(String email);
    boolean isDocumentoAlreadyInUse(String documento);

    Optional<Usuario> findByEmail(String email);
}