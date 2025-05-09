package com.prog2.tp4prog2.service;

import com.prog2.tp4prog2.model.Libro;
import java.util.List;

/**
 * Service interface for Libro entity
 */
public interface LibroService {
    /**
     * Find a book by its ISBN
     * @param isbn the ISBN to search for
     * @return the book with matching ISBN
     */
    Libro buscarPorIsbn(String isbn);
    
    /**
     * Get all books
     * @return list of all books
     */
    List<Libro> obtenerTodos();
    
    /**
     * Save a book
     * @param libro the book to save
     * @return the saved book
     */
    Libro guardar(Libro libro);
    
    /**
     * Delete a book
     * @param id the ID of the book to delete
     */
    void eliminar(Long id);
    
    /**
     * Update a book
     * @param id the ID of the book to update
     * @param libro the updated book data
     * @return the updated book
     */
    Libro actualizar(Long id, Libro libro);
}