package com.prog2.tp4prog2.repository;

import com.prog2.tp4prog2.model.Libro;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Libro entity
 */
public interface LibroRepository {
    
    /**
     * Find all books
     * @return list of all books
     */
    List<Libro> findAll();
    
    /**
     * Find a book by its ID
     * @param id the book ID
     * @return the book if found
     */
    Optional<Libro> findById(Long id);
    
    /**
     * Save a book
     * @param libro the book to save
     * @return the saved book
     */
    Libro save(Libro libro);
    
    /**
     * Delete a book
     * @param id the ID of the book to delete
     */
    void deleteById(Long id);
    
    /**
     * Find books by title
     * @param titulo the title to search for
     * @return list of books with matching title
     */
    List<Libro> findByTitulo(String titulo);
    
    /**
     * Find books by author
     * @param autor the author to search for
     * @return list of books with matching author
     */
    List<Libro> findByAutor(String autor);
    
    /**
     * Find books by ISBN
     * @param isbn the ISBN to search for
     * @return the book with matching ISBN
     */
    Optional<Libro> findByIsbn(String isbn);
}