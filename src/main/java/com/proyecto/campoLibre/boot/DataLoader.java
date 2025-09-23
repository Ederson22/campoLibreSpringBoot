package com.proyecto.campoLibre.boot;

import com.proyecto.campoLibre.entity.*;
import com.proyecto.campoLibre.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final TiendaRepository tiendaRepository;
    private final ProductoRepository productoRepository;
    private final EventoRepository eventoRepository;
    private final PqrsRepository pqrsRepository;
    private final PqrsTiendaRepository pqrsTiendaRepository;
    private final PqrsEventoRepository pqrsEventoRepository;
    private final MisEventosRepository misEventosRepository;

    @Autowired
    public DataLoader(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                      RolRepository rolRepository, UsuarioRolRepository usuarioRolRepository,
                      TiendaRepository tiendaRepository, ProductoRepository productoRepository,
                      EventoRepository eventoRepository, PqrsRepository pqrsRepository,
                      PqrsTiendaRepository pqrsTiendaRepository, PqrsEventoRepository pqrsEventoRepository,
                      MisEventosRepository misEventosRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.tiendaRepository = tiendaRepository;
        this.productoRepository = productoRepository;
        this.eventoRepository = eventoRepository;
        this.pqrsRepository = pqrsRepository;
        this.pqrsTiendaRepository = pqrsTiendaRepository;
        this.pqrsEventoRepository = pqrsEventoRepository;
        this.misEventosRepository = misEventosRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Limpiar la base de datos antes de cargar nuevos datos (ORDEN CORRECTO)
        misEventosRepository.deleteAll();
        pqrsEventoRepository.deleteAll();
        pqrsTiendaRepository.deleteAll();
        pqrsRepository.deleteAll();
        eventoRepository.deleteAll(); // Eventos antes que usuarios (FK creado_por)
        productoRepository.deleteAll();
        tiendaRepository.deleteAll();
        usuarioRolRepository.deleteAll();
        usuarioRepository.deleteAll();
        rolRepository.deleteAll();

        // Si ya hay usuarios, no hace nada
        if (usuarioRepository.count() > 0) {
            return;
        }

        // 1. Crear y guardar los roles primero
        Rol administradorRol = new Rol();
        administradorRol.setNombre("ADMINISTRADOR");
        rolRepository.save(administradorRol);

        Rol proveedorRol = new Rol();
        proveedorRol.setNombre("PROVEEDOR");
        rolRepository.save(proveedorRol);

        Rol consumidorRol = new Rol();
        consumidorRol.setNombre("CONSUMIDOR");
        rolRepository.save(consumidorRol);

        // 2. Crear y guardar los usuarios
        Usuario usuario1 = new Usuario();
        usuario1.setNombre("Juan Pérez");
        usuario1.setEmail("juan.perez@example.com");
        usuario1.setContrasena(passwordEncoder.encode("pass123"));
        usuario1.setTelefono("3001234567");
        usuario1.setTipoDocumento("C.C.");
        usuario1.setDocumento("123456789");
        usuario1.setFecha_registro(new Date());
        usuarioRepository.save(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Ana García");
        usuario2.setEmail("ana.garcia@example.com");
        usuario2.setContrasena(passwordEncoder.encode("pass456"));
        usuario2.setTelefono("3107654321");
        usuario2.setTipoDocumento("C.C.");
        usuario2.setDocumento("987654321");
        usuario2.setFecha_registro(new Date());
        usuarioRepository.save(usuario2);

        Usuario usuario3 = new Usuario();
        usuario3.setNombre("Carlos Ruiz");
        usuario3.setEmail("carlos.ruiz@example.com");
        usuario3.setContrasena(passwordEncoder.encode("pass789"));
        usuario3.setTelefono("3201112233");
        usuario3.setTipoDocumento("C.C.");
        usuario3.setDocumento("44556677");
        usuario3.setFecha_registro(new Date());
        usuarioRepository.save(usuario3);

        // 3. Crear registros en la tabla de unión UsuarioRol
        UsuarioRol adminRolUsuario = new UsuarioRol();
        adminRolUsuario.setUsuario(usuario1);
        adminRolUsuario.setRol(administradorRol);
        usuarioRolRepository.save(adminRolUsuario);

        UsuarioRol proveedorRolUsuario = new UsuarioRol();
        proveedorRolUsuario.setUsuario(usuario2);
        proveedorRolUsuario.setRol(proveedorRol);
        usuarioRolRepository.save(proveedorRolUsuario);

        UsuarioRol consumidorRolUsuario = new UsuarioRol();
        consumidorRolUsuario.setUsuario(usuario3);
        consumidorRolUsuario.setRol(consumidorRol);
        usuarioRolRepository.save(consumidorRolUsuario);

        // 4. Crear una tienda de prueba
        Tienda tienda1 = new Tienda();
        tienda1.setNombre("La Tiendita de Ana");
        tienda1.setDescripcion("Productos artesanales de calidad.");
        tienda1.setEstado(EstadoTienda.ACTIVO);
        tienda1.setCorreoTienda("tiendita.ana@example.com");
        tienda1.setTelefonoTienda("3109876543");
        tienda1.setUbicacion("Centro");
        tienda1.setDueno(usuario2);
        tiendaRepository.save(tienda1);

        // 5. Crear un producto de prueba
        Producto producto1 = new Producto();
        producto1.setNombre("Mermelada de Fresa");
        producto1.setDescripcion("Hecha con fresas frescas.");
        producto1.setPrecio(BigDecimal.valueOf(15.50));
        producto1.setStock(50);
        producto1.setCategoria("Alimentos");
        producto1.setTienda(tienda1);
        productoRepository.save(producto1);

        // 6. Crear fechas futuras para los eventos
        Calendar calendar = Calendar.getInstance();

        // Evento 1: En 7 días
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date fechaEvento1 = calendar.getTime();

        // Evento 2: En 15 días
        calendar.add(Calendar.DAY_OF_MONTH, 8);
        Date fechaEvento2 = calendar.getTime();

        // Evento 3: En 30 días
        calendar.add(Calendar.DAY_OF_MONTH, 15);
        Date fechaEvento3 = calendar.getTime();

        // 7. Crear eventos de prueba con diferentes estados
        // Evento 1: PENDIENTE (creado por proveedor)
        Evento evento1 = new Evento();
        evento1.setNombre("Feria Artesanal del Campo");
        evento1.setDescripcion("Gran feria con productos artesanales y tradicionales del campo colombiano.");
        evento1.setUbicacion("Plaza Central de Zipaquirá");
        evento1.setFechaEvento(fechaEvento1);
        evento1.setHoraEvento("10:00");
        evento1.setTipoEvento(TipoEvento.TALLER);
        evento1.setEstado(EstadoEvento.PENDIENTE); // ¡IMPORTANTE: Agregar estado!
        evento1.setCreadoPor(usuario2); // Creado por el proveedor
        eventoRepository.save(evento1);

        // Evento 2: APROBADO (creado por proveedor, aprobado por admin)
        Evento evento2 = new Evento();
        evento2.setNombre("Taller de Agricultura Sostenible");
        evento2.setDescripcion("Aprende técnicas modernas de agricultura sostenible y orgánica.");
        evento2.setUbicacion("Finca El Paraíso, Chía");
        evento2.setFechaEvento(fechaEvento2);
        evento2.setHoraEvento("14:30");
        evento2.setTipoEvento(TipoEvento.CHARLA);
        evento2.setEstado(EstadoEvento.APROBADO); // Ya está aprobado
        evento2.setCreadoPor(usuario2); // Creado por el proveedor
        eventoRepository.save(evento2);

        // Evento 3: APROBADO (para que los consumidores puedan suscribirse)
        Evento evento3 = new Evento();
        evento3.setNombre("Reunión de Productores Locales");
        evento3.setDescripcion("Encuentro mensual de productores para intercambiar experiencias.");
        evento3.setUbicacion("Casa de la Cultura, Cajicá");
        evento3.setFechaEvento(fechaEvento3);
        evento3.setHoraEvento("09:00");
        evento3.setTipoEvento(TipoEvento.REUNION);
        evento3.setEstado(EstadoEvento.APROBADO);
        evento3.setCreadoPor(usuario2);
        eventoRepository.save(evento3);

        // 8. Crear un PQRS de prueba
        PQRS pqrs1 = new PQRS();
        pqrs1.setTipo(TipoPQRS.QUEJA);
        pqrs1.setDescripcion("El producto llegó dañado.");
        pqrs1.setFecha_envio(new Date());
        pqrs1.setEstado(EstadoPQRS.PENDIENTE);
        pqrs1.setEmisor(usuario3); // El consumidor envía la queja
        pqrs1.setReceptor(usuario2); // El proveedor la recibe
        pqrsRepository.save(pqrs1);

        // 9. Crear datos en las tablas de unión
        // PqrsTienda: Conecta el PQRS con la tienda
        PqrsTienda pqrsTienda1 = new PqrsTienda();
        pqrsTienda1.setPqrs(pqrs1);
        pqrsTienda1.setTienda(tienda1);
        pqrsTiendaRepository.save(pqrsTienda1);

        // PqrsEvento: Conecta la PQRS con un evento
        PqrsEvento pqrsEvento1 = new PqrsEvento();
        pqrsEvento1.setPqrs(pqrs1);
        pqrsEvento1.setEvento(evento2); // Cambiar a evento2 que está aprobado
        pqrsEventoRepository.save(pqrsEvento1);

        // 10. Crear registros en MisEventos (suscripciones)
        // Solo se puede suscribir a eventos APROBADOS
        MisEventos misEventos1 = new MisEventos();
        misEventos1.setUsuario(usuario3); // El consumidor
        misEventos1.setEvento(evento2);   // Evento aprobado
        misEventos1.setFechaGuardado(new Date());
        misEventosRepository.save(misEventos1);

        // Segundo registro de suscripción
        MisEventos misEventos2 = new MisEventos();
        misEventos2.setUsuario(usuario3); // El mismo consumidor
        misEventos2.setEvento(evento3);   // Otro evento aprobado
        misEventos2.setFechaGuardado(new Date());
        misEventosRepository.save(misEventos2);

        System.out.println("=== DATOS DE PRUEBA CARGADOS EXITOSAMENTE ===");
        System.out.println("Usuarios creados: " + usuarioRepository.count());
        System.out.println("Eventos creados: " + eventoRepository.count());
        System.out.println("- Pendientes: " + eventoRepository.findByEstado(EstadoEvento.PENDIENTE).size());
        System.out.println("- Aprobados: " + eventoRepository.findByEstado(EstadoEvento.APROBADO).size());
        System.out.println("Suscripciones creadas: " + misEventosRepository.count());
        System.out.println("===============================================");
    }
}