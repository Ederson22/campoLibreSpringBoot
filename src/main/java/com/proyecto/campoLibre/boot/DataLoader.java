package com.proyecto.campoLibre.boot;

import com.proyecto.campoLibre.entity.*;
import com.proyecto.campoLibre.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
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
        // Limpiar la base de datos antes de cargar nuevos datos
        misEventosRepository.deleteAll();
        pqrsEventoRepository.deleteAll();
        pqrsTiendaRepository.deleteAll();
        pqrsRepository.deleteAll();
        eventoRepository.deleteAll();
        productoRepository.deleteAll();
        tiendaRepository.deleteAll();
        usuarioRolRepository.deleteAll();
        usuarioRepository.deleteAll();
        rolRepository.deleteAll(); // ¡También limpia la tabla de roles!

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
        // Asignamos un rol a cada usuario
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

        // 3. Crear una tienda de prueba
        Tienda tienda1 = new Tienda();
        tienda1.setNombre("La Tiendita de Ana");
        tienda1.setDescripcion("Productos artesanales de calidad.");
        tienda1.setEstado(EstadoTienda.ACTIVO);
        tienda1.setCorreoTienda("tiendita.ana@example.com");
        tienda1.setTelefonoTienda("3109876543");
        tienda1.setUbicacion("Centro");
        tienda1.setDueno(usuario2);
        tiendaRepository.save(tienda1);

        // 4. Crear un producto de prueba
        Producto producto1 = new Producto();
        producto1.setNombre("Mermelada de Fresa");
        producto1.setDescripcion("Hecha con fresas frescas.");
        producto1.setPrecio(BigDecimal.valueOf(15.50));
        producto1.setStock(50);
        producto1.setCategoria("Alimentos");
        producto1.setTienda(tienda1);
        productoRepository.save(producto1);

        // 5. Crear un evento de prueba
        Evento evento1 = new Evento();
        evento1.setNombre("Feria Artesanal");
        evento1.setDescripcion("Gran feria con productos de artesanos locales.");
        evento1.setUbicacion("Parque Central");
        evento1.setFecha_evento(new Date());
        evento1.setHora_evento("10:00 AM");
        evento1.setTipo_evento(TipoEvento.TALLER);
        evento1.setCreado_por(usuario1);
        eventoRepository.save(evento1);

        // 6. Crear un PQRS de prueba
        PQRS pqrs1 = new PQRS();
        pqrs1.setTipo(TipoPQRS.QUEJA);
        pqrs1.setDescripcion("El producto llegó dañado.");
        pqrs1.setFecha_envio(new Date());
        pqrs1.setEstado(EstadoPQRS.PENDIENTE);
        pqrs1.setEmisor(usuario3); // El consumidor envía la queja
        pqrs1.setReceptor(usuario2); // El proveedor la recibe
        pqrsRepository.save(pqrs1);

        // 7. Crear datos en las tablas de unión
        // PqrsTienda: Conecta el PQRS con la tienda
        PqrsTienda pqrsTienda1 = new PqrsTienda();
        pqrsTienda1.setPqrs(pqrs1);
        pqrsTienda1.setTienda(tienda1);
        pqrsTiendaRepository.save(pqrsTienda1);

        // 8. Crear un registro en la tabla de unión PqrsEvento
        // Conecta la PQRS creada con el evento creado
        PqrsEvento pqrsEvento1 = new PqrsEvento();
        pqrsEvento1.setPqrs(pqrs1);
        pqrsEvento1.setEvento(evento1);
        pqrsEventoRepository.save(pqrsEvento1);

        // MisEventos: Conecta el usuario que guarda el evento
        MisEventos misEventos1 = new MisEventos();
        misEventos1.setUsuario(usuario3);
        misEventos1.setEvento(evento1);
        misEventos1.setFecha_guardado(new Date());
        misEventosRepository.save(misEventos1);

        System.out.println("Datos de prueba completos y cargados con éxito.");
    }
}