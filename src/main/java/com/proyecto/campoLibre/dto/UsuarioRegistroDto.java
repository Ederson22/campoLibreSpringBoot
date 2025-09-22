// Archivo: src/main/java/com/proyecto/campoLibre/dto/UsuarioRegistroDto.java

package com.proyecto.campoLibre.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.proyecto.campoLibre.entity.Rol;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRegistroDto {

    @NotEmpty(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotEmpty(message = "El email no puede estar vacío")
    @Email(message = "Debe ser una dirección de email válida")
    private String email;

    @NotEmpty(message = "La contraseña no puede estar vacía")
    private String contrasena;

    @NotEmpty(message = "El teléfono no puede estar vacío")
    private String telefono;

    @NotEmpty(message = "El tipo de documento no puede estar vacío")
    private String tipoDocumento;

    @NotEmpty(message = "El documento no puede estar vacío")
    private String documento;

    private Long idRol;
}