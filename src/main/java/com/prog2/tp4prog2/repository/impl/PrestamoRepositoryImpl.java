package com.prog2.tp4prog2.repository.impl;

import com.prog2.tp4prog2.model.Prestamo;
import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.model.Usuario;
import com.prog2.tp4prog2.repository.PrestamoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of PrestamoRepository using in-memory storage
 */
@Repository
public class PrestamoRepositoryImpl implements PrestamoRepository {
    private final Map<Long, Prestamo> prestamos = new HashMap<>();
    private Long nextId = 1L;
    
    @Override
    public List<Prestamo> findAll() {
        return new ArrayList<>(prestamos.values());
    }
    
    @Override
    public Optional<Prestamo> findById(Long id) {
        return Optional.ofNullable(prestamos.get(id));
    }
    
    @Override
    public Prestamo save(Prestamo prestamo) {
        if (prestamo.getId() == null) {
            prestamo.setId(nextId++);
        }
        prestamos.put(prestamo.getId(), prestamo);
        return prestamo;
    }
    
    @Override
    public void deleteById(Long id) {
        prestamos.remove(id);
    }
    
    @Override
    public List<Prestamo> findByUsuario(Usuario usuario) {
        return prestamos.values().stream()
            .filter(prestamo -> prestamo.getUsuario().equals(usuario))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Prestamo> findByLibro(Libro libro) {
        return prestamos.values().stream()
            .filter(prestamo -> prestamo.getLibro().equals(libro))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Prestamo> findByFechaPrestamo(LocalDate fechaPrestamo) {
        return prestamos.values().stream()
            .filter(prestamo -> prestamo.getFechaPrestamo().equals(fechaPrestamo))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Prestamo> findByFechaDevolucion(LocalDate fechaDevolucion) {
        return prestamos.values().stream()
            .filter(prestamo -> prestamo.getFechaDevolucion().equals(fechaDevolucion))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Prestamo> findActivePrestamos() {
        LocalDate today = LocalDate.now();
        return prestamos.values().stream()
            .filter(prestamo -> prestamo.getFechaDevolucion().isAfter(today))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Prestamo> findOverduePrestamos() {
        LocalDate today = LocalDate.now();
        return prestamos.values().stream()
            .filter(prestamo -> prestamo.getFechaDevolucion().isBefore(today))
            .collect(Collectors.toList());
    }
}