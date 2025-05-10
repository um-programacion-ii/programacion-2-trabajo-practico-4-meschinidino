package com.prog2.tp4prog2.repository;

import com.prog2.tp4prog2.model.Libro;
import java.util.List;
import java.util.Optional;

public interface LibroRepository {

    /**
     * @return lista de todos los libros
     */
    List<Libro> findAll();

    /**
     * @param id identificador del libro
     * @return el libro si se encuentra
     */
    Optional<Libro> findById(Long id);

    /**
     * @param libro el libro a guardar
     * @return el libro guardado
     */
    Libro save(Libro libro);

    /**
     * @param id identificador del libro a eliminar
     */
    void deleteById(Long id);

    /**
     * @param titulo título a buscar
     * @return lista de libros con título coincidente
     */
    List<Libro> findByTitulo(String titulo);

    /**
     * @param autor autor a buscar
     * @return lista de libros del autor
     */
    List<Libro> findByAutor(String autor);

    /**
     * @param isbn ISBN a buscar
     * @return libro con el ISBN correspondiente
     */
    Optional<Libro> findByIsbn(String isbn);
}
