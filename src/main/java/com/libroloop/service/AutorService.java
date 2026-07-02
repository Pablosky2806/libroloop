package com.libroloop.service;

import com.libroloop.dto.AutorDTO;
import com.libroloop.dto.AutorRequestDTO;
import com.libroloop.entity.Autor;
import com.libroloop.exception.ResourceNotFoundException;
import com.libroloop.mapper.AutorMapper;
import com.libroloop.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AutorService {

    private final AutorRepository autorRepository;
    private final AutorMapper autorMapper;

    public AutorDTO createAutor(AutorRequestDTO autorRequestDTO) {
        if (autorRepository.existsByNombre(autorRequestDTO.getNombre())) {
            throw new IllegalArgumentException("Ya existe un autor con el nombre: " + autorRequestDTO.getNombre());
        }

        Autor autor = autorMapper.toEntity(autorRequestDTO);
        Autor savedAutor = autorRepository.save(autor);
        return autorMapper.toDTO(savedAutor);
    }

    public AutorDTO getAutorById(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor no encontrado con id: " + id));
        return autorMapper.toDTO(autor);
    }

    public List<AutorDTO> getAllAutores() {
        List<Autor> autores = autorRepository.findAll();
        return autorMapper.toDTOList(autores);
    }

    public AutorDTO updateAutor(Long id, AutorDTO autorDTO) {
        Autor existingAutor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor no encontrado con id: " + id));

        if (!existingAutor.getNombre().equals(autorDTO.getNombre()) 
                && autorRepository.existsByNombre(autorDTO.getNombre())) {
            throw new IllegalArgumentException("Ya existe un autor con el nombre: " + autorDTO.getNombre());
        }

        autorMapper.updateEntityFromDTO(autorDTO, existingAutor);
        Autor updatedAutor = autorRepository.save(existingAutor);
        return autorMapper.toDTO(updatedAutor);
    }

    public void deleteAutor(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor no encontrado con id: " + id));

        if (!autor.getLibros().isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar el autor porque tiene libros asociados");
        }

        autorRepository.delete(autor);
    }
}
