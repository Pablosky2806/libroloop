package com.libroloop.service;

import com.libroloop.dto.EjemplarDTO;
import com.libroloop.dto.EjemplarRequestDTO;
import com.libroloop.entity.Ejemplar;
import com.libroloop.entity.Libro;
import com.libroloop.exception.ResourceNotFoundException;
import com.libroloop.mapper.EjemplarMapper;
import com.libroloop.repository.EjemplarRepository;
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
class EjemplarServiceTest {

    @Mock
    private EjemplarRepository ejemplarRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private EjemplarMapper ejemplarMapper;

    @InjectMocks
    private EjemplarService ejemplarService;

    private Ejemplar ejemplar;
    private Libro libro;
    private EjemplarRequestDTO ejemplarRequestDTO;
    private EjemplarDTO ejemplarDTO;

    @BeforeEach
    void setUp() {
        libro = new Libro();
        libro.setId(1L);
        libro.setTitulo("Cien años de soledad");

        ejemplar = new Ejemplar();
        ejemplar.setId(1L);
        ejemplar.setLibro(libro);
        ejemplar.setEstado(Ejemplar.Estado.DISPONIBLE);

        ejemplarRequestDTO = new EjemplarRequestDTO();
        ejemplarRequestDTO.setLibroId(1L);
        ejemplarRequestDTO.setEstado(Ejemplar.Estado.DISPONIBLE);

        ejemplarDTO = new EjemplarDTO();
        ejemplarDTO.setId(1L);
        ejemplarDTO.setLibroId(1L);
        ejemplarDTO.setEstado(Ejemplar.Estado.DISPONIBLE);
    }

    @Test
    void createEjemplar_ShouldReturnEjemplarDTO() {
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(ejemplarRepository.existsByCodigoInventario(anyString())).thenReturn(false);
        when(ejemplarMapper.toEntity(any(EjemplarRequestDTO.class))).thenReturn(ejemplar);
        when(ejemplarRepository.save(any(Ejemplar.class))).thenReturn(ejemplar);
        when(ejemplarMapper.toDTO(any(Ejemplar.class))).thenReturn(ejemplarDTO);

        EjemplarDTO result = ejemplarService.createEjemplar(ejemplarRequestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getEstado()).isEqualTo(Ejemplar.Estado.DISPONIBLE);
        verify(ejemplarRepository, times(1)).save(any(Ejemplar.class));
    }

    @Test
    void createEjemplar_WhenLibroNotFound_ShouldThrowException() {
        when(libroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ejemplarService.createEjemplar(ejemplarRequestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Libro no encontrado con id: 1");

        verify(ejemplarRepository, never()).save(any(Ejemplar.class));
    }

    @Test
    void getEjemplarById_ShouldReturnEjemplarDTO() {
        when(ejemplarRepository.findById(1L)).thenReturn(Optional.of(ejemplar));
        when(ejemplarMapper.toDTO(any(Ejemplar.class))).thenReturn(ejemplarDTO);

        EjemplarDTO result = ejemplarService.getEjemplarById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getEjemplarById_WhenNotFound_ShouldThrowException() {
        when(ejemplarRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ejemplarService.getEjemplarById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ejemplar no encontrado con id: 1");
    }

    @Test
    void getEjemplaresByLibroId_ShouldReturnListOfEjemplarDTO() {
        List<Ejemplar> ejemplares = Arrays.asList(ejemplar);
        when(ejemplarRepository.findByLibroId(1L)).thenReturn(ejemplares);
        when(ejemplarMapper.toDTOList(anyList())).thenReturn(Arrays.asList(ejemplarDTO));

        List<EjemplarDTO> result = ejemplarService.getEjemplaresByLibroId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLibroId()).isEqualTo(1L);
    }

    @Test
    void deleteEjemplar_WhenIsPrestado_ShouldThrowException() {
        ejemplar.setEstado(Ejemplar.Estado.PRESTADO);
        when(ejemplarRepository.findById(1L)).thenReturn(Optional.of(ejemplar));

        assertThatThrownBy(() -> ejemplarService.deleteEjemplar(1L))
                .isInstanceOf(com.libroloop.exception.BadRequestException.class)
                .hasMessageContaining("No se puede eliminar un ejemplar que está prestado");

        verify(ejemplarRepository, never()).delete(any(Ejemplar.class));
    }
}
