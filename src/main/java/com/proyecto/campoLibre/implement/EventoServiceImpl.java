package com.proyecto.campoLibre.implement;

import com.proyecto.campoLibre.entity.Evento;
import com.proyecto.campoLibre.entity.Usuario;
import com.proyecto.campoLibre.repository.EventoRepository;
import com.proyecto.campoLibre.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;

    @Autowired
    public EventoServiceImpl(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @Override
    public List<Evento> obtenerTodosLosEventos() {
        return eventoRepository.findAll();
    }

    @Override
    public Evento obtenerEventoPorId(Long id) {
        Optional<Evento> evento = eventoRepository.findById(id);
        return evento.orElse(null);
    }

    @Override
    public Evento guardarEvento(Evento evento, Usuario creador) {
        evento.setCreado_por(creador); // Asigna el usuario creador antes de guardar
        return eventoRepository.save(evento);
    }

    @Override
    public void eliminarEvento(Long id) {
        eventoRepository.deleteById(id);
    }
}