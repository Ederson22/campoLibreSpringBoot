// Archivo: src/main/java/com/proyecto/campoLibre/entity/Rol.java

package com.proyecto.campoLibre.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rol")
@Getter
@Setter
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    @Column(name = "nombre", unique = true, nullable = false)
    private String nombre;
}