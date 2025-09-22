// Archivo: src/main/java/com/proyecto/campoLibre/repository/RolRepository.java

package com.proyecto.campoLibre.repository;

import com.proyecto.campoLibre.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    // Puedes agregar métodos de búsqueda personalizados aquí si los necesitas
    Rol findByNombre(String nombre);
}