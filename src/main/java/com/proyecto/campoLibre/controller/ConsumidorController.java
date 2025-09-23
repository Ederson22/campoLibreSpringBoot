package com.proyecto.campoLibre.controller;

import com.proyecto.campoLibre.entity.Evento;
import com.proyecto.campoLibre.entity.MisEventos;
import com.proyecto.campoLibre.entity.Usuario;
import com.proyecto.campoLibre.entity.TipoEvento;
import com.proyecto.campoLibre.entity.EstadoEvento;
import com.proyecto.campoLibre.service.EventoService;
import com.proyecto.campoLibre.service.MisEventosService;
import com.proyecto.campoLibre.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/consumidor")
public class ConsumidorController {

    private final EventoService eventoService;
    private final MisEventosService misEventosService;
    private final UsuarioService usuarioService;

    @Autowired
    public ConsumidorController(EventoService eventoService,
                                MisEventosService misEventosService,
                                UsuarioService usuarioService) {
        this.eventoService = eventoService;
        this.misEventosService = misEventosService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String consumidorHome(Model model, Authentication authentication) {
        // Obtener eventos más populares para mostrar en home
        List<Evento> eventosPopulares = misEventosService.obtenerEventosMasPopulares();
        List<Evento> eventosFuturos = eventoService.obtenerEventosFuturosAprobados();

        model.addAttribute("eventosPopulares", eventosPopulares.stream().limit(3).collect(Collectors.toList()));
        model.addAttribute("eventosFuturos", eventosFuturos.stream().limit(6).collect(Collectors.toList()));

        // Si el usuario está logueado, verificar sus suscripciones
        if (authentication != null) {
            String email = authentication.getName();
            Usuario usuario = usuarioService.findByEmail(email).orElse(null);
            if (usuario != null) {
                Map<Long, Boolean> suscripciones = eventosFuturos.stream()
                        .collect(Collectors.toMap(
                                Evento::getIdEvento,
                                evento -> misEventosService.estaUsuarioSuscrito(usuario.getIdUsuario(), evento.getIdEvento())
                        ));
                model.addAttribute("suscripciones", suscripciones);
            }
        }

        return "consumidor/consumidorHome";
    }

    //______________________________________EVENTOS DISPONIBLES______________________________________

    @GetMapping("/eventos")
    public String verEventos(Model model, Authentication authentication,
                             @RequestParam(name = "tipo", required = false) TipoEvento tipo,
                             @RequestParam(name = "ubicacion", required = false) String ubicacion,
                             @RequestParam(name = "nombre", required = false) String nombre) {

        // Obtener solo eventos futuros y aprobados
        List<Evento> eventos;

        if (nombre != null || tipo != null || ubicacion != null) {
            // Buscar con filtros, solo eventos aprobados
            eventos = eventoService.buscarEventosConFiltros(nombre, tipo, ubicacion, new Date(), null, EstadoEvento.APROBADO);
        } else {
            // Sin filtros, obtener todos los eventos futuros aprobados
            eventos = eventoService.obtenerEventosFuturosAprobados();
        }

        // Si el usuario está logueado, verificar sus suscripciones
        Map<Long, Boolean> suscripciones = null;
        Map<Long, Long> conteoSuscriptores = null;

        if (authentication != null) {
            String email = authentication.getName();
            Usuario usuario = usuarioService.findByEmail(email).orElse(null);
            if (usuario != null) {
                suscripciones = eventos.stream()
                        .collect(Collectors.toMap(
                                Evento::getIdEvento,
                                evento -> misEventosService.estaUsuarioSuscrito(usuario.getIdUsuario(), evento.getIdEvento())
                        ));
            }
        }

        // Obtener conteo de suscriptores para cada evento
        conteoSuscriptores = eventos.stream()
                .collect(Collectors.toMap(
                        Evento::getIdEvento,
                        evento -> misEventosService.contarSuscriptoresDeEvento(evento.getIdEvento())
                ));

        model.addAttribute("eventos", eventos);
        model.addAttribute("tiposEvento", TipoEvento.values());
        model.addAttribute("suscripciones", suscripciones);
        model.addAttribute("conteoSuscriptores", conteoSuscriptores);

        return "consumidor/eventos";
    }

    @GetMapping("/eventos/detalle/{id}")
    public String detalleEvento(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Evento evento = eventoService.obtenerEventoPorId(id)
                .orElse(null);

        if (evento == null || evento.getEstado() != EstadoEvento.APROBADO) {
            return "redirect:/consumidor/eventos";
        }

        // Verificar si el usuario está suscrito
        boolean usuarioSuscrito = false;
        if (authentication != null) {
            String email = authentication.getName();
            Usuario usuario = usuarioService.findByEmail(email).orElse(null);
            if (usuario != null) {
                usuarioSuscrito = misEventosService.estaUsuarioSuscrito(usuario.getIdUsuario(), id);
            }
        }

        Long totalSuscriptores = misEventosService.contarSuscriptoresDeEvento(id);

        model.addAttribute("evento", evento);
        model.addAttribute("usuarioSuscrito", usuarioSuscrito);
        model.addAttribute("totalSuscriptores", totalSuscriptores);

        return "consumidor/detalle_evento";
    }

    //______________________________________MIS EVENTOS (SUSCRIPCIONES)______________________________________

    @GetMapping("/mis-eventos")
    public String misEventos(Model model, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<MisEventos> eventosFuturos = misEventosService.obtenerEventosFuturosDelUsuario(usuario.getIdUsuario());
        List<MisEventos> eventosPasados = misEventosService.obtenerEventosPasadosDelUsuario(usuario.getIdUsuario());

        model.addAttribute("eventosFuturos", eventosFuturos);
        model.addAttribute("eventosPasados", eventosPasados);

        return "consumidor/mis_eventos";
    }

    @PostMapping("/eventos/suscribirse/{id}")
    public String suscribirseEvento(@PathVariable("id") Long id,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Usuario usuario = usuarioService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            misEventosService.suscribirseAEvento(usuario.getIdUsuario(), id);
            redirectAttributes.addFlashAttribute("mensaje", "Te has suscrito al evento exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/consumidor/eventos";
    }

    @PostMapping("/eventos/desuscribirse/{id}")
    public String desuscribirseEvento(@PathVariable("id") Long id,
                                      Authentication authentication,
                                      RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Usuario usuario = usuarioService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            misEventosService.desuscribirseDeEvento(usuario.getIdUsuario(), id);
            redirectAttributes.addFlashAttribute("mensaje", "Te has desuscrito del evento");
            redirectAttributes.addFlashAttribute("tipoMensaje", "info");

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/consumidor/eventos";
    }

    @PostMapping("/mis-eventos/desuscribirse/{id}")
    public String desuscribirseDesdeListaPersonal(@PathVariable("id") Long id,
                                                  Authentication authentication,
                                                  RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Usuario usuario = usuarioService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            misEventosService.desuscribirseDeEvento(usuario.getIdUsuario(), id);
            redirectAttributes.addFlashAttribute("mensaje", "Te has desuscrito del evento");
            redirectAttributes.addFlashAttribute("tipoMensaje", "info");

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/consumidor/mis-eventos";
    }

    //______________________________________EVENTOS POPULARES______________________________________

    @GetMapping("/eventos/populares")
    public String eventosPopulares(Model model, Authentication authentication) {
        List<Evento> eventosPopulares = misEventosService.obtenerEventosMasPopulares();

        // Si el usuario está logueado, verificar sus suscripciones
        Map<Long, Boolean> suscripciones = null;
        Map<Long, Long> conteoSuscriptores = null;

        if (authentication != null) {
            String email = authentication.getName();
            Usuario usuario = usuarioService.findByEmail(email).orElse(null);
            if (usuario != null) {
                suscripciones = eventosPopulares.stream()
                        .collect(Collectors.toMap(
                                Evento::getIdEvento,
                                evento -> misEventosService.estaUsuarioSuscrito(usuario.getIdUsuario(), evento.getIdEvento())
                        ));
            }
        }

        conteoSuscriptores = eventosPopulares.stream()
                .collect(Collectors.toMap(
                        Evento::getIdEvento,
                        evento -> misEventosService.contarSuscriptoresDeEvento(evento.getIdEvento())
                ));

        model.addAttribute("eventos", eventosPopulares);
        model.addAttribute("suscripciones", suscripciones);
        model.addAttribute("conteoSuscriptores", conteoSuscriptores);

        return "consumidor/eventos_populares";
    }
}