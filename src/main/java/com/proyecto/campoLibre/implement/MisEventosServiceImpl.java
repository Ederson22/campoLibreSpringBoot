package com.proyecto.campoLibre.implement;

import com.proyecto.campoLibre.entity.MisEventos;
import com.proyecto.campoLibre.entity.Evento;
import com.proyecto.campoLibre.entity.Usuario;
import com.proyecto.campoLibre.entity.EstadoEvento;
import com.proyecto.campoLibre.repository.MisEventosRepository;
import com.proyecto.campoLibre.repository.EventoRepository;
import com.proyecto.campoLibre.repository.UsuarioRepository;
import com.proyecto.campoLibre.service.MisEventosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MisEventosServiceImpl implements MisEventosService {

    private final MisEventosRepository misEventosRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public MisEventosServiceImpl(MisEventosRepository misEventosRepository,
                                 EventoRepository eventoRepository,
                                 UsuarioRepository usuarioRepository) {
        this.misEventosRepository = misEventosRepository;
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public MisEventos suscribirseAEvento(Long idUsuario, Long idEvento) {
        // Verificar que el usuario puede suscribirse
        if (!puedeUsuarioSuscribirse(idUsuario, idEvento)) {
            throw new RuntimeException("No se puede suscribir al evento");
        }

        // Verificar que no esté ya suscrito
        if (estaUsuarioSuscrito(idUsuario, idEvento)) {
            throw new RuntimeException("El usuario ya está suscrito a este evento");
        }

        // Obtener entidades
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        // Crear la suscripción
        MisEventos misEventos = new MisEventos();
        misEventos.setUsuario(usuario);
        misEventos.setEvento(evento);
        misEventos.setFechaGuardado(new Date());

        return misEventosRepository.save(misEventos);
    }

    @Override
    public void desuscribirseDeEvento(Long idUsuario, Long idEvento) {
        if (estaUsuarioSuscrito(idUsuario, idEvento)) {
            misEventosRepository.deleteByUsuarioIdUsuarioAndEventoIdEvento(idUsuario, idEvento);
        } else {
            throw new RuntimeException("El usuario no está suscrito a este evento");
        }
    }

    @Override
    public boolean estaUsuarioSuscrito(Long idUsuario, Long idEvento) {
        return misEventosRepository.existsByUsuarioIdUsuarioAndEventoIdEvento(idUsuario, idEvento);
    }

    @Override
    public List<MisEventos> obtenerEventosDelUsuario(Long idUsuario) {
        return misEventosRepository.findByUsuarioIdUsuario(idUsuario);
    }

    @Override
    public List<MisEventos> obtenerEventosFuturosDelUsuario(Long idUsuario) {
        return misEventosRepository.findEventosFuturosPorUsuario(idUsuario, new Date());
    }

    @Override
    public List<MisEventos> obtenerEventosPasadosDelUsuario(Long idUsuario) {
        return misEventosRepository.findEventosPasadosPorUsuario(idUsuario, new Date());
    }

    @Override
    public Long contarSuscriptoresDeEvento(Long idEvento) {
        return misEventosRepository.contarSuscriptoresPorEvento(idEvento);
    }

    @Override
    public List<Evento> obtenerEventosMasPopulares() {
        List<Object[]> resultados = misEventosRepository.findEventosMasPopulares();
        return resultados.stream()
                .map(result -> (Evento) result[0])
                .limit(10) // Top 10
                .collect(Collectors.toList());
    }

    @Override
    public boolean puedeUsuarioSuscribirse(Long idUsuario, Long idEvento) {
        // Verificar que el usuario existe
        if (!usuarioRepository.existsById(idUsuario)) {
            return false;
        }

        // Verificar que el evento existe y está aprobado
        Optional<Evento> eventoOptional = eventoRepository.findById(idEvento);
        if (eventoOptional.isEmpty()) {
            return false;
        }

        Evento evento = eventoOptional.get();

        // Solo se puede suscribir a eventos aprobados y futuros
        return evento.getEstado() == EstadoEvento.APROBADO &&
                evento.getFechaEvento().after(new Date());
    }
}