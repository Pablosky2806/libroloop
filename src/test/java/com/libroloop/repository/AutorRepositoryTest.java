package com.libroloop.repository;

import com.libroloop.entity.Autor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AutorRepositoryTest {

    @Autowired
    private AutorRepository autorRepository;

    @Test
    void whenSaveAutor_thenIdIsGenerated() {
        Autor autor = new Autor();
        autor.setNombre("Gabriel García");
        autor.setApellido("Márquez");

        Autor savedAutor = autorRepository.save(autor);

        assertThat(savedAutor.getId()).isNotNull();
        assertThat(savedAutor.getNombre()).isEqualTo("Gabriel García");
    }

    @Test
    void whenFindByNombre_thenReturnAutor() {
        Autor autor = new Autor();
        autor.setNombre("Gabriel García");
        autor.setApellido("Márquez");
        autorRepository.save(autor);

        Optional<Autor> found = autorRepository.findByNombre("Gabriel García");

        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Gabriel García");
    }

    @Test
    void whenExistsByNombre_thenReturnTrue() {
        Autor autor = new Autor();
        autor.setNombre("Gabriel García");
        autorRepository.save(autor);

        boolean exists = autorRepository.existsByNombre("Gabriel García");

        assertThat(exists).isTrue();
    }
}
