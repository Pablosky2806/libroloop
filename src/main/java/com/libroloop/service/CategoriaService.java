package com.libroloop.service;

import com.libroloop.dto.CategoriaDTO;
import com.libroloop.dto.CategoriaRequestDTO;
import com.libroloop.entity.Categoria;
import com.libroloop.exception.BadRequestException;
import com.libroloop.exception.ResourceNotFoundException;
import com.libroloop.mapper.CategoriaMapper;
import com.libroloop.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public CategoriaDTO createCategoria(CategoriaRequestDTO categoriaRequestDTO) {
        if (categoriaRepository.existsByNombre(categoriaRequestDTO.getNombre())) {
            throw new BadRequestException("Ya existe una categoría con el nombre: " + categoriaRequestDTO.getNombre());
        }

        Categoria categoria = categoriaMapper.toEntity(categoriaRequestDTO);
        Categoria savedCategoria = categoriaRepository.save(categoria);
        return categoriaMapper.toDTO(savedCategoria);
    }

    public CategoriaDTO getCategoriaById(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
        return categoriaMapper.toDTO(categoria);
    }

    public List<CategoriaDTO> getAllCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categoriaMapper.toDTOList(categorias);
    }

    public CategoriaDTO updateCategoria(Long id, CategoriaDTO categoriaDTO) {
        Categoria existingCategoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));

        if (!existingCategoria.getNombre().equals(categoriaDTO.getNombre()) 
                && categoriaRepository.existsByNombre(categoriaDTO.getNombre())) {
            throw new BadRequestException("Ya existe una categoría con el nombre: " + categoriaDTO.getNombre());
        }

        categoriaMapper.updateEntityFromDTO(categoriaDTO, existingCategoria);
        Categoria updatedCategoria = categoriaRepository.save(existingCategoria);
        return categoriaMapper.toDTO(updatedCategoria);
    }

    public void deleteCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));

        if (!categoria.getLibros().isEmpty()) {
            throw new BadRequestException("No se puede eliminar la categoría porque tiene libros asociados");
        }

        categoriaRepository.delete(categoria);
    }
}
