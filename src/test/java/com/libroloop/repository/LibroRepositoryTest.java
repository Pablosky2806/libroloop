package com.libroloop.repository;

import com.libroloop.entity.Autor;
import com.libroloop.entity.Libro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LibroRepositoryTest {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Test
    void whenSaveLibro_thenIdIsGenerated() {
        Autor autor = new Autor();
        autor.setNombre("Gabriel García");
        autor.setApellido("Márquez");
        Autor savedAutor = autorRepository.save(autor);

        Libro libro = new Libro();
        libro.setIsbn("978-3-16-148410-0");
        libro.setTitulo("Cien años de soledad");
        libro.setAutor(savedAutor);

        Libro savedLibro = libroRepository.save(libro);

        assertThat(savedLibro.getId()).isNotNull();
        assertThat(savedLibro.getIsbn()).isEqualTo("978-3-16-148410-0");
    }

    @Test
    void whenFindByIsbn_thenReturnLibro() {
        Autor autor = new Autor();
        autor.setNombre("Gabriel García");
        autor.setApellido("Márquez");
        Autor savedAutor = autorRepository.save(autor);

        Libro libro = new Libro();
        libro.setIsbn("978-3-16-148410-0");
        libro.setTitulo("Cien años de soledad");
        libro.setAutor(savedAutor);
        libroRepository.save(libro);

        Optional<Libro> found = libroRepository.findByIsbn("978-3-16-148410-0");

        assertThat(found).isPresent();
        assertThat(found.get().getIsbn()).isEqualTo("978-3-16-148410-0");
    }

    @Test
    void whenExistsByIsbn_thenReturnTrue() {
        Autor autor = new Autor();
        autor.setNombre("Gabriel García");
        autor.setApellido("Márquez");
        Autor savedAutor = autorRepository.save(autor);

        Libro libro = new Libro();
        libro.setIsbn("978-3-16-148410-0");
        libro.setTitulo("Cien años de soledad");
        libro.setAutor(savedAutor);
        libroRepository.save(libro);

        boolean exists = libroRepository.existsByIsbn("978-3-16-148410-0");

        assertThat(exists).isTrue();
    }
}
