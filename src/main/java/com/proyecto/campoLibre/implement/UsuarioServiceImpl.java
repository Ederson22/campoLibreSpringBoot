package com.proyecto.campoLibre.implement;

import com.proyecto.campoLibre.dto.UsuarioRegistroDto;
import com.proyecto.campoLibre.entity.Usuario;
import com.proyecto.campoLibre.repository.UsuarioRepository;
import com.proyecto.campoLibre.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public Usuario guardar(UsuarioRegistroDto registroDto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(registroDto.getNombre());
        usuario.setEmail(registroDto.getEmail());
        usuario.setContrasena(passwordEncoder.encode(registroDto.getContrasena()));
        usuario.setTelefono(registroDto.getTelefono());
        usuario.setTipoDocumento(registroDto.getTipoDocumento());
        usuario.setDocumento(registroDto.getDocumento());
        usuario.setFecha_registro(new Date());

        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        List<String> roles = usuario.getUsuarioRoles().stream()
                .map(usuarioRol -> "ROLE_" + usuarioRol.getRol().getNombre()) // <-- ¡Añade el prefijo aquí!
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getContrasena(),
                roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );
    }
    //__________________________________________________________________________________
    @Override
    public boolean isEmailAlreadyInUse(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean isDocumentoAlreadyInUse(String documento) {
        return usuarioRepository.findByDocumento(documento).isPresent();
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

}