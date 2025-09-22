package com.proyecto.campoLibre.service;

import com.proyecto.campoLibre.entity.Evento;
import com.proyecto.campoLibre.entity.Usuario;
import com.proyecto.campoLibre.entity.TipoEvento;

import java.util.List;

public interface EventoService {
    List<Evento> obtenerTodosLosEventos();
    Evento obtenerEventoPorId(Long id);
    Evento guardarEvento(Evento evento, Usuario creador);
    void eliminarEvento(Long id);
}