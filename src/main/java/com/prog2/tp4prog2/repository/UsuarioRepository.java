package com.prog2.tp4prog2.repository;

import com.prog2.tp4prog2.model.Usuario;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Usuario entity
 */
public interface UsuarioRepository {
    
    /**
     * Find all users
     * @return list of all users
     */
    List<Usuario> findAll();
    
    /**
     * Find a user by its ID
     * @param id the user ID
     * @return the user if found
     */
    Optional<Usuario> findById(Long id);
    
    /**
     * Save a user
     * @param usuario the user to save
     * @return the saved user
     */
    Usuario save(Usuario usuario);
    
    /**
     * Delete a user
     * @param id the ID of the user to delete
     */
    void deleteById(Long id);
    
    /**
     * Find users by name
     * @param nombre the name to search for
     * @return list of users with matching name
     */
    List<Usuario> findByNombre(String nombre);
    
    /**
     * Find a user by email
     * @param email the email to search for
     * @return the user with matching email
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Find users by status
     * @param estado the status to search for
     * @return list of users with matching status
     */
    List<Usuario> findByEstado(String estado);
}