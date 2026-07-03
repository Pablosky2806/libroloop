package com.libroloop.dto;

import com.libroloop.entity.Socio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;
    private LocalDate fechaRegistro;
    private Boolean activo;
    private Socio.Rol rol;
    private List<Long> prestamoIds;
    private List<Long> reservaIds;
}
