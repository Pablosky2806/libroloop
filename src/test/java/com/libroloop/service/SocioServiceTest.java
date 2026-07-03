package com.libroloop.service;

import com.libroloop.dto.SocioDTO;
import com.libroloop.dto.SocioRequestDTO;
import com.libroloop.entity.Socio;
import com.libroloop.exception.ResourceNotFoundException;
import com.libroloop.mapper.SocioMapper;
import com.libroloop.repository.SocioRepository;
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
class SocioServiceTest {

    @Mock
    private SocioRepository socioRepository;

    @Mock
    private SocioMapper socioMapper;

    @InjectMocks
    private SocioService socioService;

    private Socio socio;
    private SocioRequestDTO socioRequestDTO;
    private SocioDTO socioDTO;

    @BeforeEach
    void setUp() {
        socio = new Socio();
        socio.setId(1L);
        socio.setNombre("Juan");
        socio.setApellido("Pérez");
        socio.setEmail("juan@example.com");
        socio.setTelefono("123456789");
        socio.setActivo(true);
        socio.setRol(Socio.Rol.SOCIO);

        socioRequestDTO = new SocioRequestDTO();
        socioRequestDTO.setNombre("Juan");
        socioRequestDTO.setApellido("Pérez");
        socioRequestDTO.setEmail("juan@example.com");
        socioRequestDTO.setTelefono("123456789");

        socioDTO = new SocioDTO();
        socioDTO.setId(1L);
        socioDTO.setNombre("Juan");
        socioDTO.setApellido("Pérez");
        socioDTO.setEmail("juan@example.com");
        socioDTO.setTelefono("123456789");
        socioDTO.setActivo(true);
        socioDTO.setRol(Socio.Rol.SOCIO);
    }

    @Test
    void createSocio_ShouldReturnSocioDTO() {
        when(socioRepository.existsByEmail(anyString())).thenReturn(false);
        when(socioMapper.toEntity(any(SocioRequestDTO.class))).thenReturn(socio);
        when(socioRepository.save(any(Socio.class))).thenReturn(socio);
        when(socioMapper.toDTO(any(Socio.class))).thenReturn(socioDTO);

        SocioDTO result = socioService.createSocio(socioRequestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("juan@example.com");
        verify(socioRepository, times(1)).save(any(Socio.class));
    }

    @Test
    void createSocio_WhenEmailExists_ShouldThrowException() {
        when(socioRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> socioService.createSocio(socioRequestDTO))
                .isInstanceOf(com.libroloop.exception.BadRequestException.class)
                .hasMessageContaining("Ya existe un socio con el email");

        verify(socioRepository, never()).save(any(Socio.class));
    }

    @Test
    void getSocioById_ShouldReturnSocioDTO() {
        when(socioRepository.findById(1L)).thenReturn(Optional.of(socio));
        when(socioMapper.toDTO(any(Socio.class))).thenReturn(socioDTO);

        SocioDTO result = socioService.getSocioById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getSocioById_WhenNotFound_ShouldThrowException() {
        when(socioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> socioService.getSocioById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Socio no encontrado con id: 1");
    }

    @Test
    void getAllSocios_ShouldReturnListOfSocioDTO() {
        List<Socio> socios = Arrays.asList(socio);
        when(socioRepository.findAll()).thenReturn(socios);
        when(socioMapper.toDTOList(anyList())).thenReturn(Arrays.asList(socioDTO));

        List<SocioDTO> result = socioService.getAllSocios();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("juan@example.com");
    }

    @Test
    void desactivarSocio_ShouldReturnInactiveSocio() {
        when(socioRepository.findById(1L)).thenReturn(Optional.of(socio));
        when(socioRepository.save(any(Socio.class))).thenReturn(socio);
        when(socioMapper.toDTO(any(Socio.class))).thenReturn(socioDTO);

        SocioDTO result = socioService.desactivarSocio(1L);

        assertThat(result).isNotNull();
        verify(socioRepository, times(1)).save(any(Socio.class));
    }

    @Test
    void deleteSocio_WhenHasNoPrestamos_ShouldDelete() {
        when(socioRepository.findById(1L)).thenReturn(Optional.of(socio));
        when(socio.getPrestamos()).thenReturn(List.of());

        socioService.deleteSocio(1L);

        verify(socioRepository, times(1)).delete(socio);
    }

    @Test
    void deleteSocio_WhenHasPrestamos_ShouldThrowException() {
        when(socioRepository.findById(1L)).thenReturn(Optional.of(socio));
        when(socio.getPrestamos()).thenReturn(Arrays.asList(new com.libroloop.entity.Prestamo()));

        assertThatThrownBy(() -> socioService.deleteSocio(1L))
                .isInstanceOf(com.libroloop.exception.BadRequestException.class)
                .hasMessageContaining("No se puede eliminar el socio porque tiene préstamos activos");

        verify(socioRepository, never()).delete(any(Socio.class));
    }
}
