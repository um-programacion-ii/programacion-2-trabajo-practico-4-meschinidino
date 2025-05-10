package com.prog2.tp4prog2.service;

import com.prog2.tp4prog2.model.Usuario;
import java.util.List;

/**
 * Interfaz de servicio para la entidad Usuario
 */
public interface UsuarioService {
    /**
     * @param email email a buscar
     * @return usuario con email coincidente
     */
    Usuario buscarPorEmail(String email);

    /**
     * @param id ID a buscar
     * @return usuario con ID coincidente
     */
    Usuario buscarPorId(Long id);

    /**
     * @return lista de todos los usuarios
     */
    List<Usuario> obtenerTodos();

    /**
     * @param usuario usuario a guardar
     * @return usuario guardado
     */
    Usuario guardar(Usuario usuario);

    /**
     * @param id identificador del usuario a eliminar
     */
    void eliminar(Long id);

    /**
     * @param id identificador del usuario a buscar y actualizar
     * @param usuario datos actualizados del usuario
     * @return usuario actualizado
     */
    Usuario buscarYActualizar(Long id, Usuario usuario);
}
