package com.proyecto.campoLibre.implement;

import com.proyecto.campoLibre.dto.PqrsDto;
import com.proyecto.campoLibre.entity.EstadoPQRS;
import com.proyecto.campoLibre.entity.PQRS;
import com.proyecto.campoLibre.entity.Usuario;
import com.proyecto.campoLibre.entity.Tienda;
import com.proyecto.campoLibre.entity.Evento;
import com.proyecto.campoLibre.entity.PqrsEvento;
import com.proyecto.campoLibre.entity.PqrsTienda;
import com.proyecto.campoLibre.repository.PqrsRepository;
import com.proyecto.campoLibre.repository.UsuarioRepository;
import com.proyecto.campoLibre.repository.TiendaRepository;
import com.proyecto.campoLibre.repository.EventoRepository;
import com.proyecto.campoLibre.repository.PqrsEventoRepository;
import com.proyecto.campoLibre.repository.PqrsTiendaRepository;
import com.proyecto.campoLibre.service.PqrsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
public class PqrsServiceImpl implements PqrsService {

    private final PqrsRepository pqrsRepository;
    private final UsuarioRepository usuarioRepository;
    private final TiendaRepository tiendaRepository;
    private final EventoRepository eventoRepository;
    private final PqrsTiendaRepository pqrsTiendaRepository;
    private final PqrsEventoRepository pqrsEventoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PqrsServiceImpl(PqrsRepository pqrsRepository, UsuarioRepository usuarioRepository,
                           TiendaRepository tiendaRepository, EventoRepository eventoRepository,
                           PqrsTiendaRepository pqrsTiendaRepository, PqrsEventoRepository pqrsEventoRepository,
                           ModelMapper modelMapper) {
        this.pqrsRepository = pqrsRepository;
        this.usuarioRepository = usuarioRepository;
        this.tiendaRepository = tiendaRepository;
        this.eventoRepository = eventoRepository;
        this.pqrsTiendaRepository = pqrsTiendaRepository;
        this.pqrsEventoRepository = pqrsEventoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void guardarPqrs(PqrsDto pqrsDto, Long idUsuarioEmisor) {

        // Mapear manualmente el DTO a la entidad PQRS para evitar el conflicto de ModelMapper
        PQRS pqrs = new PQRS();
        pqrs.setTipo(pqrsDto.getTipo());
        pqrs.setDescripcion(pqrsDto.getDescripcion());

        // 1. Asignar el usuario emisor (proveedor)
        Optional<Usuario> emisorOptional = usuarioRepository.findById(idUsuarioEmisor);
        if (emisorOptional.isPresent()) {
            pqrs.setEmisor(emisorOptional.get());
        } else {
            throw new RuntimeException("Usuario emisor no encontrado.");
        }

        // 2. Asignar el usuario receptor (administrador)
        Optional<Usuario> receptorOptional = usuarioRepository.findById(1L); // Asume que el ID del administrador es 1
        if (receptorOptional.isPresent()) {
            pqrs.setReceptor(receptorOptional.get());
        } else {
            throw new RuntimeException("Usuario administrador no encontrado.");
        }

        // 3. Asignar el estado inicial y la fecha de envío
        pqrs.setEstado(EstadoPQRS.PENDIENTE);
        pqrs.setFecha_envio(new Date());

        // 4. Guardar la entidad PQRS para obtener su ID
        PQRS pqrsGuardado = pqrsRepository.save(pqrs);

        // 5. Asignar la tienda o el evento si se proporcionó un ID
        if (pqrsDto.getIdTienda() != null) {
            Optional<Tienda> tiendaOptional = tiendaRepository.findById(pqrsDto.getIdTienda());
            tiendaOptional.ifPresent(tienda -> {
                PqrsTienda pqrsTienda = new PqrsTienda();
                pqrsTienda.setPqrs(pqrsGuardado);
                pqrsTienda.setTienda(tienda);
                pqrsTiendaRepository.save(pqrsTienda);
            });
        }

        if (pqrsDto.getIdEvento() != null) {
            Optional<Evento> eventoOptional = eventoRepository.findById(pqrsDto.getIdEvento());
            eventoOptional.ifPresent(evento -> {
                PqrsEvento pqrsEvento = new PqrsEvento();
                pqrsEvento.setPqrs(pqrsGuardado);
                pqrsEvento.setEvento(evento);
                pqrsEventoRepository.save(pqrsEvento);
            });
        }
    }

    @Override
    public List<PQRS> obtenerPqrsPorEmisor(Long idUsuario) {
        // La consulta correcta es a través del ID del usuario emisor
        return pqrsRepository.findByEmisorIdUsuario(idUsuario);
    }

    @Override
    public Optional<PQRS> findById(Long id) {
        return pqrsRepository.findById(id);
    }

    @Override
    public void actualizar(Long id, PqrsDto pqrsDto) {
        Optional<PQRS> pqrsOptional = pqrsRepository.findById(id);

        if (pqrsOptional.isPresent()) {
            PQRS pqrsExistente = pqrsOptional.get();
            pqrsExistente.setDescripcion(pqrsDto.getDescripcion());
            pqrsExistente.setTipo(pqrsDto.getTipo());

            // Si la PQRS tiene una tienda o un evento asociado, probablemente no quieras cambiarlo.
            // Si lo haces, la lógica aquí debe ser más compleja. Por ahora, solo actualizamos
            // el tipo y la descripción.

            pqrsRepository.save(pqrsExistente);
        } else {
            throw new RuntimeException("PQRS no encontrada con ID: " + id);
        }
    }

    @Override
    public void cancelar(Long id) {
        Optional<PQRS> pqrsOptional = pqrsRepository.findById(id);
        if (pqrsOptional.isPresent()) {
            PQRS pqrs = pqrsOptional.get();
            // Solo permitir cancelar si el estado es PENDIENTE
            if (pqrs.getEstado() == EstadoPQRS.PENDIENTE) {
                pqrs.setEstado(EstadoPQRS.CANCELADA);
                pqrsRepository.save(pqrs);
            }
        }
    }

}