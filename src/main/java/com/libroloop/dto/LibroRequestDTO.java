package com.libroloop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroRequestDTO {
    @NotBlank(message = "El ISBN es obligatorio")
    @Size(max = 20, message = "El ISBN no puede exceder 20 caracteres")
    private String isbn;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede exceder 200 caracteres")
    private String titulo;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;

    private Year anioPublicacion;

    @Size(max = 100, message = "La editorial no puede exceder 100 caracteres")
    private String editorial;

    @Min(value = 1, message = "El número de páginas debe ser al menos 1")
    private Integer numeroPaginas;

    @NotNull(message = "El autor es obligatorio")
    private Long autorId;

    private List<Long> categoriaIds;
}
