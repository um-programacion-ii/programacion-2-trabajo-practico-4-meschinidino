package com.prog2.tp4prog2.repository;

import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.model.Prestamo;
import com.prog2.tp4prog2.model.Usuario;
import com.prog2.tp4prog2.repository.impl.PrestamoRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PrestamoRepositoryTest {

    private PrestamoRepository prestamoRepository;
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
        // Create a new repository instance for each test
        prestamoRepository = new PrestamoRepositoryImpl();
        
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
        prestamo1 = new Prestamo(null, libro1, usuario1, yesterday, tomorrow);
        prestamo2 = new Prestamo(null, libro2, usuario2, today, yesterday); // Overdue loan
        
        // Save the loans to the repository
        prestamoRepository.save(prestamo1);
        prestamoRepository.save(prestamo2);
    }

    @Test
    void findAll_shouldReturnAllLoans() {
        // Act
        List<Prestamo> result = prestamoRepository.findAll();
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(prestamo1));
        assertTrue(result.contains(prestamo2));
    }

    @Test
    void findById_withExistingId_shouldReturnLoan() {
        // Act
        Optional<Prestamo> result = prestamoRepository.findById(prestamo1.getId());
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(prestamo1, result.get());
    }

    @Test
    void findById_withNonExistingId_shouldReturnEmpty() {
        // Act
        Optional<Prestamo> result = prestamoRepository.findById(999L);
        
        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void save_withNewLoan_shouldAssignIdAndSaveLoan() {
        // Arrange
        Libro libro3 = new Libro(3L, "5555555555", "Nuevo Libro", "Nuevo Autor", "Prestado");
        Usuario usuario3 = new Usuario(3L, "Nuevo Usuario", "nuevo@example.com", "Activo");
        Prestamo newPrestamo = new Prestamo(null, libro3, usuario3, today, tomorrow);
        
        // Act
        Prestamo result = prestamoRepository.save(newPrestamo);
        
        // Assert
        assertNotNull(result.getId());
        assertEquals(newPrestamo.getLibro(), result.getLibro());
        assertEquals(newPrestamo.getUsuario(), result.getUsuario());
        assertEquals(newPrestamo.getFechaPrestamo(), result.getFechaPrestamo());
        assertEquals(newPrestamo.getFechaDevolucion(), result.getFechaDevolucion());
        
        // Verify it was actually saved
        Optional<Prestamo> savedPrestamo = prestamoRepository.findById(result.getId());
        assertTrue(savedPrestamo.isPresent());
    }

    @Test
    void save_withExistingLoan_shouldUpdateLoan() {
        // Arrange
        LocalDate newFechaDevolucion = today.plusDays(7);
        prestamo1.setFechaDevolucion(newFechaDevolucion);
        
        // Act
        Prestamo result = prestamoRepository.save(prestamo1);
        
        // Assert
        assertEquals(prestamo1.getId(), result.getId());
        assertEquals(newFechaDevolucion, result.getFechaDevolucion());
        
        // Verify it was actually updated
        Optional<Prestamo> updatedPrestamo = prestamoRepository.findById(prestamo1.getId());
        assertTrue(updatedPrestamo.isPresent());
        assertEquals(newFechaDevolucion, updatedPrestamo.get().getFechaDevolucion());
    }

    @Test
    void deleteById_withExistingId_shouldRemoveLoan() {
        // Act
        prestamoRepository.deleteById(prestamo1.getId());
        
        // Assert
        Optional<Prestamo> result = prestamoRepository.findById(prestamo1.getId());
        assertFalse(result.isPresent());
        assertEquals(1, prestamoRepository.findAll().size());
    }

    @Test
    void findByUsuario_shouldReturnLoansForUser() {
        // Act
        List<Prestamo> result = prestamoRepository.findByUsuario(usuario1);
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(prestamo1, result.get(0));
    }

    @Test
    void findByLibro_shouldReturnLoansForBook() {
        // Act
        List<Prestamo> result = prestamoRepository.findByLibro(libro1);
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(prestamo1, result.get(0));
    }

    @Test
    void findByFechaPrestamo_shouldReturnLoansWithMatchingDate() {
        // Act
        List<Prestamo> yesterdayLoans = prestamoRepository.findByFechaPrestamo(yesterday);
        List<Prestamo> todayLoans = prestamoRepository.findByFechaPrestamo(today);
        
        // Assert
        assertEquals(1, yesterdayLoans.size());
        assertEquals(prestamo1, yesterdayLoans.get(0));
        
        assertEquals(1, todayLoans.size());
        assertEquals(prestamo2, todayLoans.get(0));
    }

    @Test
    void findByFechaDevolucion_shouldReturnLoansWithMatchingDate() {
        // Act
        List<Prestamo> tomorrowLoans = prestamoRepository.findByFechaDevolucion(tomorrow);
        List<Prestamo> yesterdayLoans = prestamoRepository.findByFechaDevolucion(yesterday);
        
        // Assert
        assertEquals(1, tomorrowLoans.size());
        assertEquals(prestamo1, tomorrowLoans.get(0));
        
        assertEquals(1, yesterdayLoans.size());
        assertEquals(prestamo2, yesterdayLoans.get(0));
    }

    @Test
    void findActivePrestamos_shouldReturnLoansWithFutureReturnDate() {
        // Act
        List<Prestamo> result = prestamoRepository.findActivePrestamos();
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(prestamo1, result.get(0));
    }

    @Test
    void findOverduePrestamos_shouldReturnLoansWithPastReturnDate() {
        // Act
        List<Prestamo> result = prestamoRepository.findOverduePrestamos();
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(prestamo2, result.get(0));
    }
}