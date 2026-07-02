package com.libroloop.service;

import com.libroloop.dto.LibroDTO;
import com.libroloop.dto.LibroRequestDTO;
import com.libroloop.entity.Autor;
import com.libroloop.entity.Libro;
import com.libroloop.exception.ResourceNotFoundException;
import com.libroloop.mapper.LibroMapper;
import com.libroloop.repository.AutorRepository;
import com.libroloop.repository.CategoriaRepository;
import com.libroloop.repository.LibroRepository;
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
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private LibroMapper libroMapper;

    @InjectMocks
    private LibroService libroService;

    private Libro libro;
    private Autor autor;
    private LibroRequestDTO libroRequestDTO;
    private LibroDTO libroDTO;

    @BeforeEach
    void setUp() {
        autor = new Autor();
        autor.setId(1L);
        autor.setNombre("Gabriel García");

        libro = new Libro();
        libro.setId(1L);
        libro.setIsbn("978-3-16-148410-0");
        libro.setTitulo("Cien años de soledad");
        libro.setAutor(autor);

        libroRequestDTO = new LibroRequestDTO();
        libroRequestDTO.setIsbn("978-3-16-148410-0");
        libroRequestDTO.setTitulo("Cien años de soledad");
        libroRequestDTO.setAutorId(1L);

        libroDTO = new LibroDTO();
        libroDTO.setId(1L);
        libroDTO.setIsbn("978-3-16-148410-0");
        libroDTO.setTitulo("Cien años de soledad");
        libroDTO.setAutorId(1L);
    }

    @Test
    void createLibro_ShouldReturnLibroDTO() {
        when(libroRepository.existsByIsbn(anyString())).thenReturn(false);
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(libroMapper.toEntity(any(LibroRequestDTO.class))).thenReturn(libro);
        when(libroRepository.save(any(Libro.class))).thenReturn(libro);
        when(libroMapper.toDTO(any(Libro.class))).thenReturn(libroDTO);

        LibroDTO result = libroService.createLibro(libroRequestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getIsbn()).isEqualTo("978-3-16-148410-0");
        verify(libroRepository, times(1)).save(any(Libro.class));
    }

    @Test
    void createLibro_WhenIsbnExists_ShouldThrowException() {
        when(libroRepository.existsByIsbn(anyString())).thenReturn(true);

        assertThatThrownBy(() -> libroService.createLibro(libroRequestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un libro con el ISBN");

        verify(libroRepository, never()).save(any(Libro.class));
    }

    @Test
    void createLibro_WhenAutorNotFound_ShouldThrowException() {
        when(libroRepository.existsByIsbn(anyString())).thenReturn(false);
        when(autorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> libroService.createLibro(libroRequestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Autor no encontrado con id: 1");
    }

    @Test
    void getLibroById_ShouldReturnLibroDTO() {
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(libroMapper.toDTO(any(Libro.class))).thenReturn(libroDTO);

        LibroDTO result = libroService.getLibroById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getLibroById_WhenNotFound_ShouldThrowException() {
        when(libroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> libroService.getLibroById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Libro no encontrado con id: 1");
    }

    @Test
    void getAllLibros_ShouldReturnListOfLibroDTO() {
        List<Libro> libros = Arrays.asList(libro);
        when(libroRepository.findAll()).thenReturn(libros);
        when(libroMapper.toDTOList(anyList())).thenReturn(Arrays.asList(libroDTO));

        List<LibroDTO> result = libroService.getAllLibros();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIsbn()).isEqualTo("978-3-16-148410-0");
    }
}
