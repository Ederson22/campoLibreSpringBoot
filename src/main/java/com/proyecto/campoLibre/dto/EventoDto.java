package com.proyecto.campoLibre.dto;

import com.proyecto.campoLibre.entity.TipoEvento;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class EventoDto {
    private Long idEvento;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private LocalDate fechaEvento;  // ✅ antes era Date
    private LocalTime horaEvento;   // ✅ antes era String
    private TipoEvento tipoEvento;
}
