package com.libroloop.repository;

import com.libroloop.entity.Autor;
import com.libroloop.entity.Ejemplar;
import com.libroloop.entity.Libro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EjemplarRepositoryTest {

    @Autowired
    private EjemplarRepository ejemplarRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Test
    void whenSaveEjemplar_thenIdIsGenerated() {
        Autor autor = new Autor();
        autor.setNombre("Gabriel García");
        autor.setApellido("Márquez");
        Autor savedAutor = autorRepository.save(autor);

        Libro libro = new Libro();
        libro.setIsbn("978-3-16-148410-0");
        libro.setTitulo("Cien años de soledad");
        libro.setAutor(savedAutor);
        Libro savedLibro = libroRepository.save(libro);

        Ejemplar ejemplar = new Ejemplar();
        ejemplar.setLibro(savedLibro);
        ejemplar.setEstado(Ejemplar.Estado.DISPONIBLE);

        Ejemplar savedEjemplar = ejemplarRepository.save(ejemplar);

        assertThat(savedEjemplar.getId()).isNotNull();
        assertThat(savedEjemplar.getEstado()).isEqualTo(Ejemplar.Estado.DISPONIBLE);
    }

    @Test
    void whenFindByLibroId_thenReturnEjemplares() {
        Autor autor = new Autor();
        autor.setNombre("Gabriel García");
        autor.setApellido("Márquez");
        Autor savedAutor = autorRepository.save(autor);

        Libro libro = new Libro();
        libro.setIsbn("978-3-16-148410-0");
        libro.setTitulo("Cien años de soledad");
        libro.setAutor(savedAutor);
        Libro savedLibro = libroRepository.save(libro);

        Ejemplar ejemplar1 = new Ejemplar();
        ejemplar1.setLibro(savedLibro);
        ejemplar1.setEstado(Ejemplar.Estado.DISPONIBLE);
        ejemplarRepository.save(ejemplar1);

        Ejemplar ejemplar2 = new Ejemplar();
        ejemplar2.setLibro(savedLibro);
        ejemplar2.setEstado(Ejemplar.Estado.DISPONIBLE);
        ejemplarRepository.save(ejemplar2);

        List<Ejemplar> ejemplares = ejemplarRepository.findByLibroId(savedLibro.getId());

        assertThat(ejemplares).hasSize(2);
    }

    @Test
    void whenFindByEstado_thenReturnEjemplares() {
        Autor autor = new Autor();
        autor.setNombre("Gabriel García");
        autor.setApellido("Márquez");
        Autor savedAutor = autorRepository.save(autor);

        Libro libro = new Libro();
        libro.setIsbn("978-3-16-148410-0");
        libro.setTitulo("Cien años de soledad");
        libro.setAutor(savedAutor);
        Libro savedLibro = libroRepository.save(libro);

        Ejemplar ejemplar1 = new Ejemplar();
        ejemplar1.setLibro(savedLibro);
        ejemplar1.setEstado(Ejemplar.Estado.DISPONIBLE);
        ejemplarRepository.save(ejemplar1);

        Ejemplar ejemplar2 = new Ejemplar();
        ejemplar2.setLibro(savedLibro);
        ejemplar2.setEstado(Ejemplar.Estado.PRESTADO);
        ejemplarRepository.save(ejemplar2);

        List<Ejemplar> disponibles = ejemplarRepository.findByEstado(Ejemplar.Estado.DISPONIBLE);

        assertThat(disponibles).hasSize(1);
        assertThat(disponibles.get(0).getEstado()).isEqualTo(Ejemplar.Estado.DISPONIBLE);
    }
}
