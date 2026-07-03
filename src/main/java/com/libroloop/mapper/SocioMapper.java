package com.libroloop.mapper;

import com.libroloop.dto.SocioDTO;
import com.libroloop.dto.SocioRequestDTO;
import com.libroloop.entity.Socio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SocioMapper {

    @Mapping(target = "prestamoIds", source = "prestamos", qualifiedByName = "mapPrestamoIds")
    @Mapping(target = "reservaIds", source = "reservas", qualifiedByName = "mapReservaIds")
    SocioDTO toDTO(Socio socio);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "prestamos", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    Socio toEntity(SocioRequestDTO socioRequestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaRegistro", ignore = true)
    @Mapping(target = "prestamos", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    void updateEntityFromDTO(SocioDTO socioDTO, @MappingTarget Socio socio);

    List<SocioDTO> toDTOList(List<Socio> socios);

    @org.mapstruct.Named("mapPrestamoIds")
    default List<Long> mapPrestamoIds(List<?> prestamos) {
        if (prestamos == null) return null;
        return prestamos.stream()
                .map(obj -> ((com.libroloop.entity.Prestamo) obj).getId())
                .toList();
    }

    @org.mapstruct.Named("mapReservaIds")
    default List<Long> mapReservaIds(List<?> reservas) {
        if (reservas == null) return null;
        return reservas.stream()
                .map(obj -> ((com.libroloop.entity.Reserva) obj).getId())
                .toList();
    }
}
