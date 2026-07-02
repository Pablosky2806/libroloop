package com.libroloop.controller;

import com.libroloop.dto.LibroDTO;
import com.libroloop.dto.LibroRequestDTO;
import com.libroloop.service.LibroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;

    @PostMapping
    public ResponseEntity<LibroDTO> createLibro(@Valid @RequestBody LibroRequestDTO libroRequestDTO) {
        LibroDTO createdLibro = libroService.createLibro(libroRequestDTO);
        return new ResponseEntity<>(createdLibro, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroDTO> getLibroById(@PathVariable Long id) {
        LibroDTO libro = libroService.getLibroById(id);
        return ResponseEntity.ok(libro);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<LibroDTO> getLibroByIsbn(@PathVariable String isbn) {
        LibroDTO libro = libroService.getLibroByIsbn(isbn);
        return ResponseEntity.ok(libro);
    }

    @GetMapping
    public ResponseEntity<List<LibroDTO>> getAllLibros() {
        List<LibroDTO> libros = libroService.getAllLibros();
        return ResponseEntity.ok(libros);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroDTO> updateLibro(@PathVariable Long id, @Valid @RequestBody LibroDTO libroDTO) {
        LibroDTO updatedLibro = libroService.updateLibro(id, libroDTO);
        return ResponseEntity.ok(updatedLibro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibro(@PathVariable Long id) {
        libroService.deleteLibro(id);
        return ResponseEntity.noContent().build();
    }
}
