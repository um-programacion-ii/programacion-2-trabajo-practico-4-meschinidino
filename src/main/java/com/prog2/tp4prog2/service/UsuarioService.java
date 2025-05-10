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
     * @param id identificador del usuario a actualizar
     * @param usuario datos actualizados del usuario
     * @return usuario actualizado
     */
    Usuario actualizar(Long id, Usuario usuario);
}
