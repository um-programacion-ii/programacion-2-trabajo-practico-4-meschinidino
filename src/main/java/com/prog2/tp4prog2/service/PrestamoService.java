package com.prog2.tp4prog2.service;

import com.prog2.tp4prog2.model.Prestamo;
import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.model.Usuario;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz de servicio para la entidad Prestamo
 */
public interface PrestamoService {
    /**
     * @return lista de todos los préstamos
     */
    List<Prestamo> obtenerTodos();

    /**
     * @param usuario usuario a buscar
     * @return lista de préstamos del usuario
     */
    List<Prestamo> buscarPorUsuario(Usuario usuario);

    /**
     * @param libro libro a buscar
     * @return lista de préstamos del libro
     */
    List<Prestamo> buscarPorLibro(Libro libro);

    /**
     * @return lista de préstamos activos (fecha de devolución posterior a hoy)
     */
    List<Prestamo> buscarPrestamosActivos();

    /**
     * @return lista de préstamos vencidos (fecha de devolución anterior a hoy)
     */
    List<Prestamo> buscarPrestamosVencidos();

    /**
     * @param prestamo préstamo a guardar
     * @return préstamo guardado
     */
    Prestamo guardar(Prestamo prestamo);

    /**
     * @param id identificador del préstamo a eliminar
     */
    void eliminar(Long id);

    /**
     * @param id identificador del préstamo a actualizar
     * @param prestamo datos actualizados del préstamo
     * @return préstamo actualizado
     */
    Prestamo actualizar(Long id, Prestamo prestamo);
}
