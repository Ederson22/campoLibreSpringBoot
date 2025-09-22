// Archivo: src/main/java/com/proyecto.campoLibre/controller/RegistroController.java

package com.proyecto.campoLibre.controller;

import com.proyecto.campoLibre.dto.UsuarioRegistroDto;
import com.proyecto.campoLibre.entity.Rol;
import com.proyecto.campoLibre.entity.Usuario;
import com.proyecto.campoLibre.entity.UsuarioRol;
import com.proyecto.campoLibre.repository.RolRepository;
import com.proyecto.campoLibre.repository.UsuarioRepository;
import com.proyecto.campoLibre.repository.UsuarioRolRepository;
import com.proyecto.campoLibre.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/register")
public class RegistroController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    @Autowired
    public RegistroController(UsuarioRepository usuarioRepository, RolRepository rolRepository, UsuarioRolRepository usuarioRolRepository, PasswordEncoder passwordEncoder, UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("usuario", new UsuarioRegistroDto());
        model.addAttribute("roles", rolRepository.findAll());
        return "register/register";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("usuario") UsuarioRegistroDto registroDto) {
        try {
            // ¡Usa el servicio para crear y guardar el usuario!
            // Esto asegura que todos los campos del DTO se copien a la entidad.
            Usuario usuario = usuarioService.guardar(registroDto);

            // Busca el rol por su ID (que viene del formulario)
            Optional<Rol> rolOptional = rolRepository.findById(registroDto.getIdRol());

            if (rolOptional.isPresent()) {
                Rol rol = rolOptional.get();

                // Crea la relación en la tabla de unión
                UsuarioRol usuarioRol = new UsuarioRol();
                usuarioRol.setUsuario(usuario);
                usuarioRol.setRol(rol);
                usuarioRolRepository.save(usuarioRol);
            } else {
                return "redirect:/register?error=role-not-found";
            }

            return "redirect:/login?registered";
        } catch (Exception e) {
            return "redirect:/register?error";
        }
    }

    @ModelAttribute("usuario")
    public UsuarioRegistroDto retornarNuevoUsuarioRegistroDto() {
        return new UsuarioRegistroDto();
    }
}