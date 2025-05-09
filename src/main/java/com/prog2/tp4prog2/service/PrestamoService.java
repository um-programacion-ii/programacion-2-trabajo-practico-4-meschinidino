package com.prog2.tp4prog2.service;

import com.prog2.tp4prog2.model.Prestamo;
import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.model.Usuario;
import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for Prestamo entity
 */
public interface PrestamoService {
    /**
     * Get all loans
     * @return list of all loans
     */
    List<Prestamo> obtenerTodos();
    
    /**
     * Find loans by user
     * @param usuario the user to search for
     * @return list of loans for the specified user
     */
    List<Prestamo> buscarPorUsuario(Usuario usuario);
    
    /**
     * Find loans by book
     * @param libro the book to search for
     * @return list of loans for the specified book
     */
    List<Prestamo> buscarPorLibro(Libro libro);
    
    /**
     * Find active loans (loans where the return date is after the current date)
     * @return list of active loans
     */
    List<Prestamo> buscarPrestamosActivos();
    
    /**
     * Find overdue loans (loans where the return date is before the current date)
     * @return list of overdue loans
     */
    List<Prestamo> buscarPrestamosVencidos();
    
    /**
     * Save a loan
     * @param prestamo the loan to save
     * @return the saved loan
     */
    Prestamo guardar(Prestamo prestamo);
    
    /**
     * Delete a loan
     * @param id the ID of the loan to delete
     */
    void eliminar(Long id);
    
    /**
     * Update a loan
     * @param id the ID of the loan to update
     * @param prestamo the updated loan data
     * @return the updated loan
     */
    Prestamo actualizar(Long id, Prestamo prestamo);
}