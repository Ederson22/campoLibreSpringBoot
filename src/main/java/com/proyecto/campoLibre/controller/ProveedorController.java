package com.proyecto.campoLibre.controller;

import com.proyecto.campoLibre.dto.EventoDto;
import com.proyecto.campoLibre.dto.PqrsDto;
import com.proyecto.campoLibre.dto.ProductoDto;
import com.proyecto.campoLibre.dto.TiendaDto;
import com.proyecto.campoLibre.entity.*;
import com.proyecto.campoLibre.repository.EventoRepository;
import com.proyecto.campoLibre.repository.TiendaRepository;
import com.proyecto.campoLibre.service.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/proveedor")
public class ProveedorController {

    private final TiendaService tiendaService;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    private final PqrsService pqrsService;
    private final TiendaRepository tiendaRepository; // Inyecta el repositorio de tiendas
    private final EventoRepository eventoRepository; // Inyecta el repositorio de eventos
    private final EventoService eventoService;

    // MODIFICA EL CONSTRUCTOR
    public ProveedorController(TiendaService tiendaService, UsuarioService usuarioService, ProductoService productoService, PqrsService pqrsService, TiendaRepository tiendaRepository, EventoRepository eventoRepository, EventoService eventoService) {
        this.tiendaService = tiendaService;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
        this.pqrsService = pqrsService;
        this.tiendaRepository = tiendaRepository;
        this.eventoRepository = eventoRepository;
        this.eventoService = eventoService;
    }


    @GetMapping
    public String proveedorHome(Model model, Authentication authentication) {
        String email = authentication.getName();
        Optional<Usuario> usuario = usuarioService.findByEmail(email);

        if (usuario.isPresent()) {
            boolean tieneTienda = tiendaService.findByUsuario(usuario.get().getIdUsuario()).isPresent();
            model.addAttribute("tieneTienda", tieneTienda);
        } else {
            model.addAttribute("tieneTienda", false);
        }

        return "proveedor/proveedorHome";
    }

    @GetMapping("/dashboard")
    public String proveedorDashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOptional = usuarioService.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            Long idUsuario = usuarioOptional.get().getIdUsuario();

            // Llama al nuevo método del servicio para buscar la tienda del usuario
            Optional<Tienda> tiendaOptional = tiendaService.findByDuenoIdUsuario(idUsuario);

