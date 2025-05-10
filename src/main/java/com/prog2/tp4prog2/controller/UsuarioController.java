package com.prog2.tp4prog2.controller;

import com.prog2.tp4prog2.model.Usuario;
import com.prog2.tp4prog2.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones CRUD sobre usuarios.
 * Proporciona endpoints para crear, leer, actualizar y eliminar usuarios.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     * 
     * @return ResponseEntity con la lista de todos los usuarios y código de estado 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    /**
     * Obtiene un usuario específico por su ID.
     * 
     * @param id El identificador único del usuario a buscar
     * @return ResponseEntity con el usuario encontrado y código de estado 200 (OK),
     *         o código 404 (Not Found) si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene un usuario específico por su dirección de email.
     * 
     * @param email La dirección de email del usuario a buscar
     * @return ResponseEntity con el usuario encontrado y código de estado 200 (OK),
     *         o código 404 (Not Found) si no existe
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> obtenerPorEmail(@PathVariable String email) {
        try {
            Usuario usuario = usuarioService.buscarPorEmail(email);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea un nuevo usuario en el sistema.
     * 
     * @param usuario El objeto Usuario con los datos a guardar
     * @return ResponseEntity con el usuario creado y código de estado 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.guardar(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    /**
     * Actualiza un usuario existente identificado por su ID.
     * 
     * @param id El identificador único del usuario a actualizar
     * @param usuario El objeto Usuario con los datos actualizados
     * @return ResponseEntity con el usuario actualizado y código de estado 200 (OK),
     *         o código 404 (Not Found) si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.buscarYActualizar(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina un usuario existente identificado por su ID.
     * 
     * @param id El identificador único del usuario a eliminar
     * @return ResponseEntity con código de estado 204 (No Content) si se eliminó correctamente,
     *         o código 404 (Not Found) si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
