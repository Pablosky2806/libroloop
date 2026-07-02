package com.libroloop.service;

import com.libroloop.dto.EjemplarDTO;
import com.libroloop.dto.EjemplarRequestDTO;
import com.libroloop.entity.Ejemplar;
import com.libroloop.entity.Libro;
import com.libroloop.exception.BadRequestException;
import com.libroloop.exception.ResourceNotFoundException;
import com.libroloop.mapper.EjemplarMapper;
import com.libroloop.repository.EjemplarRepository;
import com.libroloop.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EjemplarService {

    private final EjemplarRepository ejemplarRepository;
    private final LibroRepository libroRepository;
    private final EjemplarMapper ejemplarMapper;

    public EjemplarDTO createEjemplar(EjemplarRequestDTO ejemplarRequestDTO) {
        Libro libro = libroRepository.findById(ejemplarRequestDTO.getLibroId())
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con id: " + ejemplarRequestDTO.getLibroId()));

        if (ejemplarRequestDTO.getCodigoInventario() != null 
                && ejemplarRepository.existsByCodigoInventario(ejemplarRequestDTO.getCodigoInventario())) {
            throw new BadRequestException("Ya existe un ejemplar con el código de inventario: " + ejemplarRequestDTO.getCodigoInventario());
        }

        Ejemplar ejemplar = ejemplarMapper.toEntity(ejemplarRequestDTO);
        ejemplar.setLibro(libro);

        if (ejemplarRequestDTO.getEstado() == null) {
            ejemplar.setEstado(Ejemplar.Estado.DISPONIBLE);
        }

        Ejemplar savedEjemplar = ejemplarRepository.save(ejemplar);
        return ejemplarMapper.toDTO(savedEjemplar);
    }

    public EjemplarDTO getEjemplarById(Long id) {
        Ejemplar ejemplar = ejemplarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejemplar no encontrado con id: " + id));
        return ejemplarMapper.toDTO(ejemplar);
    }

    public List<EjemplarDTO> getEjemplaresByLibroId(Long libroId) {
        List<Ejemplar> ejemplares = ejemplarRepository.findByLibroId(libroId);
        return ejemplarMapper.toDTOList(ejemplares);
    }

    public List<EjemplarDTO> getEjemplaresByEstado(Ejemplar.Estado estado) {
        List<Ejemplar> ejemplares = ejemplarRepository.findByEstado(estado);
        return ejemplarMapper.toDTOList(ejemplares);
    }

    public List<EjemplarDTO> getEjemplaresDisponibles(Long libroId) {
        List<Ejemplar> ejemplares = ejemplarRepository.findByLibroIdAndEstado(libroId, Ejemplar.Estado.DISPONIBLE);
        return ejemplarMapper.toDTOList(ejemplares);
    }

    public List<EjemplarDTO> getAllEjemplares() {
        List<Ejemplar> ejemplares = ejemplarRepository.findAll();
        return ejemplarMapper.toDTOList(ejemplares);
    }

    public EjemplarDTO updateEjemplar(Long id, EjemplarDTO ejemplarDTO) {
        Ejemplar existingEjemplar = ejemplarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejemplar no encontrado con id: " + id));

        if (ejemplarDTO.getCodigoInventario() != null 
                && !existingEjemplar.getCodigoInventario().equals(ejemplarDTO.getCodigoInventario())
                && ejemplarRepository.existsByCodigoInventario(ejemplarDTO.getCodigoInventario())) {
            throw new BadRequestException("Ya existe un ejemplar con el código de inventario: " + ejemplarDTO.getCodigoInventario());
        }

        if (ejemplarDTO.getLibroId() != null) {
            Libro libro = libroRepository.findById(ejemplarDTO.getLibroId())
                    .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con id: " + ejemplarDTO.getLibroId()));
            existingEjemplar.setLibro(libro);
        }

        ejemplarMapper.updateEntityFromDTO(ejemplarDTO, existingEjemplar);
        Ejemplar updatedEjemplar = ejemplarRepository.save(existingEjemplar);
        return ejemplarMapper.toDTO(updatedEjemplar);
    }

    public void deleteEjemplar(Long id) {
        Ejemplar ejemplar = ejemplarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejemplar no encontrado con id: " + id));

        if (ejemplar.getEstado() == Ejemplar.Estado.PRESTADO) {
            throw new BadRequestException("No se puede eliminar un ejemplar que está prestado");
        }

        ejemplarRepository.delete(ejemplar);
    }
}
