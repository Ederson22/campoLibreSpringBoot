package com.proyecto.campoLibre.dto;

import com.proyecto.campoLibre.entity.EstadoEvento;
import com.proyecto.campoLibre.entity.TipoEvento;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class EventoDto {

    @NotBlank(message = "El nombre del evento es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    private String descripcion;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(min = 5, max = 200, message = "La ubicación debe tener entre 5 y 200 caracteres")
    private String ubicacion;

    @NotNull(message = "La fecha del evento es obligatoria")
    @Future(message = "La fecha del evento debe ser futura")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaEvento;

    @NotBlank(message = "La hora del evento es obligatoria")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Formato de hora inválido (HH:mm)")
    private String horaEvento;

    @NotNull(message = "El tipo de evento es obligatorio")
    private TipoEvento tipoEvento;

    private EstadoEvento estado;
}