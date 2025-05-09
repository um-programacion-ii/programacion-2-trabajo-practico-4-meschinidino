package com.prog2.tp4prog2.service.impl;

import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.repository.LibroRepository;
import com.prog2.tp4prog2.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Implementation of LibroService
 */
@Service
public class LibroServiceImpl implements LibroService {
    private final LibroRepository libroRepository;

    /**
     * Constructor with dependency injection
     * @param libroRepository the repository to use
     */
    @Autowired
    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {
        return libroRepository.findByIsbn(isbn)
            .orElseThrow(() -> new RuntimeException("Libro no encontrado con ISBN: " + isbn));
    }

    @Override
    public List<Libro> obtenerTodos() {
        return libroRepository.findAll();
    }

    @Override
    public Libro guardar(Libro libro) {
        return libroRepository.save(libro);
    }

    @Override
    public void eliminar(Long id) {
        libroRepository.deleteById(id);
    }

    @Override
    public Libro actualizar(Long id, Libro libro) {
        if (!libroRepository.findById(id).isPresent()) {
            throw new RuntimeException("Libro no encontrado con ID: " + id);
        }
        libro.setId(id);
        return libroRepository.save(libro);
    }
}
