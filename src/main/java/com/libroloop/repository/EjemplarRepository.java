package com.libroloop.repository;

import com.libroloop.entity.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {

    List<Ejemplar> findByLibroId(Long libroId);

    List<Ejemplar> findByEstado(Ejemplar.Estado estado);

    List<Ejemplar> findByLibroIdAndEstado(Long libroId, Ejemplar.Estado estado);

    Optional<Ejemplar> findByCodigoInventario(String codigoInventario);

    boolean existsByCodigoInventario(String codigoInventario);
}
