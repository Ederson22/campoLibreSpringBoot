package com.proyecto.campoLibre.dto;

import com.proyecto.campoLibre.entity.TipoPQRS;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PqrsDto {

    @NotNull(message = "El tipo de PQRS no puede ser nulo")
    private TipoPQRS tipo;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    // Nuevos campos para asociar a una tienda o evento
    private Long idTienda;
    private Long idEvento;
}