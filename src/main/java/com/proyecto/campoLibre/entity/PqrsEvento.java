package com.proyecto.campoLibre.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pqrs_evento")
@Getter
@Setter
@NoArgsConstructor
public class PqrsEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pqrs")
    private PQRS pqrs;

    @ManyToOne
    @JoinColumn(name = "id_evento")
    private Evento evento;
}