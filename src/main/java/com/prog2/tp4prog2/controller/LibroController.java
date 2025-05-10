package com.prog2.tp4prog2.controller;

import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones CRUD sobre libros.
 * Proporciona endpoints para crear, leer, actualizar y eliminar libros.
 */
@RestController
@RequestMapping("/api/libros")
public class LibroController {
    private final LibroService libroService;

    @Autowired
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    /**
     * Obtiene todos los libros disponibles en el sistema.
     * 
     * @return ResponseEntity con la lista de todos los libros y código de estado 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Libro>> obtenerTodos() {
        return ResponseEntity.ok(libroService.obtenerTodos());
    }

    /**
     * Obtiene un libro específico por su ID.
     * 
     * @param id El identificador único del libro a buscar
     * @return ResponseEntity con el libro encontrado y código de estado 200 (OK),
     *         o código 404 (Not Found) si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerPorId(@PathVariable Long id) {
        try {
            Libro libro = libroService.buscarPorId(id);
            return ResponseEntity.ok(libro);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene un libro específico por su ISBN.
     * 
     * @param isbn El código ISBN del libro a buscar
     * @return ResponseEntity con el libro encontrado y código de estado 200 (OK),
     *         o código 404 (Not Found) si no existe
     */
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Libro> obtenerPorIsbn(@PathVariable String isbn) {
        try {
            Libro libro = libroService.buscarPorIsbn(isbn);
            return ResponseEntity.ok(libro);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea un nuevo libro en el sistema.
     * 
     * @param libro El objeto Libro con los datos a guardar
     * @return ResponseEntity con el libro creado y código de estado 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Libro> crear(@RequestBody Libro libro) {
        Libro nuevoLibro = libroService.guardar(libro);
        return new ResponseEntity<>(nuevoLibro, HttpStatus.CREATED);
    }

    /**
     * Actualiza un libro existente identificado por su ID.
     * 
     * @param id El identificador único del libro a actualizar
     * @param libro El objeto Libro con los datos actualizados
     * @return ResponseEntity con el libro actualizado y código de estado 200 (OK),
     *         o código 404 (Not Found) si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizar(@PathVariable Long id, @RequestBody Libro libro) {
        try {
            Libro libroActualizado = libroService.buscarYActualizar(id, libro);
            return ResponseEntity.ok(libroActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina un libro existente identificado por su ID.
     * 
     * @param id El identificador único del libro a eliminar
     * @return ResponseEntity con código de estado 204 (No Content) si se eliminó correctamente,
     *         o código 404 (Not Found) si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            libroService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
