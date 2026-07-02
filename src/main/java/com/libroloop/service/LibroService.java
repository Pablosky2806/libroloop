package com.libroloop.service;

import com.libroloop.dto.LibroDTO;
import com.libroloop.dto.LibroRequestDTO;
import com.libroloop.entity.Autor;
import com.libroloop.entity.Categoria;
import com.libroloop.entity.Libro;
import com.libroloop.exception.BadRequestException;
import com.libroloop.exception.ResourceNotFoundException;
import com.libroloop.mapper.LibroMapper;
import com.libroloop.repository.AutorRepository;
import com.libroloop.repository.CategoriaRepository;
import com.libroloop.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class LibroService {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final LibroMapper libroMapper;

    public LibroDTO createLibro(LibroRequestDTO libroRequestDTO) {
        if (libroRepository.existsByIsbn(libroRequestDTO.getIsbn())) {
            throw new BadRequestException("Ya existe un libro con el ISBN: " + libroRequestDTO.getIsbn());
        }

        Autor autor = autorRepository.findById(libroRequestDTO.getAutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Autor no encontrado con id: " + libroRequestDTO.getAutorId()));

        Libro libro = libroMapper.toEntity(libroRequestDTO);
        libro.setAutor(autor);

        if (libroRequestDTO.getCategoriaIds() != null && !libroRequestDTO.getCategoriaIds().isEmpty()) {
            Set<Categoria> categorias = new HashSet<>(categoriaRepository.findAllById(libroRequestDTO.getCategoriaIds()));
            if (categorias.size() != libroRequestDTO.getCategoriaIds().size()) {
                throw new BadRequestException("Una o más categorías no fueron encontradas");
            }
            libro.setCategorias(new ArrayList<>(categorias));
        }

        Libro savedLibro = libroRepository.save(libro);
        return libroMapper.toDTO(savedLibro);
    }

    public LibroDTO getLibroById(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con id: " + id));
        return libroMapper.toDTO(libro);
    }

    public LibroDTO getLibroByIsbn(String isbn) {
        Libro libro = libroRepository.findByIsbn(isbn)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ISBN: " + isbn));
        return libroMapper.toDTO(libro);
    }

    public List<LibroDTO> getAllLibros() {
        List<Libro> libros = libroRepository.findAll();
        return libroMapper.toDTOList(libros);
    }

    public LibroDTO updateLibro(Long id, LibroDTO libroDTO) {
        Libro existingLibro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con id: " + id));

        if (!existingLibro.getIsbn().equals(libroDTO.getIsbn()) 
                && libroRepository.existsByIsbn(libroDTO.getIsbn())) {
            throw new BadRequestException("Ya existe un libro con el ISBN: " + libroDTO.getIsbn());
        }

        if (libroDTO.getAutorId() != null) {
            Autor autor = autorRepository.findById(libroDTO.getAutorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Autor no encontrado con id: " + libroDTO.getAutorId()));
            existingLibro.setAutor(autor);
        }

        if (libroDTO.getCategoriaIds() != null) {
            Set<Categoria> categorias = new HashSet<>(categoriaRepository.findAllById(libroDTO.getCategoriaIds()));
            existingLibro.setCategorias(new ArrayList<>(categorias));
        }

        libroMapper.updateEntityFromDTO(libroDTO, existingLibro);
        Libro updatedLibro = libroRepository.save(existingLibro);
        return libroMapper.toDTO(updatedLibro);
    }

    public void deleteLibro(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con id: " + id));

        if (!libro.getEjemplares().isEmpty()) {
            throw new BadRequestException("No se puede eliminar el libro porque tiene ejemplares asociados");
        }

        libroRepository.delete(libro);
    }
}
