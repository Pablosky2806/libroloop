package com.libroloop.service;

import com.libroloop.dto.AutorDTO;
import com.libroloop.dto.AutorRequestDTO;
import com.libroloop.entity.Autor;
import com.libroloop.exception.ResourceNotFoundException;
import com.libroloop.mapper.AutorMapper;
import com.libroloop.repository.AutorRepository;
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
class AutorServiceTest {

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private AutorMapper autorMapper;

    @InjectMocks
    private AutorService autorService;

    private Autor autor;
    private AutorRequestDTO autorRequestDTO;
    private AutorDTO autorDTO;

    @BeforeEach
    void setUp() {
        autor = new Autor();
        autor.setId(1L);
        autor.setNombre("Gabriel García");
        autor.setApellido("Márquez");

        autorRequestDTO = new AutorRequestDTO();
        autorRequestDTO.setNombre("Gabriel García");
        autorRequestDTO.setApellido("Márquez");

        autorDTO = new AutorDTO();
        autorDTO.setId(1L);
        autorDTO.setNombre("Gabriel García");
        autorDTO.setApellido("Márquez");
    }

    @Test
    void createAutor_ShouldReturnAutorDTO() {
        when(autorRepository.existsByNombre(anyString())).thenReturn(false);
        when(autorMapper.toEntity(any(AutorRequestDTO.class))).thenReturn(autor);
        when(autorRepository.save(any(Autor.class))).thenReturn(autor);
        when(autorMapper.toDTO(any(Autor.class))).thenReturn(autorDTO);

        AutorDTO result = autorService.createAutor(autorRequestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Gabriel García");
        verify(autorRepository, times(1)).save(any(Autor.class));
    }

    @Test
    void createAutor_WhenNombreExists_ShouldThrowException() {
        when(autorRepository.existsByNombre(anyString())).thenReturn(true);

        assertThatThrownBy(() -> autorService.createAutor(autorRequestDTO))
                .isInstanceOf(com.libroloop.exception.BadRequestException.class)
                .hasMessageContaining("Ya existe un autor con el nombre");

        verify(autorRepository, never()).save(any(Autor.class));
    }

    @Test
    void getAutorById_ShouldReturnAutorDTO() {
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(autorMapper.toDTO(any(Autor.class))).thenReturn(autorDTO);

        AutorDTO result = autorService.getAutorById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(autorRepository, times(1)).findById(1L);
    }

    @Test
    void getAutorById_WhenNotFound_ShouldThrowException() {
        when(autorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> autorService.getAutorById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Autor no encontrado con id: 1");
    }

    @Test
    void getAllAutores_ShouldReturnListOfAutorDTO() {
        List<Autor> autores = Arrays.asList(autor);
        when(autorRepository.findAll()).thenReturn(autores);
        when(autorMapper.toDTOList(anyList())).thenReturn(Arrays.asList(autorDTO));

        List<AutorDTO> result = autorService.getAllAutores();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Gabriel García");
    }

    @Test
    void deleteAutor_WhenHasNoBooks_ShouldDelete() {
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(autor.getLibros()).thenReturn(List.of());

        autorService.deleteAutor(1L);

        verify(autorRepository, times(1)).delete(autor);
    }

    @Test
    void deleteAutor_WhenHasBooks_ShouldThrowException() {
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(autor.getLibros()).thenReturn(Arrays.asList(new com.libroloop.entity.Libro()));

        assertThatThrownBy(() -> autorService.deleteAutor(1L))
                .isInstanceOf(com.libroloop.exception.BadRequestException.class)
                .hasMessageContaining("No se puede eliminar el autor porque tiene libros asociados");

        verify(autorRepository, never()).delete(any(Autor.class));
    }
}
