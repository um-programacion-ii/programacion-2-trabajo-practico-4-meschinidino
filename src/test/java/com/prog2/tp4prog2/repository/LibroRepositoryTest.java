package com.prog2.tp4prog2.repository;

import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.repository.impl.LibroRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class LibroRepositoryTest {

    private LibroRepository libroRepository;
    private Libro libro1;
    private Libro libro2;

    @BeforeEach
    void setUp() {
        // Create a new repository instance for each test
        libroRepository = new LibroRepositoryImpl();
        
        // Create test books
        libro1 = new Libro(null, "1234567890", "El Quijote", "Miguel de Cervantes", "Disponible");
        libro2 = new Libro(null, "0987654321", "Cien años de soledad", "Gabriel García Márquez", "Disponible");
        
        // Save the books to the repository
        libroRepository.save(libro1);
        libroRepository.save(libro2);
    }

    @Test
    void findAll_shouldReturnAllBooks() {
        // Act
        List<Libro> result = libroRepository.findAll();
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(libro1));
        assertTrue(result.contains(libro2));
    }

    @Test
    void findById_withExistingId_shouldReturnBook() {
        // Act
        Optional<Libro> result = libroRepository.findById(libro1.getId());
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(libro1, result.get());
    }

    @Test
    void findById_withNonExistingId_shouldReturnEmpty() {
        // Act
        Optional<Libro> result = libroRepository.findById(999L);
        
        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void save_withNewBook_shouldAssignIdAndSaveBook() {
        // Arrange
        Libro newLibro = new Libro(null, "5555555555", "Nuevo Libro", "Nuevo Autor", "Disponible");
        
        // Act
        Libro result = libroRepository.save(newLibro);
        
        // Assert
        assertNotNull(result.getId());
        assertEquals(newLibro.getIsbn(), result.getIsbn());
        assertEquals(newLibro.getTitulo(), result.getTitulo());
        assertEquals(newLibro.getAutor(), result.getAutor());
        assertEquals(newLibro.getEstado(), result.getEstado());
        
        // Verify it was actually saved
        Optional<Libro> savedLibro = libroRepository.findById(result.getId());
        assertTrue(savedLibro.isPresent());
    }

    @Test
    void save_withExistingBook_shouldUpdateBook() {
        // Arrange
        libro1.setTitulo("Título Actualizado");
        libro1.setAutor("Autor Actualizado");
        
        // Act
        Libro result = libroRepository.save(libro1);
        
        // Assert
        assertEquals(libro1.getId(), result.getId());
        assertEquals("Título Actualizado", result.getTitulo());
        assertEquals("Autor Actualizado", result.getAutor());
        
        // Verify it was actually updated
        Optional<Libro> updatedLibro = libroRepository.findById(libro1.getId());
        assertTrue(updatedLibro.isPresent());
        assertEquals("Título Actualizado", updatedLibro.get().getTitulo());
    }

    @Test
    void deleteById_withExistingId_shouldRemoveBook() {
        // Act
        libroRepository.deleteById(libro1.getId());
        
        // Assert
        Optional<Libro> result = libroRepository.findById(libro1.getId());
        assertFalse(result.isPresent());
        assertEquals(1, libroRepository.findAll().size());
    }

    @Test
    void findByTitulo_withExistingTitle_shouldReturnMatchingBooks() {
        // Act
        List<Libro> result = libroRepository.findByTitulo("Quijote");
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(libro1, result.get(0));
    }

    @Test
    void findByTitulo_withNonExistingTitle_shouldReturnEmptyList() {
        // Act
        List<Libro> result = libroRepository.findByTitulo("No existe");
        
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByAutor_withExistingAuthor_shouldReturnMatchingBooks() {
        // Act
        List<Libro> result = libroRepository.findByAutor("Cervantes");
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(libro1, result.get(0));
    }

    @Test
    void findByAutor_withNonExistingAuthor_shouldReturnEmptyList() {
        // Act
        List<Libro> result = libroRepository.findByAutor("No existe");
        
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByIsbn_withExistingIsbn_shouldReturnBook() {
        // Act
        Optional<Libro> result = libroRepository.findByIsbn("1234567890");
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(libro1, result.get());
    }

    @Test
    void findByIsbn_withNonExistingIsbn_shouldReturnEmpty() {
        // Act
        Optional<Libro> result = libroRepository.findByIsbn("9999999999");
        
        // Assert
        assertFalse(result.isPresent());
    }
}