package com.prog2.tp4prog2.repository;

import com.prog2.tp4prog2.model.Prestamo;
import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.model.Usuario;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Prestamo entity
 */
public interface PrestamoRepository {
    
    /**
     * Find all loans
     * @return list of all loans
     */
    List<Prestamo> findAll();
    
    /**
     * Find a loan by its ID
     * @param id the loan ID
     * @return the loan if found
     */
    Optional<Prestamo> findById(Long id);
    
    /**
     * Save a loan
     * @param prestamo the loan to save
     * @return the saved loan
     */
    Prestamo save(Prestamo prestamo);
    
    /**
     * Delete a loan
     * @param id the ID of the loan to delete
     */
    void deleteById(Long id);
    
    /**
     * Find loans by user
     * @param usuario the user to search for
     * @return list of loans for the specified user
     */
    List<Prestamo> findByUsuario(Usuario usuario);
    
    /**
     * Find loans by book
     * @param libro the book to search for
     * @return list of loans for the specified book
     */
    List<Prestamo> findByLibro(Libro libro);
    
    /**
     * Find loans by loan date
     * @param fechaPrestamo the loan date to search for
     * @return list of loans with matching loan date
     */
    List<Prestamo> findByFechaPrestamo(LocalDate fechaPrestamo);
    
    /**
     * Find loans by return date
     * @param fechaDevolucion the return date to search for
     * @return list of loans with matching return date
     */
    List<Prestamo> findByFechaDevolucion(LocalDate fechaDevolucion);
    
    /**
     * Find active loans (loans where the return date is after the current date)
     * @return list of active loans
     */
    List<Prestamo> findActivePrestamos();
    
    /**
     * Find overdue loans (loans where the return date is before the current date)
     * @return list of overdue loans
     */
    List<Prestamo> findOverduePrestamos();
}