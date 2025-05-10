package com.prog2.tp4prog2.repository;

import com.prog2.tp4prog2.model.Usuario;
import com.prog2.tp4prog2.repository.impl.UsuarioRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioRepositoryTest {

    private UsuarioRepository usuarioRepository;
    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        // Create a new repository instance for each test
        usuarioRepository = new UsuarioRepositoryImpl();
        
        // Create test users
        usuario1 = new Usuario(null, "Juan Pérez", "juan@example.com", "Activo");
        usuario2 = new Usuario(null, "María García", "maria@example.com", "Activo");
        
        // Save the users to the repository
        usuarioRepository.save(usuario1);
        usuarioRepository.save(usuario2);
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        // Act
        List<Usuario> result = usuarioRepository.findAll();
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(usuario1));
        assertTrue(result.contains(usuario2));
    }

    @Test
    void findById_withExistingId_shouldReturnUser() {
        // Act
        Optional<Usuario> result = usuarioRepository.findById(usuario1.getId());
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(usuario1, result.get());
    }

    @Test
    void findById_withNonExistingId_shouldReturnEmpty() {
        // Act
        Optional<Usuario> result = usuarioRepository.findById(999L);
        
        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void save_withNewUser_shouldAssignIdAndSaveUser() {
        // Arrange
        Usuario newUsuario = new Usuario(null, "Nuevo Usuario", "nuevo@example.com", "Activo");
        
        // Act
        Usuario result = usuarioRepository.save(newUsuario);
        
        // Assert
        assertNotNull(result.getId());
        assertEquals(newUsuario.getNombre(), result.getNombre());
        assertEquals(newUsuario.getEmail(), result.getEmail());
        assertEquals(newUsuario.getEstado(), result.getEstado());
        
        // Verify it was actually saved
        Optional<Usuario> savedUsuario = usuarioRepository.findById(result.getId());
        assertTrue(savedUsuario.isPresent());
    }

    @Test
    void save_withExistingUser_shouldUpdateUser() {
        // Arrange
        usuario1.setNombre("Nombre Actualizado");
        usuario1.setEmail("actualizado@example.com");
        
        // Act
        Usuario result = usuarioRepository.save(usuario1);
        
        // Assert
        assertEquals(usuario1.getId(), result.getId());
        assertEquals("Nombre Actualizado", result.getNombre());
        assertEquals("actualizado@example.com", result.getEmail());
        
        // Verify it was actually updated
        Optional<Usuario> updatedUsuario = usuarioRepository.findById(usuario1.getId());
        assertTrue(updatedUsuario.isPresent());
        assertEquals("Nombre Actualizado", updatedUsuario.get().getNombre());
    }

    @Test
    void deleteById_withExistingId_shouldRemoveUser() {
        // Act
        usuarioRepository.deleteById(usuario1.getId());
        
        // Assert
        Optional<Usuario> result = usuarioRepository.findById(usuario1.getId());
        assertFalse(result.isPresent());
        assertEquals(1, usuarioRepository.findAll().size());
    }

    @Test
    void findByNombre_withExistingName_shouldReturnMatchingUsers() {
        // Act
        List<Usuario> result = usuarioRepository.findByNombre("Juan");
        
        // Assert
        assertEquals(1, result.size());
        assertEquals(usuario1, result.get(0));
    }

    @Test
    void findByNombre_withNonExistingName_shouldReturnEmptyList() {
        // Act
        List<Usuario> result = usuarioRepository.findByNombre("No existe");
        
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByEmail_withExistingEmail_shouldReturnUser() {
        // Act
        Optional<Usuario> result = usuarioRepository.findByEmail("juan@example.com");
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(usuario1, result.get());
    }

    @Test
    void findByEmail_withNonExistingEmail_shouldReturnEmpty() {
        // Act
        Optional<Usuario> result = usuarioRepository.findByEmail("noexiste@example.com");
        
        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findByEstado_withExistingStatus_shouldReturnMatchingUsers() {
        // Arrange
        usuario1.setEstado("Inactivo");
        usuarioRepository.save(usuario1);
        
        // Act
        List<Usuario> activosResult = usuarioRepository.findByEstado("Activo");
        List<Usuario> inactivosResult = usuarioRepository.findByEstado("Inactivo");
        
        // Assert
        assertEquals(1, activosResult.size());
        assertEquals(usuario2, activosResult.get(0));
        
        assertEquals(1, inactivosResult.size());
        assertEquals(usuario1, inactivosResult.get(0));
    }

    @Test
    void findByEstado_withNonExistingStatus_shouldReturnEmptyList() {
        // Act
        List<Usuario> result = usuarioRepository.findByEstado("No existe");
        
        // Assert
        assertTrue(result.isEmpty());
    }
}