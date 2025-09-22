package com.proyecto.campoLibre.implement;

import com.proyecto.campoLibre.dto.ProductoDto;
import com.proyecto.campoLibre.entity.Producto;
import com.proyecto.campoLibre.entity.Tienda;
import com.proyecto.campoLibre.entity.Usuario;
import com.proyecto.campoLibre.repository.ProductoRepository;
import com.proyecto.campoLibre.repository.TiendaRepository;
import com.proyecto.campoLibre.service.ProductoService;
import com.proyecto.campoLibre.service.TiendaService;
import com.proyecto.campoLibre.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final TiendaService tiendaService;
    private final TiendaRepository tiendaRepository;
    private final UsuarioService usuarioService;
    private final ModelMapper modelMapper;
    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository, TiendaService tiendaService, TiendaRepository tiendaRepository, UsuarioService usuarioService, ModelMapper modelMapper) {
        this.productoRepository = productoRepository;
        this.tiendaService = tiendaService;
        this.tiendaRepository = tiendaRepository;
        this.usuarioService = usuarioService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void guardar(ProductoDto productoDto, Long idUsuario) {
        Optional<Tienda> tiendaOptional = tiendaRepository.findByDuenoIdUsuario(idUsuario);
        if (tiendaOptional.isEmpty()) {
            throw new RuntimeException("No se puede crear un producto sin una tienda asociada.");
        }

        Producto producto = modelMapper.map(productoDto, Producto.class);
        producto.setTienda(tiendaOptional.get());
        productoRepository.save(producto);
    }

    @Override
    public List<Producto> findByTiendaId(Long idTienda) {
        return productoRepository.findByTiendaIdTienda(idTienda);
    }

    @Override
    public Optional<Producto> findById(Long idProducto) {
        return productoRepository.findById(idProducto);
    }

    @Override
    public void actualizar(Long idProducto, ProductoDto productoDto) {
        Optional<Producto> productoOptional = productoRepository.findById(idProducto);
        if (productoOptional.isPresent()) {
            Producto productoExistente = productoOptional.get();
            // Actualizar los campos con los datos del DTO
            productoExistente.setNombre(productoDto.getNombre());
            productoExistente.setDescripcion(productoDto.getDescripcion());
            productoExistente.setPrecio(productoDto.getPrecio());
            productoExistente.setStock(productoDto.getStock());
            productoExistente.setImagen_producto(productoDto.getImagen_producto()); // Mapear imagen

            productoRepository.save(productoExistente);
        } else {
            throw new RuntimeException("Producto no encontrado con ID: " + idProducto);
        }
    }


    @Override
    public void eliminar(Long idProducto, Long idUsuario) {
        // 1. Encontrar la tienda del usuario
        Optional<Tienda> tiendaOptional = tiendaRepository.findByDuenoIdUsuario(idUsuario);

        if (tiendaOptional.isPresent()) {
            Long idTienda = tiendaOptional.get().getIdTienda();

            // 2. Usar el nuevo método del repositorio para verificar que el producto pertenece a la tienda
            Optional<Producto> productoOptional = productoRepository.findByIdProductoAndTiendaIdTienda(idProducto, idTienda);

            // 3. Si el producto se encuentra y pertenece a la tienda, lo elimina
            if (productoOptional.isPresent()) {
                productoRepository.delete(productoOptional.get());
            } else {
                throw new RuntimeException("El producto no existe o no pertenece a tu tienda.");
            }
        } else {
            throw new RuntimeException("El usuario no tiene una tienda asociada.");
        }
    }
    //______________________________________FILTROS______________________________________
    // Implementación del método que llama a la nueva consulta multicriterio del repositorio
    @Override
    public List<Producto> buscarProductos(Long idTienda, String nombre, String categoria, Integer stock) {
        // Si el nombre es una cadena vacía, la convertimos a null para que la consulta lo ignore
        String nombreFiltrado = (nombre != null && nombre.isEmpty()) ? null : nombre;

        // Lo mismo para la categoría si es necesario
        String categoriaFiltrada = (categoria != null && categoria.isEmpty()) ? null : categoria;

        return productoRepository.buscarProductosMulticriterio(idTienda, nombreFiltrado, categoriaFiltrada, stock);
    }
}