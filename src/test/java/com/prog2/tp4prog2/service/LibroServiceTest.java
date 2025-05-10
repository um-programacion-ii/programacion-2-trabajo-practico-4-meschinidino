package com.prog2.tp4prog2.service;

import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.repository.LibroRepository;
import com.prog2.tp4prog2.service.impl.LibroServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    private LibroService libroService;
    private Libro libro1;
    private Libro libro2;

    @BeforeEach
    void setUp() {
        // Initialize the service with the mocked repository
        libroService = new LibroServiceImpl(libroRepository);
        
        // Create test books
        libro1 = new Libro(1L, "1234567890", "El Quijote", "Miguel de Cervantes", "Disponible");
        libro2 = new Libro(2L, "0987654321", "Cien años de soledad", "Gabriel García Márquez", "Disponible");
    }

    @Test
    void buscarPorIsbn_withExistingIsbn_shouldReturnBook() {
        // Arrange
        when(libroRepository.findByIsbn("1234567890")).thenReturn(Optional.of(libro1));
        
        // Act
        Libro result = libroService.buscarPorIsbn("1234567890");
        
        // Assert
        assertEquals(libro1, result);
        verify(libroRepository).findByIsbn("1234567890");
    }

    @Test
    void buscarPorIsbn_withNonExistingIsbn_shouldThrowException() {
        // Arrange
        when(libroRepository.findByIsbn("9999999999")).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            libroService.buscarPorIsbn("9999999999");
        });
        
        assertEquals("Libro no encontrado con ISBN: 9999999999", exception.getMessage());
        verify(libroRepository).findByIsbn("9999999999");
    }

    @Test
    void buscarPorId_withExistingId_shouldReturnBook() {
        // Arrange
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro1));
        
        // Act
        Libro result = libroService.buscarPorId(1L);
        
        // Assert
        assertEquals(libro1, result);
        verify(libroRepository).findById(1L);
    }

    @Test
    void buscarPorId_withNonExistingId_shouldThrowException() {
        // Arrange
        when(libroRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            libroService.buscarPorId(999L);
        });
        
        assertEquals("Libro no encontrado con ID: 999", exception.getMessage());
        verify(libroRepository).findById(999L);
    }

    @Test
    void obtenerTodos_shouldReturnAllBooks() {
        // Arrange
        List<Libro> libros = Arrays.asList(libro1, libro2);
        when(libroRepository.findAll()).thenReturn(libros);
        
        // Act
        List<Libro> result = libroService.obtenerTodos();
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(libro1));
        assertTrue(result.contains(libro2));
        verify(libroRepository).findAll();
    }

    @Test
    void guardar_shouldSaveAndReturnBook() {
        // Arrange
        Libro nuevoLibro = new Libro(null, "5555555555", "Nuevo Libro", "Nuevo Autor", "Disponible");
        Libro libroGuardado = new Libro(3L, "5555555555", "Nuevo Libro", "Nuevo Autor", "Disponible");
        
        when(libroRepository.save(nuevoLibro)).thenReturn(libroGuardado);
        
        // Act
        Libro result = libroService.guardar(nuevoLibro);
        
        // Assert
        assertEquals(libroGuardado, result);
        verify(libroRepository).save(nuevoLibro);
    }

    @Test
    void eliminar_shouldCallRepositoryDeleteById() {
        // Act
        libroService.eliminar(1L);
        
        // Assert
        verify(libroRepository).deleteById(1L);
    }

    @Test
    void buscarYActualizar_withExistingId_shouldUpdateAndReturnBook() {
        // Arrange
        Libro libroActualizado = new Libro(null, "1234567890", "Título Actualizado", "Autor Actualizado", "Disponible");
        Libro libroGuardado = new Libro(1L, "1234567890", "Título Actualizado", "Autor Actualizado", "Disponible");
        
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro1));
        when(libroRepository.save(any(Libro.class))).thenReturn(libroGuardado);
        
        // Act
        Libro result = libroService.buscarYActualizar(1L, libroActualizado);
        
        // Assert
        assertEquals(libroGuardado, result);
        verify(libroRepository).findById(1L);
        verify(libroRepository).save(any(Libro.class));
    }

    @Test
    void buscarYActualizar_withNonExistingId_shouldThrowException() {
        // Arrange
        Libro libroActualizado = new Libro(null, "1234567890", "Título Actualizado", "Autor Actualizado", "Disponible");
        
        when(libroRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            libroService.buscarYActualizar(999L, libroActualizado);
        });
        
        assertEquals("Libro no encontrado con ID: 999", exception.getMessage());
        verify(libroRepository).findById(999L);
        verify(libroRepository, never()).save(any(Libro.class));
    }
}