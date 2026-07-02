package com.libroloop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libroloop.dto.EjemplarDTO;
import com.libroloop.dto.EjemplarRequestDTO;
import com.libroloop.entity.Ejemplar;
import com.libroloop.service.EjemplarService;
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

@WebMvcTest(EjemplarController.class)
class EjemplarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EjemplarService ejemplarService;

    @Test
    void createEjemplar_ShouldReturnCreated() throws Exception {
        EjemplarRequestDTO requestDTO = new EjemplarRequestDTO();
        requestDTO.setLibroId(1L);
        requestDTO.setEstado(Ejemplar.Estado.DISPONIBLE);

        EjemplarDTO responseDTO = new EjemplarDTO();
        responseDTO.setId(1L);
        responseDTO.setLibroId(1L);
        responseDTO.setEstado(Ejemplar.Estado.DISPONIBLE);

        when(ejemplarService.createEjemplar(any(EjemplarRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/ejemplares")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("DISPONIBLE"));
    }

    @Test
    void getEjemplarById_ShouldReturnEjemplar() throws Exception {
        EjemplarDTO ejemplarDTO = new EjemplarDTO();
        ejemplarDTO.setId(1L);
        ejemplarDTO.setLibroId(1L);
        ejemplarDTO.setEstado(Ejemplar.Estado.DISPONIBLE);

        when(ejemplarService.getEjemplarById(1L)).thenReturn(ejemplarDTO);

        mockMvc.perform(get("/api/ejemplares/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("DISPONIBLE"));
    }

    @Test
    void getEjemplaresByLibroId_ShouldReturnList() throws Exception {
        EjemplarDTO ejemplarDTO = new EjemplarDTO();
        ejemplarDTO.setId(1L);
        ejemplarDTO.setLibroId(1L);

        List<EjemplarDTO> ejemplares = Arrays.asList(ejemplarDTO);
        when(ejemplarService.getEjemplaresByLibroId(1L)).thenReturn(ejemplares);

        mockMvc.perform(get("/api/ejemplares/libro/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].libroId").value(1));
    }

    @Test
    void getEjemplaresByEstado_ShouldReturnList() throws Exception {
        EjemplarDTO ejemplarDTO = new EjemplarDTO();
        ejemplarDTO.setId(1L);
        ejemplarDTO.setEstado(Ejemplar.Estado.DISPONIBLE);

        List<EjemplarDTO> ejemplares = Arrays.asList(ejemplarDTO);
        when(ejemplarService.getEjemplaresByEstado(Ejemplar.Estado.DISPONIBLE)).thenReturn(ejemplares);

        mockMvc.perform(get("/api/ejemplares/estado/DISPONIBLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].estado").value("DISPONIBLE"));
    }
}