            if (tiendaOptional.isPresent()) {
                // Si el usuario ya tiene una tienda, puedes mostrarle su panel de gestión
                model.addAttribute("tienda", tiendaOptional.get());
                // Por ejemplo, puedes redirigir a una página que muestre el estado de su tienda
                return "proveedor/dashboard";
            } else {
                // Si el usuario no tiene una tienda, redirígelo a la página para crear una
                return "redirect:/proveedor/tienda/crear";
            }
        }
        // Si no se encuentra el usuario, se le redirige a la página de inicio o login
        return "redirect:/";
    }
    //______________________________________PRODUCTOS______________________________________
    @GetMapping("/productos/crear")
    public String showCrearProductoForm(Model model) {
        model.addAttribute("producto", new ProductoDto());
        return "proveedor/crear_producto";
    }

    @PostMapping("/productos/crear")
    public String crearProducto(@Valid @ModelAttribute("producto") ProductoDto productoDto,
                                BindingResult result,
                                Authentication authentication) {
        if (result.hasErrors()) {
            return "proveedor/crear_producto";
        }

        String email = authentication.getName();
        Usuario usuario = usuarioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        productoService.guardar(productoDto, usuario.getIdUsuario());

        return "redirect:/proveedor/productos";
    }
    @GetMapping("/productos/editar/{id}")
    public String showEditarProductoForm(@PathVariable("id") Long id, Model model) {
        Optional<Producto> productoOptional = productoService.findById(id);
        if (productoOptional.isPresent()) {
            model.addAttribute("producto", productoOptional.get());
            return "proveedor/editar_producto";
        }
        return "redirect:/proveedor/productos";
    }

    @GetMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") Long id, Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        productoService.eliminar(id, usuario.getIdUsuario());
        return "redirect:/proveedor/productos";
    }

    @PostMapping("/productos/editar/{id}")
    public String actualizarProducto(@PathVariable("id") Long id,
                                     @Valid @ModelAttribute("producto") ProductoDto productoDto,
                                     BindingResult result) {
        if (result.hasErrors()) {
            // El modelo se mantiene con los errores, así que la vista mostrará los mensajes
            return "proveedor/editar_producto";
        }

        // Llamar al servicio para actualizar el producto
        productoService.actualizar(id, productoDto);

        return "redirect:/proveedor/productos";
    }

    //______________________________________TIENDA______________________________________
    @GetMapping("/tienda/crear")
    public String showCrearTiendaForm(Model model, Authentication authentication) {
        String email = authentication.getName();
        // Busca el usuario por email
        Optional<Usuario> usuario = usuarioService.findByEmail(email);

        if (usuario.isPresent()) {
            // Verifica si el usuario ya tiene una tienda
            Optional<Tienda> tienda = tiendaService.findByDuenoIdUsuario(usuario.get().getIdUsuario());
            if (tienda.isEmpty()) {
                // Si no tiene tienda, se le muestra el formulario
                model.addAttribute("tienda", new TiendaDto());
                return "proveedor/crear_tienda";
            }
        }
        // Si ya tiene tienda o no se encuentra el usuario, se redirige al dashboard
        return "redirect:/proveedor/dashboard";
    }

    @PostMapping("/tienda/crear")
    public String crearTienda(@Valid @ModelAttribute("tienda") TiendaDto tiendaDto, BindingResult result, Authentication authentication) {
        if (result.hasErrors()) {
            return "proveedor/crear_tienda";
        }

        String email = authentication.getName();
        Optional<Usuario> usuario = usuarioService.findByEmail(email);

        if (usuario.isPresent()) {
            tiendaService.guardar(tiendaDto, usuario.get().getIdUsuario());
            return "redirect:/proveedor/dashboard";
        }
        // Si no se encuentra el usuario, se redirige
        return "redirect:/proveedor/dashboard";
    }

    @GetMapping("/tienda/ver")
    public String verTienda(Model model, Authentication authentication) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOptional = usuarioService.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            Long idUsuario = usuarioOptional.get().getIdUsuario();
            Optional<Tienda> tiendaOptional = tiendaService.findByDuenoIdUsuario(idUsuario);

            if (tiendaOptional.isPresent()) {
                model.addAttribute("tienda", tiendaOptional.get());
                return "proveedor/ver_tienda";
            } else {
                return "redirect:/proveedor/tienda/crear";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/tienda/editar")
    public String showEditarTiendaForm(Model model, Authentication authentication) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOptional = usuarioService.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            Optional<Tienda> tiendaOptional = tiendaService.findByDuenoIdUsuario(usuarioOptional.get().getIdUsuario());
            if (tiendaOptional.isPresent()) {
                model.addAttribute("tienda", tiendaOptional.get());
                return "proveedor/editar_tienda";
            }
        }
        return "redirect:/proveedor/dashboard";
    }

    @PostMapping("/tienda/editar")
    public String editarTienda(@Valid @ModelAttribute("tienda") TiendaDto tiendaDto,
                               BindingResult result,
                               Authentication authentication,
                               Model model) { // <-- ¡Añade el objeto Model aquí!
        if (result.hasErrors()) {
            // En caso de error, vuelve a pasar el DTO al modelo
            model.addAttribute("tienda", tiendaDto);
            return "proveedor/editar_tienda";
        }

        String email = authentication.getName();
        Usuario usuario = usuarioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        tiendaService.actualizar(tiendaDto, usuario.getIdUsuario());

        return "redirect:/proveedor/tienda/ver";
    }

    @GetMapping("/tienda/eliminar")
    public String eliminarTienda(Authentication authentication) {
        String email = authentication.getName();
        Usuario usuario = usuarioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        tiendaService.eliminar(usuario.getIdUsuario());

        // Redirige al usuario a la página de creación de tienda o a la página de inicio
        return "redirect:/proveedor/dashboard";
    }

    //______________________________________FILTROS PARA PRODUCTOS______________________________________

    @GetMapping("/productos")
    public String gestionProductos(Model model, Authentication authentication,
                                   @RequestParam(name = "nombre", required = false) String nombre,
                                   @RequestParam(name = "categoria", required = false) String categoria,
                                   @RequestParam(name = "stock", required = false) Integer stock) {

        String email = authentication.getName();
        Usuario usuario = usuarioService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Aquí nos aseguramos de que el usuario tenga una tienda asociada
        if (usuario.getTienda() == null) {
            return "redirect:/proveedor/dashboard"; // O a la página de creación de tienda
        }

        Long idTienda = usuario.getTienda().getIdTienda();

        // Llama al servicio de búsqueda. Si los parámetros son nulos, la consulta SQL los ignorará
        List<Producto> productos = productoService.buscarProductos(idTienda, nombre, categoria, stock);

        model.addAttribute("productos", productos);
        return "proveedor/gestion_productos";
    }

    //___________________________________PQRS_________________________________

    @GetMapping("/pqrs")
    public String gestionPqrs(Model model, Authentication authentication) {
        String email = authentication.getName(); // Obtienes el email del usuario logueado

        Optional<Usuario> usuarioOptional = usuarioService.findByEmail(email); // Buscas el usuario por su email

        if (usuarioOptional.isPresent()) {
            Long idUsuario = usuarioOptional.get().getIdUsuario(); // Obtienes el ID del usuario
            List<PQRS> pqrsList = pqrsService.obtenerPqrsPorEmisor(idUsuario); // Filtra las PQRS por el ID
            model.addAttribute("pqrsList", pqrsList);
            return "proveedor/gestion_pqrs";
        }

        // Si no se encuentra el usuario, redirige al dashboard o a una página de error
        return "redirect:/proveedor/dashboard";
    }

    @GetMapping("/pqrs/crear")
    public String crearPqrs(Model model) {
        model.addAttribute("pqrsDto", new PqrsDto());
        model.addAttribute("tipos", TipoPQRS.values());
        model.addAttribute("tiendas", tiendaService.obtenerTodasLasTiendas());
        model.addAttribute("eventos", eventoService.obtenerTodosLosEventos());
        return "proveedor/crear_pqrs";
    }

    @PostMapping("/pqrs/guardar")
    public String guardarPqrs(@Valid @ModelAttribute("pqrsDto") PqrsDto pqrsDto,
                              BindingResult result,
                              Authentication authentication,
                              Model model) {
        // ... (tu lógica de validación)

        String emailUsuario = authentication.getName();
        Optional<Usuario> emisorOptional = usuarioService.findByEmail(emailUsuario);

        if (emisorOptional.isPresent()) {
            pqrsService.guardarPqrs(pqrsDto, emisorOptional.get().getIdUsuario());
            return "redirect:/proveedor/pqrs"; // <-- ¡Corrección aquí!
        } else {
            return "redirect:/error";
        }
    }
    @GetMapping("/pqrs/editar/{id}")
    public String showEditarPqrsForm(@PathVariable("id") Long id, Model model) {
        Optional<PQRS> pqrsOptional = pqrsService.findById(id);
        if (pqrsOptional.isPresent()) {
            model.addAttribute("pqrs", pqrsOptional.get());
            model.addAttribute("tipos", TipoPQRS.values());
            return "proveedor/editar_pqrs";
        }
        return "redirect:/proveedor/pqrs";
    }

    @PostMapping("/pqrs/editar/{id}")
    public String editarPqrs(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("pqrs") PqrsDto pqrsDto,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "proveedor/editar_pqrs";
        }

        pqrsService.actualizar(id, pqrsDto);
        return "redirect:/proveedor/pqrs";
    }

    @GetMapping("/pqrs/cancelar/{id}")
    public String cancelarPqrs(@PathVariable("id") Long id) {
        pqrsService.cancelar(id);
        return "redirect:/proveedor/pqrs";
    }


    //___________________________________EVENTOS_________________________________
    @GetMapping("/eventos/crear")
    public String crearEvento(Model model) {
        // Ahora pasas un EventoDto al modelo
        model.addAttribute("evento", new EventoDto());
        model.addAttribute("tiposEvento", TipoEvento.values());
        return "proveedor/crear_evento";
    }

    @PostMapping("/eventos/guardar")
    public String guardarEvento(@ModelAttribute EventoDto eventoDto, Authentication authentication) {

        // Obten el usuario creador
        String emailUsuario = authentication.getName();
        Usuario creador = usuarioService.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Mapea los datos del DTO a la entidad Evento
        Evento evento = new Evento();
        evento.setNombre(eventoDto.getNombre());
        evento.setDescripcion(eventoDto.getDescripcion());
        evento.setUbicacion(eventoDto.getUbicacion());
        evento.setFecha_evento(eventoDto.getFecha_evento());
        evento.setHora_evento(eventoDto.getHora_evento());
        evento.setTipo_evento(eventoDto.getTipo_evento());

        // Guarda la entidad con el usuario creador
        eventoService.guardarEvento(evento, creador);
        return "redirect:/proveedor/eventos";
    }


}