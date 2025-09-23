    package com.proyecto.campoLibre.implement;

    import com.proyecto.campoLibre.dto.EventoDto;
    import com.proyecto.campoLibre.entity.Evento;
    import com.proyecto.campoLibre.entity.Usuario;
    import com.proyecto.campoLibre.entity.EstadoEvento;
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
        public Optional<Evento> obtenerEventoPorId(Long id) {
            return eventoRepository.findById(id);
        }

        // Este método es el que has estado usando en tu controlador.
        // Su firma coincide con la que hemos estado revisando.
        @Override
        public Evento guardarEvento(Evento evento, Usuario creador) {
            evento.setCreador(creador);
            evento.setEstado(EstadoEvento.PENDIENTE); // O el estado inicial que desees
            return eventoRepository.save(evento);
        }

        @Override
        public void eliminarEvento(Long id) {
            eventoRepository.deleteById(id);
        }

        @Override
        public void actualizarEvento(Long id, EventoDto eventoDto) {
            Optional<Evento> eventoExistenteOptional = eventoRepository.findById(id);
            if (eventoExistenteOptional.isPresent()) {
                Evento eventoExistente = eventoExistenteOptional.get();
                eventoExistente.setNombre(eventoDto.getNombre());
                eventoExistente.setDescripcion(eventoDto.getDescripcion());
                eventoExistente.setUbicacion(eventoDto.getUbicacion());
                eventoExistente.setFechaEvento(eventoDto.getFechaEvento());
                eventoExistente.setHoraEvento(eventoDto.getHoraEvento());
                eventoExistente.setTipoEvento(eventoDto.getTipoEvento());
                eventoRepository.save(eventoExistente);
            } else {
                throw new RuntimeException("Evento no encontrado con ID: " + id);
            }
        }

        @Override
        public List<Evento> obtenerEventosPorCreador(Long idUsuarioCreador) {
            return eventoRepository.findByCreadorIdUsuario(idUsuarioCreador);
        }

        @Override
        public void cancelarEvento(Long id) {
            Optional<Evento> eventoOptional = eventoRepository.findById(id);
            if (eventoOptional.isPresent()) {
                Evento evento = eventoOptional.get();
                evento.setEstado(EstadoEvento.CANCELADO);
                eventoRepository.save(evento);
            }
        }
    }