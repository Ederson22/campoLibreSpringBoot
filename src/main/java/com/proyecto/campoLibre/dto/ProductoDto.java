package com.proyecto.campoLibre.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductoDto {

    private Long id;

    @NotEmpty(message = "El nombre del producto no puede estar vacío")
    private String nombre;

    @NotEmpty(message = "La descripción no puede estar vacía")
    private String descripcion;

    @NotNull(message = "El precio no puede ser nulo")
    private BigDecimal precio;

    private int stock;

    private String categoria;

    private String imagen_producto;
}