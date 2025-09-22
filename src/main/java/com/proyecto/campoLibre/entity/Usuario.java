// Archivo: src/main/java/com/proyecto/campoLibre/entity/Usuario.java

package com.proyecto.campoLibre.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "contrasena")
    private String contrasena;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "documento", unique = true)
    private String documento;

    @Column(name = "fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date fecha_registro;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UsuarioRol> usuarioRoles = new HashSet<>(); // <-- ¡Inicializamos el Set aquí!

    public void addRol(UsuarioRol usuarioRol) {
        this.usuarioRoles.add(usuarioRol);
        usuarioRol.setUsuario(this);
    }

    @OneToOne(mappedBy = "dueno") // 'dueno' es el nombre del campo en la clase Tienda
    private Tienda tienda;
}