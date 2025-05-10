package com.prog2.tp4prog2.service;

import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.model.Prestamo;
import com.prog2.tp4prog2.model.Usuario;
import com.prog2.tp4prog2.repository.PrestamoRepository;
import com.prog2.tp4prog2.service.impl.PrestamoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrestamoServiceTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    private PrestamoService prestamoService;
    private Prestamo prestamo1;
    private Prestamo prestamo2;
    private Usuario usuario1;
    private Usuario usuario2;
    private Libro libro1;
    private Libro libro2;
    private LocalDate today;
    private LocalDate tomorrow;
    private LocalDate yesterday;

    @BeforeEach
    void setUp() {
        // Initialize the service with the mocked repository
        prestamoService = new PrestamoServiceImpl(prestamoRepository);
        
        // Set up dates
        today = LocalDate.now();
        tomorrow = today.plusDays(1);
        yesterday = today.minusDays(1);
        
        // Create test users
        usuario1 = new Usuario(1L, "Juan Pérez", "juan@example.com", "Activo");
        usuario2 = new Usuario(2L, "María García", "maria@example.com", "Activo");
        
        // Create test books
        libro1 = new Libro(1L, "1234567890", "El Quijote", "Miguel de Cervantes", "Prestado");
        libro2 = new Libro(2L, "0987654321", "Cien años de soledad", "Gabriel García Márquez", "Prestado");
        
        // Create test loans
        prestamo1 = new Prestamo(1L, libro1, usuario1, yesterday, tomorrow);
        prestamo2 = new Prestamo(2L, libro2, usuario2, today, yesterday); // Overdue loan
    }

    @Test
    void buscarPorId_withExistingId_shouldReturnLoan() {
        // Arrange
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo1));
        
        // Act
        Prestamo result = prestamoService.buscarPorId(1L);
        
        // Assert
        assertEquals(prestamo1, result);
        verify(prestamoRepository).findById(1L);
    }

    @Test
    void buscarPorId_withNonExistingId_shouldThrowException() {
        // Arrange
        when(prestamoRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            prestamoService.buscarPorId(999L);
        });
        
        assertEquals("Prestamo no encontrado con ID: 999", exception.getMessage());
        verify(prestamoRepository).findById(999L);
    }

    @Test
    void obtenerTodos_shouldReturnAllLoans() {
        // Arrange
        List<Prestamo> prestamos = Arrays.asList(prestamo1, prestamo2);
        when(prestamoRepository.findAll()).thenReturn(prestamos);
        
        // Act
        List<Prestamo> result = prestamoService.obtenerTodos();
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(prestamo1));
        assertTrue(result.contains(prestamo2));
        verify(prestamoRepository).findAll();
    }

    @Test
    void buscarPorUsuario_shouldReturnLoansForUser() {
        // Arrange
        List<Prestamo> prestamosUsuario1 = Arrays.asList(prestamo1);
        when(prestamoRepository.findByUsuario(usuario1)).thenReturn(prestamosUsuario1);
        
        // Act
        List<Prestamo> result = prestamoService.buscarPorUsuario(usuario1);
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(prestamo1, result.get(0));
        verify(prestamoRepository).findByUsuario(usuario1);
    }

    @Test
    void buscarPorLibro_shouldReturnLoansForBook() {
        // Arrange
        List<Prestamo> prestamosLibro1 = Arrays.asList(prestamo1);
        when(prestamoRepository.findByLibro(libro1)).thenReturn(prestamosLibro1);
        
        // Act
        List<Prestamo> result = prestamoService.buscarPorLibro(libro1);
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(prestamo1, result.get(0));
        verify(prestamoRepository).findByLibro(libro1);
    }

    @Test
    void buscarPrestamosActivos_shouldReturnActiveLoans() {
        // Arrange
        List<Prestamo> prestamosActivos = Arrays.asList(prestamo1);
        when(prestamoRepository.findActivePrestamos()).thenReturn(prestamosActivos);
        
        // Act
        List<Prestamo> result = prestamoService.buscarPrestamosActivos();
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(prestamo1, result.get(0));
        verify(prestamoRepository).findActivePrestamos();
    }

    @Test
    void buscarPrestamosVencidos_shouldReturnOverdueLoans() {
        // Arrange
        List<Prestamo> prestamosVencidos = Arrays.asList(prestamo2);
        when(prestamoRepository.findOverduePrestamos()).thenReturn(prestamosVencidos);
        
        // Act
        List<Prestamo> result = prestamoService.buscarPrestamosVencidos();
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(prestamo2, result.get(0));
        verify(prestamoRepository).findOverduePrestamos();
    }

    @Test
    void guardar_shouldSaveAndReturnLoan() {
        // Arrange
        Libro libro3 = new Libro(3L, "5555555555", "Nuevo Libro", "Nuevo Autor", "Prestado");
        Usuario usuario3 = new Usuario(3L, "Nuevo Usuario", "nuevo@example.com", "Activo");
        Prestamo nuevoPrestamo = new Prestamo(null, libro3, usuario3, today, tomorrow);
        Prestamo prestamoGuardado = new Prestamo(3L, libro3, usuario3, today, tomorrow);
        
        when(prestamoRepository.save(nuevoPrestamo)).thenReturn(prestamoGuardado);
        
        // Act
        Prestamo result = prestamoService.guardar(nuevoPrestamo);
        
        // Assert
        assertEquals(prestamoGuardado, result);
        verify(prestamoRepository).save(nuevoPrestamo);
    }

    @Test
    void eliminar_shouldCallRepositoryDeleteById() {
        // Act
        prestamoService.eliminar(1L);
        
        // Assert
        verify(prestamoRepository).deleteById(1L);
    }

    @Test
    void buscarYActualizar_withExistingId_shouldUpdateAndReturnLoan() {
        // Arrange
        LocalDate newFechaDevolucion = today.plusDays(7);
        Prestamo prestamoActualizado = new Prestamo(null, libro1, usuario1, yesterday, newFechaDevolucion);
        Prestamo prestamoGuardado = new Prestamo(1L, libro1, usuario1, yesterday, newFechaDevolucion);
        
        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(prestamo1));
        when(prestamoRepository.save(any(Prestamo.class))).thenReturn(prestamoGuardado);
        
        // Act
        Prestamo result = prestamoService.buscarYActualizar(1L, prestamoActualizado);
        
        // Assert
        assertEquals(prestamoGuardado, result);
        verify(prestamoRepository).findById(1L);
        verify(prestamoRepository).save(any(Prestamo.class));
    }

    @Test
    void buscarYActualizar_withNonExistingId_shouldThrowException() {
        // Arrange
        Prestamo prestamoActualizado = new Prestamo(null, libro1, usuario1, yesterday, tomorrow);
        
        when(prestamoRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            prestamoService.buscarYActualizar(999L, prestamoActualizado);
        });
        
        assertEquals("Prestamo no encontrado con ID: 999", exception.getMessage());
        verify(prestamoRepository).findById(999L);
        verify(prestamoRepository, never()).save(any(Prestamo.class));
    }
}