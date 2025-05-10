package com.prog2.tp4prog2.service;

import com.prog2.tp4prog2.model.Libro;
import java.util.List;

/**
 * Interfaz de servicio para la entidad Libro
 */
public interface LibroService {
    /**
     * @param isbn ISBN a buscar
     * @return libro con ISBN coincidente
     */
    Libro buscarPorIsbn(String isbn);

    /**
     * @return lista de todos los libros
     */
    List<Libro> obtenerTodos();

    /**
     * @param libro libro a guardar
     * @return libro guardado
     */
    Libro guardar(Libro libro);

    /**
     * @param id identificador del libro a eliminar
     */
    void eliminar(Long id);

    /**
     * @param id identificador del libro a actualizar
     * @param libro datos actualizados del libro
     * @return libro actualizado
     */
    Libro actualizar(Long id, Libro libro);
}
