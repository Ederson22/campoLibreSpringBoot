package com.proyecto.campoLibre.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "evento")
@Getter
@Setter
@NoArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvento;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "ubicacion")
    private String ubicacion;

    @Column(name = "fecha_evento")
    private LocalDate fechaEvento;  // ✅ antes era Date

    @Column(name = "hora_evento")
    private LocalTime horaEvento;   // ✅ antes era String

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_evento")
    private TipoEvento tipoEvento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoEvento estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creado_por")
    private Usuario creador;
}
