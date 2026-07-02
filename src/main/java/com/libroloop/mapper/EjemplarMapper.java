package com.libroloop.mapper;

import com.libroloop.dto.EjemplarDTO;
import com.libroloop.dto.EjemplarRequestDTO;
import com.libroloop.entity.Ejemplar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EjemplarMapper {

    @Mapping(target = "libroId", source = "libro.id")
    @Mapping(target = "libroTitulo", source = "libro.titulo")
    EjemplarDTO toDTO(Ejemplar ejemplar);

    @Mapping(target = "libro", ignore = true)
    Ejemplar toEntity(EjemplarDTO ejemplarDTO);

    @Mapping(target = "libro", ignore = true)
    Ejemplar toEntity(EjemplarRequestDTO ejemplarRequestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "libro", ignore = true)
    void updateEntityFromDTO(EjemplarDTO ejemplarDTO, @MappingTarget Ejemplar ejemplar);

    List<EjemplarDTO> toDTOList(List<Ejemplar> ejemplares);
}
