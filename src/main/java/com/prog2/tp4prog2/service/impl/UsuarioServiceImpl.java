package com.prog2.tp4prog2.service.impl;

import com.prog2.tp4prog2.model.Usuario;
import com.prog2.tp4prog2.repository.UsuarioRepository;
import com.prog2.tp4prog2.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario buscarYActualizar(Long id, Usuario usuario) {
        Usuario existingUsuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Update the existing user with the provided data
        if (usuario.getNombre() != null) {
            existingUsuario.setNombre(usuario.getNombre());
        }
        if (usuario.getEmail() != null) {
            existingUsuario.setEmail(usuario.getEmail());
        }
        if (usuario.getEstado() != null) {
            existingUsuario.setEstado(usuario.getEstado());
        }

        // Save the updated user
        return usuarioRepository.save(existingUsuario);
    }
}
