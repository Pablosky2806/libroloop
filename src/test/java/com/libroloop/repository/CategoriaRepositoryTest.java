package com.libroloop.repository;

import com.libroloop.entity.Categoria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void whenSaveCategoria_thenIdIsGenerated() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Ficción");
        categoria.setDescripcion("Libros de ficción literaria");

        Categoria savedCategoria = categoriaRepository.save(categoria);

        assertThat(savedCategoria.getId()).isNotNull();
        assertThat(savedCategoria.getNombre()).isEqualTo("Ficción");
    }

    @Test
    void whenFindByNombre_thenReturnCategoria() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Ficción");
        categoriaRepository.save(categoria);

        Optional<Categoria> found = categoriaRepository.findByNombre("Ficción");

        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Ficción");
    }

    @Test
    void whenExistsByNombre_thenReturnTrue() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Ficción");
        categoriaRepository.save(categoria);

        boolean exists = categoriaRepository.existsByNombre("Ficción");

        assertThat(exists).isTrue();
    }
}
