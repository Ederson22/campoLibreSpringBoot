package com.proyecto.campoLibre.implement;

import com.proyecto.campoLibre.dto.EventoDto;
import com.proyecto.campoLibre.entity.Evento;
import com.proyecto.campoLibre.entity.Usuario;
import com.proyecto.campoLibre.entity.TipoEvento;
import com.proyecto.campoLibre.entity.EstadoEvento;
import com.proyecto.campoLibre.repository.EventoRepository;
import com.proyecto.campoLibre.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public Optional<Evento> obtenerEventoPorId(Long id) {
        return eventoRepository.findById(id);
    }

    @Override
    public Evento guardarEvento(Evento evento, Usuario creador) {
        evento.setCreadoPor(creador);
        // Si no se especifica estado, se asigna PENDIENTE por defecto
        if (evento.getEstado() == null) {
            evento.setEstado(EstadoEvento.PENDIENTE);
        }
        return eventoRepository.save(evento);
    }

    @Override
    public Evento guardarEventoDesdeDto(EventoDto eventoDto, Usuario creador) {
        Evento evento = new Evento();
        evento.setNombre(eventoDto.getNombre());
        evento.setDescripcion(eventoDto.getDescripcion());
        evento.setUbicacion(eventoDto.getUbicacion());
        evento.setFechaEvento(eventoDto.getFechaEvento());
        evento.setHoraEvento(eventoDto.getHoraEvento());
        evento.setTipoEvento(eventoDto.getTipoEvento());
        evento.setCreadoPor(creador);
        evento.setEstado(EstadoEvento.PENDIENTE); // Siempre inicia como PENDIENTE

        return eventoRepository.save(evento);
    }

    @Override
    public Evento actualizarEvento(Long id, EventoDto eventoDto) {
        Optional<Evento> eventoExistente = eventoRepository.findById(id);

        if (eventoExistente.isPresent()) {
            Evento evento = eventoExistente.get();
            evento.setNombre(eventoDto.getNombre());
            evento.setDescripcion(eventoDto.getDescripcion());
            evento.setUbicacion(eventoDto.getUbicacion());
            evento.setFechaEvento(eventoDto.getFechaEvento());
            evento.setHoraEvento(eventoDto.getHoraEvento());
            evento.setTipoEvento(eventoDto.getTipoEvento());
            // El estado no se actualiza desde el DTO normal
            // solo los administradores pueden cambiar estados

            return eventoRepository.save(evento);
        }

        throw new RuntimeException("Evento no encontrado con ID: " + id);
    }

    @Override
    public void eliminarEvento(Long id) {
        if (eventoRepository.existsById(id)) {
            eventoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Evento no encontrado con ID: " + id);
        }
    }

    @Override
    public List<Evento> obtenerEventosPorCreador(Long idUsuario) {
        return eventoRepository.findByCreadoPorIdUsuario(idUsuario);
    }

    @Override
    public List<Evento> obtenerEventosFuturos() {
        return eventoRepository.findEventosFuturos(new Date());
    }

    @Override
    public List<Evento> obtenerEventosFuturosAprobados() {
        return eventoRepository.findEventosFuturosAprobados(new Date(), EstadoEvento.APROBADO);
    }

    // Métodos para manejo de estados (principalmente para administradores)
    @Override
    public List<Evento> obtenerEventosPendientes() {
        return eventoRepository.findEventosPendientes();
    }

    @Override
    public List<Evento> obtenerEventosPorEstado(EstadoEvento estado) {
        return eventoRepository.findByEstado(estado);
    }

    @Override
    public Evento aprobarEvento(Long id) {
        Optional<Evento> eventoOptional = eventoRepository.findById(id);
        if (eventoOptional.isPresent()) {
            Evento evento = eventoOptional.get();
            evento.setEstado(EstadoEvento.APROBADO);
            return eventoRepository.save(evento);
        }
        throw new RuntimeException("Evento no encontrado con ID: " + id);
    }

    @Override
    public Evento rechazarEvento(Long id) {
        Optional<Evento> eventoOptional = eventoRepository.findById(id);
        if (eventoOptional.isPresent()) {
            Evento evento = eventoOptional.get();
            evento.setEstado(EstadoEvento.RECHAZADO);
            return eventoRepository.save(evento);
        }
        throw new RuntimeException("Evento no encontrado con ID: " + id);
    }

    @Override
    public Evento cancelarEvento(Long id) {
        Optional<Evento> eventoOptional = eventoRepository.findById(id);
        if (eventoOptional.isPresent()) {
            Evento evento = eventoOptional.get();
            evento.setEstado(EstadoEvento.CANCELADO);
            return eventoRepository.save(evento);
        }
        throw new RuntimeException("Evento no encontrado con ID: " + id);
    }

    @Override
    public List<Evento> buscarEventosPorTipo(TipoEvento tipoEvento) {
        return eventoRepository.findByTipoEvento(tipoEvento);
    }

    @Override
    public List<Evento> buscarEventosPorUbicacion(String ubicacion) {
        return eventoRepository.findByUbicacionContaining(ubicacion);
    }

    @Override
    public List<Evento> buscarEventosConFiltros(String nombre, TipoEvento tipoEvento,
                                                String ubicacion, Date fechaInicio, Date fechaFin, EstadoEvento estado) {
        return eventoRepository.buscarEventosConFiltros(nombre, tipoEvento, ubicacion, fechaInicio, fechaFin, estado);
    }

    @Override
    public boolean esCreadorDelEvento(Long idEvento, Long idUsuario) {
        Optional<Evento> evento = eventoRepository.findById(idEvento);
        return evento.isPresent() && evento.get().getCreadoPor().getIdUsuario().equals(idUsuario);
    }
}