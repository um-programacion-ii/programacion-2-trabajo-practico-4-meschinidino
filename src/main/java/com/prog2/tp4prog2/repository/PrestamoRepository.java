package com.prog2.tp4prog2.repository;

import com.prog2.tp4prog2.model.Prestamo;
import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.model.Usuario;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PrestamoRepository {

    /**
     * @return lista de todos los préstamos
     */
    List<Prestamo> findAll();

    /**
     * @param id identificador del préstamo
     * @return el préstamo si se encuentra
     */
    Optional<Prestamo> findById(Long id);

    /**
     * @param prestamo el préstamo a guardar
     * @return el préstamo guardado
     */
    Prestamo save(Prestamo prestamo);

    /**
     * @param id identificador del préstamo a eliminar
     */
    void deleteById(Long id);

    /**
     * @param usuario usuario a buscar
     * @return lista de préstamos del usuario
     */
    List<Prestamo> findByUsuario(Usuario usuario);

    /**
     * @param libro libro a buscar
     * @return lista de préstamos del libro
     */
    List<Prestamo> findByLibro(Libro libro);

    /**
     * @param fechaPrestamo fecha de préstamo a buscar
     * @return lista de préstamos con la fecha indicada
     */
    List<Prestamo> findByFechaPrestamo(LocalDate fechaPrestamo);

    /**
     * @param fechaDevolucion fecha de devolución a buscar
     * @return lista de préstamos con la fecha de devolución indicada
     */
    List<Prestamo> findByFechaDevolucion(LocalDate fechaDevolucion);

    /**
     * @return lista de préstamos activos (fecha de devolución posterior a hoy)
     */
    List<Prestamo> findActivePrestamos();

    /**
     * @return lista de préstamos vencidos (fecha de devolución anterior a hoy)
     */
    List<Prestamo> findOverduePrestamos();
}
