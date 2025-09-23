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
    private Long idMisEventos;  // Cambio: id_mis_eventos -> idMisEventos

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento")
    private Evento evento;

    @Column(name = "fecha_guardado")
    @Temporal(TemporalType.DATE)
    private Date fechaGuardado;  // Cambio: fecha_guardado -> fechaGuardado
}