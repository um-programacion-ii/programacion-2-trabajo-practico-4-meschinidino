package com.prog2.tp4prog2.repository.impl;

import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.repository.LibroRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del repositorio de libros
 */
@Repository
public class LibroRepositoryImpl implements LibroRepository {
    private final Map<Long, Libro> libros = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public List<Libro> findAll() {
        return new ArrayList<>(libros.values());
    }

    @Override
    public Optional<Libro> findById(Long id) {
        return Optional.ofNullable(libros.get(id));
    }

    @Override
    public Libro save(Libro libro) {
        if (libro.getId() == null) {
            libro.setId(nextId++);
        }
        libros.put(libro.getId(), libro);
        return libro;
    }

    @Override
    public void deleteById(Long id) {
        libros.remove(id);
    }

    @Override
    public List<Libro> findByTitulo(String titulo) {
        return libros.values().stream()
            .filter(libro -> libro.getTitulo().contains(titulo))
            .collect(Collectors.toList());
    }

    @Override
    public List<Libro> findByAutor(String autor) {
        return libros.values().stream()
            .filter(libro -> libro.getAutor().contains(autor))
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Libro> findByIsbn(String isbn) {
        return libros.values().stream()
            .filter(libro -> libro.getIsbn().equals(isbn))
            .findFirst();
    }
}
