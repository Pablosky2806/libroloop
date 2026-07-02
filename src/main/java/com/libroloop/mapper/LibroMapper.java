package com.libroloop.mapper;

import com.libroloop.dto.LibroDTO;
import com.libroloop.dto.LibroRequestDTO;
import com.libroloop.entity.Libro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AutorMapper.class})
public interface LibroMapper {

    @Mapping(target = "autorId", source = "autor.id")
    @Mapping(target = "autorNombre", source = "autor.nombre")
    @Mapping(target = "categoriaIds", source = "categorias", qualifiedByName = "mapCategoriaIds")
    @Mapping(target = "categoriaNombres", source = "categorias", qualifiedByName = "mapCategoriaNombres")
    @Mapping(target = "ejemplarIds", source = "ejemplares", qualifiedByName = "mapEjemplarIds")
    LibroDTO toDTO(Libro libro);

    @Mapping(target = "autor", ignore = true)
    @Mapping(target = "categorias", ignore = true)
    @Mapping(target = "ejemplares", ignore = true)
    Libro toEntity(LibroDTO libroDTO);

    @Mapping(target = "autor", ignore = true)
    @Mapping(target = "categorias", ignore = true)
    @Mapping(target = "ejemplares", ignore = true)
    Libro toEntity(LibroRequestDTO libroRequestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "autor", ignore = true)
    @Mapping(target = "categorias", ignore = true)
    @Mapping(target = "ejemplares", ignore = true)
    void updateEntityFromDTO(LibroDTO libroDTO, @MappingTarget Libro libro);

    List<LibroDTO> toDTOList(List<Libro> libros);

    @Named("mapCategoriaIds")
    default List<Long> mapCategoriaIds(List<?> categorias) {
        if (categorias == null) return null;
        return categorias.stream()
                .map(obj -> ((com.libroloop.entity.Categoria) obj).getId())
                .toList();
    }

    @Named("mapCategoriaNombres")
    default List<String> mapCategoriaNombres(List<?> categorias) {
        if (categorias == null) return null;
        return categorias.stream()
                .map(obj -> ((com.libroloop.entity.Categoria) obj).getNombre())
                .toList();
    }

    @Named("mapEjemplarIds")
    default List<Long> mapEjemplarIds(List<?> ejemplares) {
        if (ejemplares == null) return null;
        return ejemplares.stream()
                .map(obj -> ((com.libroloop.entity.Ejemplar) obj).getId())
                .toList();
    }
}
