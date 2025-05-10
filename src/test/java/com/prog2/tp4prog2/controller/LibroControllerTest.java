package com.prog2.tp4prog2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prog2.tp4prog2.model.Libro;
import com.prog2.tp4prog2.service.LibroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest(controllers = LibroController.class)
public class LibroControllerTest {

    @MockBean
    private LibroService libroService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private Libro libro1;
    private Libro libro2;

    @BeforeEach
    void setUp() {
        // Create test books
        libro1 = new Libro(1L, "1234567890", "El Quijote", "Miguel de Cervantes", "Disponible");
        libro2 = new Libro(2L, "0987654321", "Cien años de soledad", "Gabriel García Márquez", "Disponible");
    }

    @Test
    void obtenerTodos_shouldReturnAllBooks() throws Exception {
        // Arrange
        List<Libro> libros = Arrays.asList(libro1, libro2);
        when(libroService.obtenerTodos()).thenReturn(libros);

        // Act & Assert
        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].isbn", is("1234567890")))
                .andExpect(jsonPath("$[0].titulo", is("El Quijote")))
                .andExpect(jsonPath("$[0].autor", is("Miguel de Cervantes")))
                .andExpect(jsonPath("$[0].estado", is("Disponible")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].isbn", is("0987654321")))
                .andExpect(jsonPath("$[1].titulo", is("Cien años de soledad")))
                .andExpect(jsonPath("$[1].autor", is("Gabriel García Márquez")))
                .andExpect(jsonPath("$[1].estado", is("Disponible")));

        verify(libroService).obtenerTodos();
    }

    @Test
    void obtenerPorId_withExistingId_shouldReturnBook() throws Exception {
        // Arrange
        when(libroService.buscarPorId(1L)).thenReturn(libro1);

        // Act & Assert
        mockMvc.perform(get("/api/libros/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.isbn", is("1234567890")))
                .andExpect(jsonPath("$.titulo", is("El Quijote")))
                .andExpect(jsonPath("$.autor", is("Miguel de Cervantes")))
                .andExpect(jsonPath("$.estado", is("Disponible")));

        verify(libroService).buscarPorId(1L);
    }

    @Test
    void obtenerPorId_withNonExistingId_shouldReturnNotFound() throws Exception {
        // Arrange
        when(libroService.buscarPorId(999L)).thenThrow(new RuntimeException("Libro no encontrado con ID: 999"));

        // Act & Assert
        mockMvc.perform(get("/api/libros/999"))
                .andExpect(status().isNotFound());

        verify(libroService).buscarPorId(999L);
    }

    @Test
    void obtenerPorIsbn_withExistingIsbn_shouldReturnBook() throws Exception {
        // Arrange
        when(libroService.buscarPorIsbn("1234567890")).thenReturn(libro1);

        // Act & Assert
        mockMvc.perform(get("/api/libros/isbn/1234567890"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.isbn", is("1234567890")))
                .andExpect(jsonPath("$.titulo", is("El Quijote")))
                .andExpect(jsonPath("$.autor", is("Miguel de Cervantes")))
                .andExpect(jsonPath("$.estado", is("Disponible")));

        verify(libroService).buscarPorIsbn("1234567890");
    }

    @Test
    void obtenerPorIsbn_withNonExistingIsbn_shouldReturnNotFound() throws Exception {
        // Arrange
        when(libroService.buscarPorIsbn("9999999999")).thenThrow(new RuntimeException("Libro no encontrado con ISBN: 9999999999"));

        // Act & Assert
        mockMvc.perform(get("/api/libros/isbn/9999999999"))
                .andExpect(status().isNotFound());

        verify(libroService).buscarPorIsbn("9999999999");
    }

    @Test
    void crear_shouldCreateAndReturnBook() throws Exception {
        // Arrange
        Libro nuevoLibro = new Libro(null, "5555555555", "Nuevo Libro", "Nuevo Autor", "Disponible");
        Libro libroGuardado = new Libro(3L, "5555555555", "Nuevo Libro", "Nuevo Autor", "Disponible");

        when(libroService.guardar(any(Libro.class))).thenReturn(libroGuardado);

        // Act & Assert
        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoLibro)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.isbn", is("5555555555")))
                .andExpect(jsonPath("$.titulo", is("Nuevo Libro")))
                .andExpect(jsonPath("$.autor", is("Nuevo Autor")))
                .andExpect(jsonPath("$.estado", is("Disponible")));

        verify(libroService).guardar(any(Libro.class));
    }

    @Test
    void actualizar_withExistingId_shouldUpdateAndReturnBook() throws Exception {
        // Arrange
        Libro libroActualizado = new Libro(null, "1234567890", "Título Actualizado", "Autor Actualizado", "Disponible");
        Libro libroGuardado = new Libro(1L, "1234567890", "Título Actualizado", "Autor Actualizado", "Disponible");

        when(libroService.buscarYActualizar(eq(1L), any(Libro.class))).thenReturn(libroGuardado);

        // Act & Assert
        mockMvc.perform(put("/api/libros/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(libroActualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.isbn", is("1234567890")))
                .andExpect(jsonPath("$.titulo", is("Título Actualizado")))
                .andExpect(jsonPath("$.autor", is("Autor Actualizado")))
                .andExpect(jsonPath("$.estado", is("Disponible")));

        verify(libroService).buscarYActualizar(eq(1L), any(Libro.class));
    }

    @Test
    void actualizar_withNonExistingId_shouldReturnNotFound() throws Exception {
        // Arrange
        Libro libroActualizado = new Libro(null, "1234567890", "Título Actualizado", "Autor Actualizado", "Disponible");

        when(libroService.buscarYActualizar(eq(999L), any(Libro.class)))
            .thenThrow(new RuntimeException("Libro no encontrado con ID: 999"));

        // Act & Assert
        mockMvc.perform(put("/api/libros/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(libroActualizado)))
                .andExpect(status().isNotFound());

        verify(libroService).buscarYActualizar(eq(999L), any(Libro.class));
    }

    @Test
    void eliminar_withExistingId_shouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(libroService).eliminar(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/libros/1"))
                .andExpect(status().isNoContent());

        verify(libroService).eliminar(1L);
    }

    @Test
    void eliminar_withNonExistingId_shouldReturnNotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Libro no encontrado con ID: 999")).when(libroService).eliminar(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/libros/999"))
                .andExpect(status().isNotFound());

        verify(libroService).eliminar(999L);
    }
}
