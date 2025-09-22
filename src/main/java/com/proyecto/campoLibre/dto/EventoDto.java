package com.proyecto.campoLibre.dto;

import com.proyecto.campoLibre.entity.TipoEvento;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EventoDto {

    private String nombre;
    private String descripcion;
    private String ubicacion;
    private Date fecha_evento;
    private String hora_evento;
    private TipoEvento tipo_evento;
}