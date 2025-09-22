// Archivo: src/main/java/com/proyecto/campoLibre/entity/MisEventos.java

package com.proyecto.campoLibre.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "mis_eventos")
@Getter
@Setter
@NoArgsConstructor
public class MisEventos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_mis_eventos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento")
    private Evento evento;

    @Column(name = "fecha_guardado")
    @Temporal(TemporalType.DATE)
    private Date fecha_guardado;
}