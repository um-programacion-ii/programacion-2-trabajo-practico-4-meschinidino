package com.prog2.tp4prog2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.model.Prestamo;
import com.prog2.tp4prog2.model.Usuario;
import com.prog2.tp4prog2.service.LibroService;
import com.prog2.tp4prog2.service.PrestamoService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PrestamoController.class)
@Import(PrestamoControllerTest.TestConfig.class)
public class PrestamoControllerTest {

    @Configuration
    static class TestConfig {
        @Bean
        public PrestamoService prestamoService() {
            return mock(PrestamoService.class);
        }
        
        @Bean
        public LibroService libroService() {
            return mock(LibroService.class);
        }
        
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
    private PrestamoService prestamoService;
    
    @Autowired
    private LibroService libroService;
    
    @Autowired
    private UsuarioService usuarioService;

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
        
        // Reset the mocks before each test
        reset(prestamoService, libroService, usuarioService);
    }

    @Test
    void obtenerTodos_shouldReturnAllLoans() throws Exception {
        // Arrange
        List<Prestamo> prestamos = Arrays.asList(prestamo1, prestamo2);
        when(prestamoService.obtenerTodos()).thenReturn(prestamos);

        // Act & Assert
        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].libro.id", is(1)))
                .andExpect(jsonPath("$[0].usuario.id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].libro.id", is(2)))
                .andExpect(jsonPath("$[1].usuario.id", is(2)));

