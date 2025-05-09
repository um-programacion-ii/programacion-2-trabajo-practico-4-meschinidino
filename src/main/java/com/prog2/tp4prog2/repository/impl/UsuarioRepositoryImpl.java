package com.prog2.tp4prog2.repository.impl;

import com.prog2.tp4prog2.model.Usuario;
import com.prog2.tp4prog2.repository.UsuarioRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of UsuarioRepository using in-memory storage
 */
@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {
    private final Map<Long, Usuario> usuarios = new HashMap<>();
    private Long nextId = 1L;
    
    @Override
    public List<Usuario> findAll() {
        return new ArrayList<>(usuarios.values());
    }
    
    @Override
    public Optional<Usuario> findById(Long id) {
        return Optional.ofNullable(usuarios.get(id));
    }
    
    @Override
    public Usuario save(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(nextId++);
        }
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }
    
    @Override
    public void deleteById(Long id) {
        usuarios.remove(id);
    }
    
    @Override
    public List<Usuario> findByNombre(String nombre) {
        return usuarios.values().stream()
            .filter(usuario -> usuario.getNombre().contains(nombre))
            .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarios.values().stream()
            .filter(usuario -> usuario.getEmail().equals(email))
            .findFirst();
    }
    
    @Override
    public List<Usuario> findByEstado(String estado) {
        return usuarios.values().stream()
            .filter(usuario -> usuario.getEstado().equals(estado))
            .collect(Collectors.toList());
    }
}