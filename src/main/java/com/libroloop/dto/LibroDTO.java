package com.libroloop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroDTO {
    private Long id;
    private String isbn;
    private String titulo;
    private String descripcion;
    private Year anioPublicacion;
    private String editorial;
    private Integer numeroPaginas;
    private Long autorId;
    private String autorNombre;
    private List<Long> categoriaIds;
    private List<String> categoriaNombres;
    private List<Long> ejemplarIds;
}
