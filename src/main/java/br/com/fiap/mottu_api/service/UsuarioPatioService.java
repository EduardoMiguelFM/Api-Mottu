package br.com.fiap.mottu_api.service;

import br.com.fiap.mottu_api.dto.UsuarioPatioDTO;
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

    public UsuarioPatio salvar(UsuarioPatioDTO dto) {
        UsuarioPatio usuario = new UsuarioPatio();
        usuario.setNome(dto.getNome());
        usuario.setCpf(dto.getCpf());
        usuario.setFuncao(dto.getFuncao());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        return usuarioRepository.save(usuario);
    }

    public UsuarioPatio autenticar(String email, String senha) {
        return usuarioRepository.findByEmailAndSenha(email, senha)
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos"));
    }

    public List<UsuarioPatio> listarTodos() {
        return usuarioRepository.findAll();
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
