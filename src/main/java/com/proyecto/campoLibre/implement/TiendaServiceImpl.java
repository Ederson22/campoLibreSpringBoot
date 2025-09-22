// Archivo: src/main/java/com/proyecto/campoLibre/implement/TiendaServiceImpl.java

package com.proyecto.campoLibre.implement;

import com.proyecto.campoLibre.dto.TiendaDto;
import com.proyecto.campoLibre.entity.EstadoTienda;
import com.proyecto.campoLibre.entity.Tienda;
import com.proyecto.campoLibre.entity.Usuario;
import com.proyecto.campoLibre.repository.TiendaRepository;
import com.proyecto.campoLibre.repository.UsuarioRepository;
import com.proyecto.campoLibre.service.TiendaService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TiendaServiceImpl implements TiendaService {

    private final TiendaRepository tiendaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    public TiendaServiceImpl(TiendaRepository tiendaRepository, UsuarioRepository usuarioRepository, ModelMapper modelMapper) {
        this.tiendaRepository = tiendaRepository;
        this.usuarioRepository = usuarioRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Tienda guardar(TiendaDto tiendaDto, Long idUsuario) {
        // 1. Encuentra el usuario para verificar que no tenga una tienda ya creada
        Optional<Tienda> tiendaOptional = tiendaRepository.findByDuenoIdUsuario(idUsuario);

        if (tiendaOptional.isPresent()) {
            throw new RuntimeException("El usuario ya tiene una tienda asociada.");
        }

        // Si no tiene tienda, procede a encontrar el usuario para asignarlo
        Optional<Usuario> duenoOptional = usuarioRepository.findById(idUsuario);

        if (duenoOptional.isPresent()) {
            Tienda tienda = modelMapper.map(tiendaDto, Tienda.class);
            tienda.setDueno(duenoOptional.get());
            tienda.setEstado(EstadoTienda.ACTIVO);

            // **Corrección:** Devuelve el objeto Tienda que el método save() retorna.
            return tiendaRepository.save(tienda);
        } else {
            throw new RuntimeException("Usuario no encontrado.");
        }
    }

    @Override
    public Optional<Tienda> findById(Long id) {
        return tiendaRepository.findById(id);
    }

    @Override
    public Optional<Tienda> findByUsuario(Long idUsuario) {
        // Usa el guion bajo para que coincida con el nombre del método en el repositorio
        return tiendaRepository.findByDuenoIdUsuario(idUsuario);
    }

    @Override
    public void eliminar(Long id) {
        tiendaRepository.deleteById(id);
    }

    @Override
    public Optional<Tienda> findByDuenoIdUsuario(Long idUsuario) {
        return tiendaRepository.findByDuenoIdUsuario(idUsuario);
    }

    @Override
    public void actualizar(TiendaDto tiendaDto, Long idUsuario) {
        Tienda tiendaExistente = tiendaRepository.findByDuenoIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Tienda no encontrada para el usuario"));

        tiendaExistente.setNombre(tiendaDto.getNombre());
        tiendaExistente.setDescripcion(tiendaDto.getDescripcion());
        tiendaExistente.setCorreoTienda(tiendaDto.getCorreoTienda());
        tiendaExistente.setTelefonoTienda(tiendaDto.getTelefonoTienda());
        tiendaExistente.setUbicacion(tiendaDto.getUbicacion());
        tiendaExistente.setImagenTienda(tiendaDto.getImagenTienda());

        tiendaRepository.save(tiendaExistente);
    }

    @Override
    public List<Tienda> obtenerTodasLasTiendas() {
        return tiendaRepository.findAll();
    }
}