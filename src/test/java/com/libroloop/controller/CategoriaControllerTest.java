package com.libroloop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libroloop.dto.CategoriaDTO;
import com.libroloop.dto.CategoriaRequestDTO;
import com.libroloop.service.CategoriaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoriaService categoriaService;

    @Test
    void createCategoria_ShouldReturnCreated() throws Exception {
        CategoriaRequestDTO requestDTO = new CategoriaRequestDTO();
        requestDTO.setNombre("Ficción");
        requestDTO.setDescripcion("Libros de ficción");

        CategoriaDTO responseDTO = new CategoriaDTO();
        responseDTO.setId(1L);
        responseDTO.setNombre("Ficción");
        responseDTO.setDescripcion("Libros de ficción");

        when(categoriaService.createCategoria(any(CategoriaRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Ficción"));
    }

    @Test
    void getCategoriaById_ShouldReturnCategoria() throws Exception {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(1L);
        categoriaDTO.setNombre("Ficción");

        when(categoriaService.getCategoriaById(1L)).thenReturn(categoriaDTO);

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Ficción"));
    }

    @Test
    void getAllCategorias_ShouldReturnList() throws Exception {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(1L);
        categoriaDTO.setNombre("Ficción");

        List<CategoriaDTO> categorias = Arrays.asList(categoriaDTO);
        when(categoriaService.getAllCategorias()).thenReturn(categorias);

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Ficción"));
    }
}
