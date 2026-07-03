package com.libroloop.controller;

import com.libroloop.dto.SocioDTO;
import com.libroloop.dto.SocioRequestDTO;
import com.libroloop.service.SocioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socios")
@RequiredArgsConstructor
public class SocioController {

    private final SocioService socioService;

    @PostMapping
    public ResponseEntity<SocioDTO> createSocio(@Valid @RequestBody SocioRequestDTO socioRequestDTO) {
        SocioDTO createdSocio = socioService.createSocio(socioRequestDTO);
        return new ResponseEntity<>(createdSocio, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocioDTO> getSocioById(@PathVariable Long id) {
        SocioDTO socio = socioService.getSocioById(id);
        return ResponseEntity.ok(socio);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<SocioDTO> getSocioByEmail(@PathVariable String email) {
        SocioDTO socio = socioService.getSocioByEmail(email);
        return ResponseEntity.ok(socio);
    }

    @GetMapping
    public ResponseEntity<List<SocioDTO>> getAllSocios() {
        List<SocioDTO> socios = socioService.getAllSocios();
        return ResponseEntity.ok(socios);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<SocioDTO>> getSociosActivos() {
        List<SocioDTO> socios = socioService.getSociosActivos();
        return ResponseEntity.ok(socios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SocioDTO> updateSocio(@PathVariable Long id, @Valid @RequestBody SocioDTO socioDTO) {
        SocioDTO updatedSocio = socioService.updateSocio(id, socioDTO);
        return ResponseEntity.ok(updatedSocio);
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<SocioDTO> desactivarSocio(@PathVariable Long id) {
        SocioDTO socio = socioService.desactivarSocio(id);
        return ResponseEntity.ok(socio);
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<SocioDTO> activarSocio(@PathVariable Long id) {
        SocioDTO socio = socioService.activarSocio(id);
        return ResponseEntity.ok(socio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSocio(@PathVariable Long id) {
        socioService.deleteSocio(id);
        return ResponseEntity.noContent().build();
    }
}
