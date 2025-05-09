package com.prog2.tp4prog2.service;

import com.prog2.tp4prog2.model.Usuario;
import java.util.List;

/**
 * Service interface for Usuario entity
 */
public interface UsuarioService {
    /**
     * Find a user by email
     * @param email the email to search for
     * @return the user with matching email
     */
    Usuario buscarPorEmail(String email);
    
    /**
     * Get all users
     * @return list of all users
     */
    List<Usuario> obtenerTodos();
    
    /**
     * Save a user
     * @param usuario the user to save
     * @return the saved user
     */
    Usuario guardar(Usuario usuario);
    
    /**
     * Delete a user
     * @param id the ID of the user to delete
     */
    void eliminar(Long id);
    
    /**
     * Update a user
     * @param id the ID of the user to update
     * @param usuario the updated user data
     * @return the updated user
     */
    Usuario actualizar(Long id, Usuario usuario);
}