package com.prog2.tp4prog2.repository;

import com.prog2.tp4prog2.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {

    /**
     * @return lista de todos los usuarios
     */
    List<Usuario> findAll();

    /**
     * @param id identificador del usuario
     * @return el usuario si se encuentra
     */
    Optional<Usuario> findById(Long id);

    /**
     * @param usuario el usuario a guardar
     * @return el usuario guardado
     */
    Usuario save(Usuario usuario);

    /**
     * @param id identificador del usuario a eliminar
     */
    void deleteById(Long id);

    /**
     * @param nombre nombre a buscar
     * @return lista de usuarios con nombre coincidente
     */
    List<Usuario> findByNombre(String nombre);

    /**
     * @param email email a buscar
     * @return usuario con email coincidente
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * @param estado estado a buscar
     * @return lista de usuarios con estado coincidente
     */
    List<Usuario> findByEstado(String estado);
}
