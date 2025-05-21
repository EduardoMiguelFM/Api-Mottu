package br.com.fiap.mottu_api.service;

import br.com.fiap.mottu_api.model.UsuarioPatio;
import br.com.fiap.mottu_api.repository.UsuarioPatioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class UsuarioPatioService {

    private final UsuarioPatioRepository usuarioRepository;

    public UsuarioPatioService(UsuarioPatioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioPatio> listarTodos() {
        return usuarioRepository.findAll();
    }

    public UsuarioPatio salvar(UsuarioPatio usuario) {
        return usuarioRepository.save(usuario);
    }

    public UsuarioPatio buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public void deletar(Long id) {
        UsuarioPatio usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}
