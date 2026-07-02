package com.libroloop.dto;

import com.libroloop.entity.Autor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutorDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String biografia;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private List<Long> libroIds;
}
