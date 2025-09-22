// Archivo: src/main/java/com/proyecto/campoLibre/dto/UsuarioDTO.java

package com.proyecto.campoLibre.dto;

import com.proyecto.campoLibre.entity.Rol;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UsuarioDTO {
    private Long id_usuario;
    private String nombre;
    private String email;
    private String telefono;
    private String tipoDocumento;
    private String documento;
    private Date fecha_registro;
    private Rol id_rol;
}