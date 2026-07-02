package com.libroloop.dto;

import com.libroloop.entity.Ejemplar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EjemplarDTO {
    private Long id;
    private Long libroId;
    private String libroTitulo;
    private Ejemplar.Estado estado;
    private String codigoInventario;
    private String ubicacion;
    private LocalDateTime fechaAdquisicion;
    private String observaciones;
}
