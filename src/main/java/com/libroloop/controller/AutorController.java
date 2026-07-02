package com.libroloop.controller;

import com.libroloop.dto.AutorDTO;
import com.libroloop.dto.AutorRequestDTO;
import com.libroloop.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService autorService;

    @PostMapping
    public ResponseEntity<AutorDTO> createAutor(@Valid @RequestBody AutorRequestDTO autorRequestDTO) {
        AutorDTO createdAutor = autorService.createAutor(autorRequestDTO);
        return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorDTO> getAutorById(@PathVariable Long id) {
        AutorDTO autor = autorService.getAutorById(id);
        return ResponseEntity.ok(autor);
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> getAllAutores() {
        List<AutorDTO> autores = autorService.getAllAutores();
        return ResponseEntity.ok(autores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorDTO> updateAutor(@PathVariable Long id, @Valid @RequestBody AutorDTO autorDTO) {
        AutorDTO updatedAutor = autorService.updateAutor(id, autorDTO);
        return ResponseEntity.ok(updatedAutor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutor(@PathVariable Long id) {
        autorService.deleteAutor(id);
        return ResponseEntity.noContent().build();
    }
}
