package com.proyecto.campoLibre.controller;

import com.proyecto.campoLibre.entity.Evento;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EventoService eventoService;
    private final MisEventosService misEventosService;
    private final UsuarioService usuarioService;

    @Autowired
    public AdminController(EventoService eventoService,
                           MisEventosService misEventosService,
                           UsuarioService usuarioService) {
        this.eventoService = eventoService;
        this.misEventosService = misEventosService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String adminHome(Model model) {
        // Estadísticas para el dashboard
        List<Evento> eventosPendientes = eventoService.obtenerEventosPendientes();
        List<Evento> eventosAprobados = eventoService.obtenerEventosPorEstado(EstadoEvento.APROBADO);
        List<Evento> todosLosEventos = eventoService.obtenerTodosLosEventos();

        // Contadores para el dashboard
        long totalEventos = todosLosEventos.size();
        long eventosPendientesCount = eventosPendientes.size();
        long eventosAprobadosCount = eventosAprobados.size();
        long eventosRechazadosCount = eventoService.obtenerEventosPorEstado(EstadoEvento.RECHAZADO).size();

        model.addAttribute("totalEventos", totalEventos);
        model.addAttribute("eventosPendientes", eventosPendientesCount);
        model.addAttribute("eventosAprobados", eventosAprobadosCount);
        model.addAttribute("eventosRechazados", eventosRechazadosCount);
        model.addAttribute("eventosPendientesList", eventosPendientes.stream().limit(5).collect(Collectors.toList()));

        return "admin/adminHome";
    }

    @GetMapping("/users")
    public String manageUsers() {
        return "admin/users";
    }

    //______________________________________GESTIÓN DE EVENTOS______________________________________

    @GetMapping("/eventos")
    public String gestionEventos(Model model,
                                 @RequestParam(name = "estado", required = false) EstadoEvento estado,
                                 @RequestParam(name = "tipo", required = false) TipoEvento tipo,
                                 @RequestParam(name = "nombre", required = false) String nombre) {

        List<Evento> eventos;

        if (nombre != null || tipo != null || estado != null) {
            // Buscar con filtros
            eventos = eventoService.buscarEventosConFiltros(nombre, tipo, null, null, null, estado);
        } else {
            // Sin filtros, obtener todos los eventos
            eventos = eventoService.obtenerTodosLosEventos();
        }

        // Obtener conteo de suscriptores para cada evento
        Map<Long, Long> conteoSuscriptores = eventos.stream()
                .collect(Collectors.toMap(
                        Evento::getIdEvento,
                        evento -> misEventosService.contarSuscriptoresDeEvento(evento.getIdEvento())
                ));

        model.addAttribute("eventos", eventos);
        model.addAttribute("estadosEvento", EstadoEvento.values());
        model.addAttribute("tiposEvento", TipoEvento.values());
        model.addAttribute("conteoSuscriptores", conteoSuscriptores);

        return "admin/gestion_eventos";
    }

    @GetMapping("/eventos/pendientes")
    public String eventosPendientes(Model model) {
        List<Evento> eventosPendientes = eventoService.obtenerEventosPendientes();

        // Obtener información adicional para cada evento
        Map<Long, Long> conteoSuscriptores = eventosPendientes.stream()
                .collect(Collectors.toMap(
                        Evento::getIdEvento,
                        evento -> misEventosService.contarSuscriptoresDeEvento(evento.getIdEvento())
                ));

        model.addAttribute("eventos", eventosPendientes);
        model.addAttribute("conteoSuscriptores", conteoSuscriptores);

        return "admin/eventos_pendientes";
    }

    @GetMapping("/eventos/ver/{id}")
    public String verEvento(@PathVariable("id") Long id, Model model) {
        Optional<Evento> eventoOptional = eventoService.obtenerEventoPorId(id);

        if (eventoOptional.isPresent()) {
            Evento evento = eventoOptional.get();
            Long totalSuscriptores = misEventosService.contarSuscriptoresDeEvento(id);

            model.addAttribute("evento", evento);
            model.addAttribute("totalSuscriptores", totalSuscriptores);
            return "admin/ver_evento";
        }

        return "redirect:/admin/eventos";
    }

    //______________________________________ACCIONES DE APROBACIÓN______________________________________

    @PostMapping("/eventos/aprobar/{id}")
    public String aprobarEvento(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes) {
        try {
            Evento evento = eventoService.aprobarEvento(id);
            redirectAttributes.addFlashAttribute("mensaje",
                    "Evento '" + evento.getNombre() + "' aprobado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/admin/eventos/pendientes";
    }

    @PostMapping("/eventos/rechazar/{id}")
    public String rechazarEvento(@PathVariable("id") Long id,
                                 RedirectAttributes redirectAttributes) {
        try {
            Evento evento = eventoService.rechazarEvento(id);
            redirectAttributes.addFlashAttribute("mensaje",
                    "Evento '" + evento.getNombre() + "' rechazado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/admin/eventos/pendientes";
    }

    @PostMapping("/eventos/cancelar/{id}")
    public String cancelarEvento(@PathVariable("id") Long id,
                                 RedirectAttributes redirectAttributes) {
        try {
            Evento evento = eventoService.cancelarEvento(id);
            redirectAttributes.addFlashAttribute("mensaje",
                    "Evento '" + evento.getNombre() + "' cancelado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "info");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/admin/eventos";
    }

    //______________________________________ACCIONES MASIVAS______________________________________

    @PostMapping("/eventos/aprobar-masivo")
    public String aprobarEventosMasivo(@RequestParam("eventosIds") List<Long> eventosIds,
                                       RedirectAttributes redirectAttributes) {
        int aprobados = 0;
        int errores = 0;

        for (Long id : eventosIds) {
            try {
                eventoService.aprobarEvento(id);
                aprobados++;
            } catch (RuntimeException e) {
                errores++;
            }
        }

        if (aprobados > 0) {
            redirectAttributes.addFlashAttribute("mensaje",
                    aprobados + " evento(s) aprobado(s) exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        }

        if (errores > 0) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    errores + " evento(s) no pudieron ser aprobados");
        }

        return "redirect:/admin/eventos/pendientes";
    }

    @PostMapping("/eventos/rechazar-masivo")
    public String rechazarEventosMasivo(@RequestParam("eventosIds") List<Long> eventosIds,
                                        RedirectAttributes redirectAttributes) {
        int rechazados = 0;
        int errores = 0;

        for (Long id : eventosIds) {
            try {
                eventoService.rechazarEvento(id);
                rechazados++;
            } catch (RuntimeException e) {
                errores++;
            }
        }

        if (rechazados > 0) {
            redirectAttributes.addFlashAttribute("mensaje",
                    rechazados + " evento(s) rechazado(s)");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
        }

        if (errores > 0) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    errores + " evento(s) no pudieron ser rechazados");
        }

        return "redirect:/admin/eventos/pendientes";
    }

    //______________________________________ESTADÍSTICAS Y REPORTES______________________________________

    @GetMapping("/eventos/estadisticas")
    public String estadisticasEventos(Model model) {
        // Estadísticas generales
        List<Evento> todosEventos = eventoService.obtenerTodosLosEventos();
        Map<EstadoEvento, Long> eventosPorEstado = todosEventos.stream()
                .collect(Collectors.groupingBy(Evento::getEstado, Collectors.counting()));

        Map<TipoEvento, Long> eventosPorTipo = todosEventos.stream()
                .collect(Collectors.groupingBy(Evento::getTipoEvento, Collectors.counting()));

        // Eventos más populares
        List<Evento> eventosPopulares = misEventosService.obtenerEventosMasPopulares();

        // Proveedores más activos (top 5)
        Map<Usuario, Long> proveedoresActivos = todosEventos.stream()
                .collect(Collectors.groupingBy(Evento::getCreadoPor, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Usuario, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        java.util.LinkedHashMap::new
                ));

        model.addAttribute("eventosPorEstado", eventosPorEstado);
        model.addAttribute("eventosPorTipo", eventosPorTipo);
        model.addAttribute("eventosPopulares", eventosPopulares);
        model.addAttribute("proveedoresActivos", proveedoresActivos);
        model.addAttribute("totalEventos", todosEventos.size());

        return "admin/estadisticas_eventos";
    }

    //______________________________________ELIMINAR EVENTOS______________________________________

    @PostMapping("/eventos/eliminar/{id}")
    public String eliminarEvento(@PathVariable("id") Long id,
                                 RedirectAttributes redirectAttributes) {
        try {
            Optional<Evento> eventoOptional = eventoService.obtenerEventoPorId(id);
            if (eventoOptional.isPresent()) {
                String nombreEvento = eventoOptional.get().getNombre();
                eventoService.eliminarEvento(id);
                redirectAttributes.addFlashAttribute("mensaje",
                        "Evento '" + nombreEvento + "' eliminado exitosamente");
                redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/admin/eventos";
    }
}