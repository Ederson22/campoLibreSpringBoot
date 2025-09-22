// Archivo: src/main/java/com/proyecto/campoLibre/dto/TiendaDto.java

package com.proyecto.campoLibre.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TiendaDto {

    private Long idTienda; // Corregido de id_tienda

    @NotEmpty(message = "El nombre de la tienda no puede estar vacío")
    private String nombre;

    @NotEmpty(message = "La descripción no puede estar vacía")
    private String descripcion;

    @NotEmpty(message = "El correo de la tienda no puede estar vacío")
    private String correoTienda; // Corregido de correo_tienda

    @NotEmpty(message = "El teléfono no puede estar vacío")
    private String telefonoTienda; // Corregido de telefono_tienda

    private String ubicacion;

    private String imagenTienda; // Corregido de imagen_Tienda
}