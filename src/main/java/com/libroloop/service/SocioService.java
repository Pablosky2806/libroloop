package com.libroloop.service;

import com.libroloop.dto.SocioDTO;
import com.libroloop.dto.SocioRequestDTO;
import com.libroloop.entity.Socio;
import com.libroloop.exception.BadRequestException;
import com.libroloop.exception.ResourceNotFoundException;
import com.libroloop.mapper.SocioMapper;
import com.libroloop.repository.SocioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SocioService {

    private final SocioRepository socioRepository;
    private final SocioMapper socioMapper;

    public SocioDTO createSocio(SocioRequestDTO socioRequestDTO) {
        if (socioRepository.existsByEmail(socioRequestDTO.getEmail())) {
            throw new BadRequestException("Ya existe un socio con el email: " + socioRequestDTO.getEmail());
        }

        Socio socio = socioMapper.toEntity(socioRequestDTO);
        Socio savedSocio = socioRepository.save(socio);
        return socioMapper.toDTO(savedSocio);
    }

    public SocioDTO getSocioById(Long id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));
        return socioMapper.toDTO(socio);
    }

    public SocioDTO getSocioByEmail(String email) {
        Socio socio = socioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con email: " + email));
        return socioMapper.toDTO(socio);
    }

    public List<SocioDTO> getAllSocios() {
        List<Socio> socios = socioRepository.findAll();
        return socioMapper.toDTOList(socios);
    }

    public List<SocioDTO> getSociosActivos() {
        List<Socio> socios = socioRepository.findAll().stream()
                .filter(Socio::getActivo)
                .toList();
        return socioMapper.toDTOList(socios);
    }

    public SocioDTO updateSocio(Long id, SocioDTO socioDTO) {
        Socio existingSocio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));

        if (!existingSocio.getEmail().equals(socioDTO.getEmail()) 
                && socioRepository.existsByEmail(socioDTO.getEmail())) {
            throw new BadRequestException("Ya existe un socio con el email: " + socioDTO.getEmail());
        }

        socioMapper.updateEntityFromDTO(socioDTO, existingSocio);
        Socio updatedSocio = socioRepository.save(existingSocio);
        return socioMapper.toDTO(updatedSocio);
    }

    public SocioDTO desactivarSocio(Long id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));

        socio.setActivo(false);
        Socio updatedSocio = socioRepository.save(socio);
        return socioMapper.toDTO(updatedSocio);
    }

    public SocioDTO activarSocio(Long id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));

        socio.setActivo(true);
        Socio updatedSocio = socioRepository.save(socio);
        return socioMapper.toDTO(updatedSocio);
    }

    public void deleteSocio(Long id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));

        if (!socio.getPrestamos().isEmpty()) {
            throw new BadRequestException("No se puede eliminar el socio porque tiene préstamos activos");
        }

        socioRepository.delete(socio);
    }
}
