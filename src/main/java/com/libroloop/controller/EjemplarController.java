package com.libroloop.controller;

import com.libroloop.dto.EjemplarDTO;
import com.libroloop.dto.EjemplarRequestDTO;
import com.libroloop.entity.Ejemplar;
import com.libroloop.service.EjemplarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ejemplares")
@RequiredArgsConstructor
public class EjemplarController {

    private final EjemplarService ejemplarService;

    @PostMapping
    public ResponseEntity<EjemplarDTO> createEjemplar(@Valid @RequestBody EjemplarRequestDTO ejemplarRequestDTO) {
        EjemplarDTO createdEjemplar = ejemplarService.createEjemplar(ejemplarRequestDTO);
        return new ResponseEntity<>(createdEjemplar, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EjemplarDTO> getEjemplarById(@PathVariable Long id) {
        EjemplarDTO ejemplar = ejemplarService.getEjemplarById(id);
        return ResponseEntity.ok(ejemplar);
    }

    @GetMapping
    public ResponseEntity<List<EjemplarDTO>> getAllEjemplares() {
        List<EjemplarDTO> ejemplares = ejemplarService.getAllEjemplares();
        return ResponseEntity.ok(ejemplares);
    }

    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<EjemplarDTO>> getEjemplaresByLibroId(@PathVariable Long libroId) {
        List<EjemplarDTO> ejemplares = ejemplarService.getEjemplaresByLibroId(libroId);
        return ResponseEntity.ok(ejemplares);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<EjemplarDTO>> getEjemplaresByEstado(@PathVariable Ejemplar.Estado estado) {
        List<EjemplarDTO> ejemplares = ejemplarService.getEjemplaresByEstado(estado);
        return ResponseEntity.ok(ejemplares);
    }

    @GetMapping("/libro/{libroId}/disponibles")
    public ResponseEntity<List<EjemplarDTO>> getEjemplaresDisponibles(@PathVariable Long libroId) {
        List<EjemplarDTO> ejemplares = ejemplarService.getEjemplaresDisponibles(libroId);
        return ResponseEntity.ok(ejemplares);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EjemplarDTO> updateEjemplar(@PathVariable Long id, @Valid @RequestBody EjemplarDTO ejemplarDTO) {
        EjemplarDTO updatedEjemplar = ejemplarService.updateEjemplar(id, ejemplarDTO);
        return ResponseEntity.ok(updatedEjemplar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEjemplar(@PathVariable Long id) {
        ejemplarService.deleteEjemplar(id);
        return ResponseEntity.noContent().build();
    }
}