        verify(prestamoService).obtenerTodos();
    }

    @Test
    void obtenerPrestamosActivos_shouldReturnActiveLoans() throws Exception {
        // Arrange
        List<Prestamo> prestamosActivos = Arrays.asList(prestamo1);
        when(prestamoService.buscarPrestamosActivos()).thenReturn(prestamosActivos);

        // Act & Assert
        mockMvc.perform(get("/api/prestamos/activos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].libro.id", is(1)))
                .andExpect(jsonPath("$[0].usuario.id", is(1)));

        verify(prestamoService).buscarPrestamosActivos();
    }

    @Test
    void obtenerPrestamosVencidos_shouldReturnOverdueLoans() throws Exception {
        // Arrange
        List<Prestamo> prestamosVencidos = Arrays.asList(prestamo2);
        when(prestamoService.buscarPrestamosVencidos()).thenReturn(prestamosVencidos);

        // Act & Assert
        mockMvc.perform(get("/api/prestamos/vencidos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].libro.id", is(2)))
                .andExpect(jsonPath("$[0].usuario.id", is(2)));

        verify(prestamoService).buscarPrestamosVencidos();
    }

    @Test
    void obtenerPrestamosPorUsuario_withExistingUser_shouldReturnUserLoans() throws Exception {
        // Arrange
        List<Prestamo> prestamosUsuario1 = Arrays.asList(prestamo1);
        when(usuarioService.buscarPorId(1L)).thenReturn(usuario1);
        when(prestamoService.buscarPorUsuario(usuario1)).thenReturn(prestamosUsuario1);

        // Act & Assert
        mockMvc.perform(get("/api/prestamos/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].libro.id", is(1)))
                .andExpect(jsonPath("$[0].usuario.id", is(1)));

        verify(usuarioService).buscarPorId(1L);
        verify(prestamoService).buscarPorUsuario(usuario1);
    }

    @Test
    void obtenerPrestamosPorUsuario_withNonExistingUser_shouldReturnNotFound() throws Exception {
        // Arrange
        when(usuarioService.buscarPorId(999L)).thenThrow(new RuntimeException("Usuario no encontrado con ID: 999"));

        // Act & Assert
        mockMvc.perform(get("/api/prestamos/usuario/999"))
                .andExpect(status().isNotFound());

        verify(usuarioService).buscarPorId(999L);
        verify(prestamoService, never()).buscarPorUsuario(any(Usuario.class));
    }

    @Test
    void obtenerPrestamosPorLibro_withExistingBook_shouldReturnBookLoans() throws Exception {
        // Arrange
        List<Prestamo> prestamosLibro1 = Arrays.asList(prestamo1);
        when(libroService.buscarPorId(1L)).thenReturn(libro1);
        when(prestamoService.buscarPorLibro(libro1)).thenReturn(prestamosLibro1);

        // Act & Assert
        mockMvc.perform(get("/api/prestamos/libro/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].libro.id", is(1)))
                .andExpect(jsonPath("$[0].usuario.id", is(1)));

        verify(libroService).buscarPorId(1L);
        verify(prestamoService).buscarPorLibro(libro1);
    }

    @Test
    void obtenerPrestamosPorLibro_withNonExistingBook_shouldReturnNotFound() throws Exception {
        // Arrange
        when(libroService.buscarPorId(999L)).thenThrow(new RuntimeException("Libro no encontrado con ID: 999"));

        // Act & Assert
        mockMvc.perform(get("/api/prestamos/libro/999"))
                .andExpect(status().isNotFound());

        verify(libroService).buscarPorId(999L);
        verify(prestamoService, never()).buscarPorLibro(any(Libro.class));
    }

    @Test
    void crear_shouldCreateAndReturnLoan() throws Exception {
        // Arrange
        Prestamo nuevoPrestamo = new Prestamo(null, libro1, usuario1, today, tomorrow);
        Prestamo prestamoGuardado = new Prestamo(3L, libro1, usuario1, today, tomorrow);
        
        when(prestamoService.guardar(any(Prestamo.class))).thenReturn(prestamoGuardado);

        // Act & Assert
        mockMvc.perform(post("/api/prestamos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoPrestamo)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.libro.id", is(1)))
                .andExpect(jsonPath("$.usuario.id", is(1)))
                .andExpect(jsonPath("$.fechaPrestamo").exists())
                .andExpect(jsonPath("$.fechaDevolucion").exists());

        verify(prestamoService).guardar(any(Prestamo.class));
    }

    @Test
    void actualizar_withExistingId_shouldUpdateAndReturnLoan() throws Exception {
        // Arrange
        LocalDate newFechaDevolucion = today.plusDays(7);
        Prestamo prestamoActualizado = new Prestamo(null, libro1, usuario1, yesterday, newFechaDevolucion);
        Prestamo prestamoGuardado = new Prestamo(1L, libro1, usuario1, yesterday, newFechaDevolucion);
        
        when(prestamoService.buscarYActualizar(eq(1L), any(Prestamo.class))).thenReturn(prestamoGuardado);

        // Act & Assert
        mockMvc.perform(put("/api/prestamos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prestamoActualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.libro.id", is(1)))
                .andExpect(jsonPath("$.usuario.id", is(1)))
                .andExpect(jsonPath("$.fechaPrestamo").exists())
                .andExpect(jsonPath("$.fechaDevolucion").exists());

        verify(prestamoService).buscarYActualizar(eq(1L), any(Prestamo.class));
    }

    @Test
    void actualizar_withNonExistingId_shouldReturnNotFound() throws Exception {
        // Arrange
        Prestamo prestamoActualizado = new Prestamo(null, libro1, usuario1, yesterday, tomorrow);
        
        when(prestamoService.buscarYActualizar(eq(999L), any(Prestamo.class)))
            .thenThrow(new RuntimeException("Prestamo no encontrado con ID: 999"));

        // Act & Assert
        mockMvc.perform(put("/api/prestamos/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prestamoActualizado)))
                .andExpect(status().isNotFound());

        verify(prestamoService).buscarYActualizar(eq(999L), any(Prestamo.class));
    }

    @Test
    void eliminar_withExistingId_shouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(prestamoService).eliminar(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/prestamos/1"))
                .andExpect(status().isNoContent());

        verify(prestamoService).eliminar(1L);
    }

    @Test
    void eliminar_withNonExistingId_shouldReturnNotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Prestamo no encontrado con ID: 999")).when(prestamoService).eliminar(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/prestamos/999"))
                .andExpect(status().isNotFound());

        verify(prestamoService).eliminar(999L);
    }
}