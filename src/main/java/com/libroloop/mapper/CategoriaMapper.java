package com.libroloop.mapper;

import com.libroloop.dto.CategoriaDTO;
import com.libroloop.dto.CategoriaRequestDTO;
import com.libroloop.entity.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    CategoriaDTO toDTO(Categoria categoria);

    Categoria toEntity(CategoriaDTO categoriaDTO);

    Categoria toEntity(CategoriaRequestDTO categoriaRequestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "libros", ignore = true)
    void updateEntityFromDTO(CategoriaDTO categoriaDTO, @MappingTarget Categoria categoria);

    List<CategoriaDTO> toDTOList(List<Categoria> categorias);
}
