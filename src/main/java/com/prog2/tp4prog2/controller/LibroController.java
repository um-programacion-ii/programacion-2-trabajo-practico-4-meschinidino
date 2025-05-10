package com.prog2.tp4prog2.controller;

import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroController {
    private final LibroService libroService;

    @Autowired
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public ResponseEntity<List<Libro>> obtenerTodos() {
        return ResponseEntity.ok(libroService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerPorId(@PathVariable Long id) {
        try {
            Libro libro = libroService.buscarPorIsbn(id.toString());
            return ResponseEntity.ok(libro);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Libro> obtenerPorIsbn(@PathVariable String isbn) {
        try {
            Libro libro = libroService.buscarPorIsbn(isbn);
            return ResponseEntity.ok(libro);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Libro> crear(@RequestBody Libro libro) {
        Libro nuevoLibro = libroService.guardar(libro);
        return new ResponseEntity<>(nuevoLibro, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizar(@PathVariable Long id, @RequestBody Libro libro) {
        try {
            Libro libroActualizado = libroService.actualizar(id, libro);
            return ResponseEntity.ok(libroActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

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