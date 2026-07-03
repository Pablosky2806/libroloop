package com.libroloop.repository;

import com.libroloop.entity.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Long> {

    boolean existsByEmail(String email);

    Optional<Socio> findByEmail(String email);

    Optional<Socio> findByEmailAndActivoTrue(String email);
}
