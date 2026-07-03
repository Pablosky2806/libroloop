package com.libroloop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libroloop.dto.SocioDTO;
import com.libroloop.dto.SocioRequestDTO;
import com.libroloop.entity.Socio;
import com.libroloop.service.SocioService;
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

@WebMvcTest(SocioController.class)
class SocioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SocioService socioService;

    @Test
    void createSocio_ShouldReturnCreated() throws Exception {
        SocioRequestDTO requestDTO = new SocioRequestDTO();
        requestDTO.setNombre("Juan");
        requestDTO.setApellido("Pérez");
        requestDTO.setEmail("juan@example.com");
        requestDTO.setTelefono("123456789");

        SocioDTO responseDTO = new SocioDTO();
        responseDTO.setId(1L);
        responseDTO.setNombre("Juan");
        responseDTO.setApellido("Pérez");
        responseDTO.setEmail("juan@example.com");

        when(socioService.createSocio(any(SocioRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/socios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void getSocioById_ShouldReturnSocio() throws Exception {
        SocioDTO socioDTO = new SocioDTO();
        socioDTO.setId(1L);
        socioDTO.setEmail("juan@example.com");

        when(socioService.getSocioById(1L)).thenReturn(socioDTO);

        mockMvc.perform(get("/api/socios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void getSocioByEmail_ShouldReturnSocio() throws Exception {
        SocioDTO socioDTO = new SocioDTO();
        socioDTO.setId(1L);
        socioDTO.setEmail("juan@example.com");

        when(socioService.getSocioByEmail("juan@example.com")).thenReturn(socioDTO);

        mockMvc.perform(get("/api/socios/email/juan@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void getAllSocios_ShouldReturnList() throws Exception {
        SocioDTO socioDTO = new SocioDTO();
        socioDTO.setId(1L);
        socioDTO.setEmail("juan@example.com");

        List<SocioDTO> socios = Arrays.asList(socioDTO);
        when(socioService.getAllSocios()).thenReturn(socios);

        mockMvc.perform(get("/api/socios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].email").value("juan@example.com"));
    }

    @Test
    void getSociosActivos_ShouldReturnList() throws Exception {
        SocioDTO socioDTO = new SocioDTO();
        socioDTO.setId(1L);
        socioDTO.setActivo(true);

        List<SocioDTO> socios = Arrays.asList(socioDTO);
        when(socioService.getSociosActivos()).thenReturn(socios);

        mockMvc.perform(get("/api/socios/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].activo").value(true));
    }

    @Test
    void desactivarSocio_ShouldReturnSocio() throws Exception {
        SocioDTO socioDTO = new SocioDTO();
        socioDTO.setId(1L);
        socioDTO.setActivo(false);

        when(socioService.desactivarSocio(1L)).thenReturn(socioDTO);

        mockMvc.perform(patch("/api/socios/1/desactivar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));
    }

    @Test
    void activarSocio_ShouldReturnSocio() throws Exception {
        SocioDTO socioDTO = new SocioDTO();
        socioDTO.setId(1L);
        socioDTO.setActivo(true);

        when(socioService.activarSocio(1L)).thenReturn(socioDTO);

        mockMvc.perform(patch("/api/socios/1/activar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(true));
    }

    @Test
    void deleteSocio_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/socios/1"))
                .andExpect(status().isNoContent());
    }
}
