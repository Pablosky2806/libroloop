package com.libroloop.mapper;

import com.libroloop.dto.AutorDTO;
import com.libroloop.dto.AutorRequestDTO;
import com.libroloop.entity.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    AutorDTO toDTO(Autor autor);

    Autor toEntity(AutorDTO autorDTO);

    Autor toEntity(AutorRequestDTO autorRequestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "libros", ignore = true)
    void updateEntityFromDTO(AutorDTO autorDTO, @MappingTarget Autor autor);

    List<AutorDTO> toDTOList(List<Autor> autores);
}
