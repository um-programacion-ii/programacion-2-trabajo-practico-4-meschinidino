package com.prog2.tp4prog2.service.impl;

import com.prog2.tp4prog2.model.Prestamo;
import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.model.Usuario;
import com.prog2.tp4prog2.repository.PrestamoRepository;
import com.prog2.tp4prog2.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoServiceImpl implements PrestamoService {
    private final PrestamoRepository prestamoRepository;

    @Autowired
    public PrestamoServiceImpl(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public List<Prestamo> obtenerTodos() {
        return prestamoRepository.findAll();
    }

    @Override
    public List<Prestamo> buscarPorUsuario(Usuario usuario) {
        return prestamoRepository.findByUsuario(usuario);
    }

    @Override
    public List<Prestamo> buscarPorLibro(Libro libro) {
        return prestamoRepository.findByLibro(libro);
    }

    @Override
    public List<Prestamo> buscarPrestamosActivos() {
        return prestamoRepository.findActivePrestamos();
    }

    @Override
    public List<Prestamo> buscarPrestamosVencidos() {
        return prestamoRepository.findOverduePrestamos();
    }

    @Override
    public Prestamo guardar(Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }

    @Override
    public void eliminar(Long id) {
        prestamoRepository.deleteById(id);
    }

    @Override
    public Prestamo actualizar(Long id, Prestamo prestamo) {
        if (!prestamoRepository.findById(id).isPresent()) {
            throw new RuntimeException("Prestamo no encontrado con ID: " + id);
        }
        prestamo.setId(id);
        return prestamoRepository.save(prestamo);
    }
}
