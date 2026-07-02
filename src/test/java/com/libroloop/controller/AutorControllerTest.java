package com.libroloop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libroloop.dto.AutorDTO;
import com.libroloop.dto.AutorRequestDTO;
import com.libroloop.service.AutorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AutorController.class)
class AutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AutorService autorService;

    @Test
    void createAutor_ShouldReturnCreated() throws Exception {
        AutorRequestDTO requestDTO = new AutorRequestDTO();
        requestDTO.setNombre("Gabriel García");
        requestDTO.setApellido("Márquez");

        AutorDTO responseDTO = new AutorDTO();
        responseDTO.setId(1L);
        responseDTO.setNombre("Gabriel García");
        responseDTO.setApellido("Márquez");

        when(autorService.createAutor(any(AutorRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/autores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Gabriel García"));
    }

    @Test
    void getAutorById_ShouldReturnAutor() throws Exception {
        AutorDTO autorDTO = new AutorDTO();
        autorDTO.setId(1L);
        autorDTO.setNombre("Gabriel García");

        when(autorService.getAutorById(1L)).thenReturn(autorDTO);

        mockMvc.perform(get("/api/autores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Gabriel García"));
    }

    @Test
    void getAllAutores_ShouldReturnList() throws Exception {
        AutorDTO autorDTO = new AutorDTO();
        autorDTO.setId(1L);
        autorDTO.setNombre("Gabriel García");

        List<AutorDTO> autores = Arrays.asList(autorDTO);
        when(autorService.getAllAutores()).thenReturn(autores);

        mockMvc.perform(get("/api/autores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Gabriel García"));
    }

    @Test
    void updateAutor_ShouldReturnUpdatedAutor() throws Exception {
        AutorDTO autorDTO = new AutorDTO();
        autorDTO.setId(1L);
        autorDTO.setNombre("Gabriel García Márquez");

        when(autorService.updateAutor(eq(1L), any(AutorDTO.class))).thenReturn(autorDTO);

        mockMvc.perform(put("/api/autores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(autorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Gabriel García Márquez"));
    }

    @Test
    void deleteAutor_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/autores/1"))
                .andExpect(status().isNoContent());
    }
}
