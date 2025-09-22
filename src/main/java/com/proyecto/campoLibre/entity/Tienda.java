// Archivo: src/main/java/com/proyecto/campoLibre/entity/Tienda.java

package com.proyecto.campoLibre.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tienda")
@Getter
@Setter
@NoArgsConstructor
public class Tienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTienda;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoTienda estado;

    @Column(name = "correo_tienda")
    private String correoTienda;

    @Column(name = "telefono_tienda")
    private String telefonoTienda;

    @Column(name = "ubicacion")
    private String ubicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario dueno;

    @Column(name = "imagen_tienda")
    private String imagenTienda;
}