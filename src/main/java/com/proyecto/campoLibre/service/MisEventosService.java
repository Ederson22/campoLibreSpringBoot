package com.proyecto.campoLibre.service;

import com.proyecto.campoLibre.entity.MisEventos;
import com.proyecto.campoLibre.entity.Evento;
import java.util.List;

public interface MisEventosService {

    // Suscribirse a un evento
    MisEventos suscribirseAEvento(Long idUsuario, Long idEvento);

    // Desuscribirse de un evento
    void desuscribirseDeEvento(Long idUsuario, Long idEvento);

    // Verificar si un usuario está suscrito a un evento
    boolean estaUsuarioSuscrito(Long idUsuario, Long idEvento);

    // Obtener todos los eventos guardados por un usuario
    List<MisEventos> obtenerEventosDelUsuario(Long idUsuario);

    // Obtener eventos futuros del usuario
    List<MisEventos> obtenerEventosFuturosDelUsuario(Long idUsuario);

    // Obtener eventos pasados del usuario
    List<MisEventos> obtenerEventosPasadosDelUsuario(Long idUsuario);

    // Contar suscriptores de un evento
    Long contarSuscriptoresDeEvento(Long idEvento);

    // Obtener eventos más populares
    List<Evento> obtenerEventosMasPopulares();

    // Validar que el evento se puede suscribir (existe y está aprobado)
    boolean puedeUsuarioSuscribirse(Long idUsuario, Long idEvento);
}