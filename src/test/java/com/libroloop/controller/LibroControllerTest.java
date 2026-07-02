package com.libroloop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libroloop.dto.LibroDTO;
import com.libroloop.dto.LibroRequestDTO;
import com.libroloop.service.LibroService;
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

@WebMvcTest(LibroController.class)
class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LibroService libroService;

    @Test
    void createLibro_ShouldReturnCreated() throws Exception {
        LibroRequestDTO requestDTO = new LibroRequestDTO();
        requestDTO.setIsbn("978-3-16-148410-0");
        requestDTO.setTitulo("Cien años de soledad");
        requestDTO.setAutorId(1L);

        LibroDTO responseDTO = new LibroDTO();
        responseDTO.setId(1L);
        responseDTO.setIsbn("978-3-16-148410-0");
        responseDTO.setTitulo("Cien años de soledad");
        responseDTO.setAutorId(1L);

        when(libroService.createLibro(any(LibroRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value("978-3-16-148410-0"));
    }

    @Test
    void getLibroById_ShouldReturnLibro() throws Exception {
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setId(1L);
        libroDTO.setIsbn("978-3-16-148410-0");
        libroDTO.setTitulo("Cien años de soledad");

        when(libroService.getLibroById(1L)).thenReturn(libroDTO);

        mockMvc.perform(get("/api/libros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value("978-3-16-148410-0"));
    }

    @Test
    void getLibroByIsbn_ShouldReturnLibro() throws Exception {
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setId(1L);
        libroDTO.setIsbn("978-3-16-148410-0");
        libroDTO.setTitulo("Cien años de soledad");

        when(libroService.getLibroByIsbn("978-3-16-148410-0")).thenReturn(libroDTO);

        mockMvc.perform(get("/api/libros/isbn/978-3-16-148410-0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("978-3-16-148410-0"));
    }

    @Test
    void getAllLibros_ShouldReturnList() throws Exception {
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setId(1L);
        libroDTO.setIsbn("978-3-16-148410-0");

        List<LibroDTO> libros = Arrays.asList(libroDTO);
        when(libroService.getAllLibros()).thenReturn(libros);

        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].isbn").value("978-3-16-148410-0"));
    }
}
