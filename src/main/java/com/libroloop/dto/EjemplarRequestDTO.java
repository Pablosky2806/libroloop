package com.libroloop.dto;

import com.libroloop.entity.Ejemplar;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EjemplarRequestDTO {
    @NotNull(message = "El libro es obligatorio")
    private Long libroId;

    private Ejemplar.Estado estado;

    private String codigoInventario;

    private String ubicacion;

    private String observaciones;
}
