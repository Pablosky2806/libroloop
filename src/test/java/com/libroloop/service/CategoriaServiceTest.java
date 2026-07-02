package com.libroloop.service;

import com.libroloop.dto.CategoriaDTO;
import com.libroloop.dto.CategoriaRequestDTO;
import com.libroloop.entity.Categoria;
import com.libroloop.exception.ResourceNotFoundException;
import com.libroloop.mapper.CategoriaMapper;
import com.libroloop.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriaMapper categoriaMapper;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;
    private CategoriaRequestDTO categoriaRequestDTO;
    private CategoriaDTO categoriaDTO;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Ficción");
        categoria.setDescripcion("Libros de ficción");

        categoriaRequestDTO = new CategoriaRequestDTO();
        categoriaRequestDTO.setNombre("Ficción");
        categoriaRequestDTO.setDescripcion("Libros de ficción");

        categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(1L);
        categoriaDTO.setNombre("Ficción");
        categoriaDTO.setDescripcion("Libros de ficción");
    }

    @Test
    void createCategoria_ShouldReturnCategoriaDTO() {
        when(categoriaRepository.existsByNombre(anyString())).thenReturn(false);
        when(categoriaMapper.toEntity(any(CategoriaRequestDTO.class))).thenReturn(categoria);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);
        when(categoriaMapper.toDTO(any(Categoria.class))).thenReturn(categoriaDTO);

        CategoriaDTO result = categoriaService.createCategoria(categoriaRequestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Ficción");
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void createCategoria_WhenNombreExists_ShouldThrowException() {
        when(categoriaRepository.existsByNombre(anyString())).thenReturn(true);

        assertThatThrownBy(() -> categoriaService.createCategoria(categoriaRequestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe una categoría con el nombre");

        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void getCategoriaById_ShouldReturnCategoriaDTO() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(categoriaMapper.toDTO(any(Categoria.class))).thenReturn(categoriaDTO);

        CategoriaDTO result = categoriaService.getCategoriaById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getCategoriaById_WhenNotFound_ShouldThrowException() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoriaService.getCategoriaById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Categoría no encontrada con id: 1");
    }

    @Test
    void getAllCategorias_ShouldReturnListOfCategoriaDTO() {
        List<Categoria> categorias = Arrays.asList(categoria);
        when(categoriaRepository.findAll()).thenReturn(categorias);
        when(categoriaMapper.toDTOList(anyList())).thenReturn(Arrays.asList(categoriaDTO));

        List<CategoriaDTO> result = categoriaService.getAllCategorias();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Ficción");
    }
}
