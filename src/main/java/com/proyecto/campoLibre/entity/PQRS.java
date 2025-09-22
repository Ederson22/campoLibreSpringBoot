package com.proyecto.campoLibre.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "pqrs")
@Getter
@Setter
@NoArgsConstructor
public class PQRS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPqrs;

    @Enumerated(EnumType.STRING)
    private TipoPQRS tipo;

    private String descripcion;
    private Date fecha_envio;

    @Enumerated(EnumType.STRING)
    private EstadoPQRS estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_emisor")
    private Usuario emisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_receptor")
    private Usuario receptor;


    // Aquí no se declaran campos para Tienda o Evento
}