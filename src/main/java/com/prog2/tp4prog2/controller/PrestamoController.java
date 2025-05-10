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

    @GetMapping
    public ResponseEntity<List<Prestamo>> obtenerTodos() {
        return ResponseEntity.ok(prestamoService.obtenerTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Prestamo>> obtenerPrestamosActivos() {
        return ResponseEntity.ok(prestamoService.buscarPrestamosActivos());
    }

    @GetMapping("/vencidos")
    public ResponseEntity<List<Prestamo>> obtenerPrestamosVencidos() {
        return ResponseEntity.ok(prestamoService.buscarPrestamosVencidos());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Prestamo>> obtenerPrestamosPorUsuario(@PathVariable Long usuarioId) {
        try {
            Usuario usuario = usuarioService.buscarPorId(usuarioId);
            return ResponseEntity.ok(prestamoService.buscarPorUsuario(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<Prestamo>> obtenerPrestamosPorLibro(@PathVariable Long libroId) {
        try {
            Libro libro = libroService.buscarPorId(libroId);
            return ResponseEntity.ok(prestamoService.buscarPorLibro(libro));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Prestamo> crear(@RequestBody Prestamo prestamo) {
        Prestamo nuevoPrestamo = prestamoService.guardar(prestamo);
        return new ResponseEntity<>(nuevoPrestamo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prestamo> actualizar(@PathVariable Long id, @RequestBody Prestamo prestamo) {
        try {
            Prestamo prestamoActualizado = prestamoService.buscarYActualizar(id, prestamo);
            return ResponseEntity.ok(prestamoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

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
