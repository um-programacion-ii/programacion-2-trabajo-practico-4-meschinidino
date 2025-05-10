package com.prog2.tp4prog2.controller;

import com.prog2.tp4prog2.model.Prestamo;
import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.model.Usuario;
import com.prog2.tp4prog2.service.PrestamoService;
import com.prog2.tp4prog2.service.LibroService;
import com.prog2.tp4prog2.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones CRUD sobre préstamos.
 * Proporciona endpoints para crear, leer, actualizar y eliminar préstamos,
 * así como para consultar préstamos por usuario, libro, y estado (activos/vencidos).
 */
@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {
    private final PrestamoService prestamoService;
    private final LibroService libroService;
    private final UsuarioService usuarioService;

    @Autowired
    public PrestamoController(PrestamoService prestamoService, LibroService libroService, UsuarioService usuarioService) {
        this.prestamoService = prestamoService;
        this.libroService = libroService;
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todos los préstamos registrados en el sistema.
     * 
     * @return ResponseEntity con la lista de todos los préstamos y código de estado 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Prestamo>> obtenerTodos() {
        return ResponseEntity.ok(prestamoService.obtenerTodos());
    }

    /**
     * Obtiene todos los préstamos activos (con fecha de devolución posterior a la fecha actual).
     * 
     * @return ResponseEntity con la lista de préstamos activos y código de estado 200 (OK)
     */
    @GetMapping("/activos")
    public ResponseEntity<List<Prestamo>> obtenerPrestamosActivos() {
        return ResponseEntity.ok(prestamoService.buscarPrestamosActivos());
    }

    /**
     * Obtiene todos los préstamos vencidos (con fecha de devolución anterior a la fecha actual).
     * 
     * @return ResponseEntity con la lista de préstamos vencidos y código de estado 200 (OK)
     */
    @GetMapping("/vencidos")
    public ResponseEntity<List<Prestamo>> obtenerPrestamosVencidos() {
        return ResponseEntity.ok(prestamoService.buscarPrestamosVencidos());
    }

    /**
     * Obtiene todos los préstamos asociados a un usuario específico.
     * 
     * @param usuarioId El identificador único del usuario
     * @return ResponseEntity con la lista de préstamos del usuario y código de estado 200 (OK),
     *         o código 404 (Not Found) si el usuario no existe
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Prestamo>> obtenerPrestamosPorUsuario(@PathVariable Long usuarioId) {
        try {
            Usuario usuario = usuarioService.buscarPorId(usuarioId);
            return ResponseEntity.ok(prestamoService.buscarPorUsuario(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene todos los préstamos asociados a un libro específico.
     * 
     * @param libroId El identificador único del libro
     * @return ResponseEntity con la lista de préstamos del libro y código de estado 200 (OK),
     *         o código 404 (Not Found) si el libro no existe
     */
    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<Prestamo>> obtenerPrestamosPorLibro(@PathVariable Long libroId) {
        try {
            Libro libro = libroService.buscarPorId(libroId);
            return ResponseEntity.ok(prestamoService.buscarPorLibro(libro));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea un nuevo préstamo en el sistema.
     * 
     * @param prestamo El objeto Prestamo con los datos a guardar
     * @return ResponseEntity con el préstamo creado y código de estado 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Prestamo> crear(@RequestBody Prestamo prestamo) {
        Prestamo nuevoPrestamo = prestamoService.guardar(prestamo);
        return new ResponseEntity<>(nuevoPrestamo, HttpStatus.CREATED);
    }

    /**
     * Actualiza un préstamo existente identificado por su ID.
     * 
     * @param id El identificador único del préstamo a actualizar
     * @param prestamo El objeto Prestamo con los datos actualizados
     * @return ResponseEntity con el préstamo actualizado y código de estado 200 (OK),
     *         o código 404 (Not Found) si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Prestamo> actualizar(@PathVariable Long id, @RequestBody Prestamo prestamo) {
        try {
            Prestamo prestamoActualizado = prestamoService.buscarYActualizar(id, prestamo);
            return ResponseEntity.ok(prestamoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina un préstamo existente identificado por su ID.
     * 
     * @param id El identificador único del préstamo a eliminar
     * @return ResponseEntity con código de estado 204 (No Content) si se eliminó correctamente,
     *         o código 404 (Not Found) si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            prestamoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
