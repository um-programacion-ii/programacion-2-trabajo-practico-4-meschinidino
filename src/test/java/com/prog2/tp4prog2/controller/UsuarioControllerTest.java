package com.prog2.tp4prog2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prog2.tp4prog2.model.Usuario;
import com.prog2.tp4prog2.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@Import(UsuarioControllerTest.TestConfig.class)
public class UsuarioControllerTest {

    @Configuration
    static class TestConfig {
        @Bean
        public UsuarioService usuarioService() {
            return mock(UsuarioService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioService usuarioService;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        // Create test users
        usuario1 = new Usuario(1L, "Juan Pérez", "juan@example.com", "Activo");
        usuario2 = new Usuario(2L, "María García", "maria@example.com", "Activo");
        
        // Reset the mock before each test
        reset(usuarioService);
    }

    @Test
    void obtenerTodos_shouldReturnAllUsers() throws Exception {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
        when(usuarioService.obtenerTodos()).thenReturn(usuarios);

        // Act & Assert
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$[0].email", is("juan@example.com")))
                .andExpect(jsonPath("$[0].estado", is("Activo")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("María García")))
                .andExpect(jsonPath("$[1].email", is("maria@example.com")))
                .andExpect(jsonPath("$[1].estado", is("Activo")));

        verify(usuarioService).obtenerTodos();
    }

    @Test
    void obtenerPorId_withExistingId_shouldReturnUser() throws Exception {
        // Arrange
        when(usuarioService.buscarPorId(1L)).thenReturn(usuario1);

        // Act & Assert
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$.email", is("juan@example.com")))
                .andExpect(jsonPath("$.estado", is("Activo")));

        verify(usuarioService).buscarPorId(1L);
    }

    @Test
    void obtenerPorId_withNonExistingId_shouldReturnNotFound() throws Exception {
        // Arrange
        when(usuarioService.buscarPorId(999L)).thenThrow(new RuntimeException("Usuario no encontrado con ID: 999"));

        // Act & Assert
        mockMvc.perform(get("/api/usuarios/999"))
                .andExpect(status().isNotFound());

        verify(usuarioService).buscarPorId(999L);
    }

    @Test
    void obtenerPorEmail_withExistingEmail_shouldReturnUser() throws Exception {
        // Arrange
        when(usuarioService.buscarPorEmail("juan@example.com")).thenReturn(usuario1);

        // Act & Assert
        mockMvc.perform(get("/api/usuarios/email/juan@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$.email", is("juan@example.com")))
                .andExpect(jsonPath("$.estado", is("Activo")));

        verify(usuarioService).buscarPorEmail("juan@example.com");
    }

    @Test
    void obtenerPorEmail_withNonExistingEmail_shouldReturnNotFound() throws Exception {
        // Arrange
        when(usuarioService.buscarPorEmail("noexiste@example.com")).thenThrow(new RuntimeException("Usuario no encontrado con email: noexiste@example.com"));

        // Act & Assert
        mockMvc.perform(get("/api/usuarios/email/noexiste@example.com"))
                .andExpect(status().isNotFound());

        verify(usuarioService).buscarPorEmail("noexiste@example.com");
    }

    @Test
    void crear_shouldCreateAndReturnUser() throws Exception {
        // Arrange
        Usuario nuevoUsuario = new Usuario(null, "Nuevo Usuario", "nuevo@example.com", "Activo");
        Usuario usuarioGuardado = new Usuario(3L, "Nuevo Usuario", "nuevo@example.com", "Activo");
        
        when(usuarioService.guardar(any(Usuario.class))).thenReturn(usuarioGuardado);

        // Act & Assert
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.nombre", is("Nuevo Usuario")))
                .andExpect(jsonPath("$.email", is("nuevo@example.com")))
                .andExpect(jsonPath("$.estado", is("Activo")));

        verify(usuarioService).guardar(any(Usuario.class));
    }

    @Test
    void actualizar_withExistingId_shouldUpdateAndReturnUser() throws Exception {
        // Arrange
        Usuario usuarioActualizado = new Usuario(null, "Nombre Actualizado", "actualizado@example.com", "Activo");
        Usuario usuarioGuardado = new Usuario(1L, "Nombre Actualizado", "actualizado@example.com", "Activo");
        
        when(usuarioService.buscarYActualizar(eq(1L), any(Usuario.class))).thenReturn(usuarioGuardado);

        // Act & Assert
        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Nombre Actualizado")))
                .andExpect(jsonPath("$.email", is("actualizado@example.com")))
                .andExpect(jsonPath("$.estado", is("Activo")));

        verify(usuarioService).buscarYActualizar(eq(1L), any(Usuario.class));
    }

    @Test
    void actualizar_withNonExistingId_shouldReturnNotFound() throws Exception {
        // Arrange
        Usuario usuarioActualizado = new Usuario(null, "Nombre Actualizado", "actualizado@example.com", "Activo");
        
        when(usuarioService.buscarYActualizar(eq(999L), any(Usuario.class)))
            .thenThrow(new RuntimeException("Usuario no encontrado con ID: 999"));

        // Act & Assert
        mockMvc.perform(put("/api/usuarios/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioActualizado)))
                .andExpect(status().isNotFound());

        verify(usuarioService).buscarYActualizar(eq(999L), any(Usuario.class));
    }

    @Test
    void eliminar_withExistingId_shouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(usuarioService).eliminar(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService).eliminar(1L);
    }

    @Test
    void eliminar_withNonExistingId_shouldReturnNotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Usuario no encontrado con ID: 999")).when(usuarioService).eliminar(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/usuarios/999"))
                .andExpect(status().isNotFound());

        verify(usuarioService).eliminar(999L);
    }
}