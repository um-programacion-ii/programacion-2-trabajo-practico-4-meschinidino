package com.prog2.tp4prog2.service;

import com.prog2.tp4prog2.model.Usuario;
import com.prog2.tp4prog2.repository.UsuarioRepository;
import com.prog2.tp4prog2.service.impl.UsuarioServiceImpl;
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
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioService usuarioService;
    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        // Initialize the service with the mocked repository
        usuarioService = new UsuarioServiceImpl(usuarioRepository);
        
        // Create test users
        usuario1 = new Usuario(1L, "Juan Pérez", "juan@example.com", "Activo");
        usuario2 = new Usuario(2L, "María García", "maria@example.com", "Activo");
    }

    @Test
    void buscarPorEmail_withExistingEmail_shouldReturnUser() {
        // Arrange
        when(usuarioRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(usuario1));
        
        // Act
        Usuario result = usuarioService.buscarPorEmail("juan@example.com");
        
        // Assert
        assertEquals(usuario1, result);
        verify(usuarioRepository).findByEmail("juan@example.com");
    }

    @Test
    void buscarPorEmail_withNonExistingEmail_shouldThrowException() {
        // Arrange
        when(usuarioRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.buscarPorEmail("noexiste@example.com");
        });
        
        assertEquals("Usuario no encontrado con email: noexiste@example.com", exception.getMessage());
        verify(usuarioRepository).findByEmail("noexiste@example.com");
    }

    @Test
    void buscarPorId_withExistingId_shouldReturnUser() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario1));
        
        // Act
        Usuario result = usuarioService.buscarPorId(1L);
        
        // Assert
        assertEquals(usuario1, result);
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void buscarPorId_withNonExistingId_shouldThrowException() {
        // Arrange
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.buscarPorId(999L);
        });
        
        assertEquals("Usuario no encontrado con ID: 999", exception.getMessage());
        verify(usuarioRepository).findById(999L);
    }

    @Test
    void obtenerTodos_shouldReturnAllUsers() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        
        // Act
        List<Usuario> result = usuarioService.obtenerTodos();
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(usuario1));
        assertTrue(result.contains(usuario2));
        verify(usuarioRepository).findAll();
    }

    @Test
    void guardar_shouldSaveAndReturnUser() {
        // Arrange
        Usuario nuevoUsuario = new Usuario(null, "Nuevo Usuario", "nuevo@example.com", "Activo");
        Usuario usuarioGuardado = new Usuario(3L, "Nuevo Usuario", "nuevo@example.com", "Activo");
        
        when(usuarioRepository.save(nuevoUsuario)).thenReturn(usuarioGuardado);
        
        // Act
        Usuario result = usuarioService.guardar(nuevoUsuario);
        
        // Assert
        assertEquals(usuarioGuardado, result);
        verify(usuarioRepository).save(nuevoUsuario);
    }

    @Test
    void eliminar_shouldCallRepositoryDeleteById() {
        // Act
        usuarioService.eliminar(1L);
        
        // Assert
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void buscarYActualizar_withExistingId_shouldUpdateAndReturnUser() {
        // Arrange
        Usuario usuarioActualizado = new Usuario(null, "Nombre Actualizado", "actualizado@example.com", "Activo");
        Usuario usuarioGuardado = new Usuario(1L, "Nombre Actualizado", "actualizado@example.com", "Activo");
        
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario1));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);
        
        // Act
        Usuario result = usuarioService.buscarYActualizar(1L, usuarioActualizado);
        
        // Assert
        assertEquals(usuarioGuardado, result);
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void buscarYActualizar_withNonExistingId_shouldThrowException() {
        // Arrange
        Usuario usuarioActualizado = new Usuario(null, "Nombre Actualizado", "actualizado@example.com", "Activo");
        
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.buscarYActualizar(999L, usuarioActualizado);
        });
        
        assertEquals("Usuario no encontrado con ID: 999", exception.getMessage());
        verify(usuarioRepository).findById(999L);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}