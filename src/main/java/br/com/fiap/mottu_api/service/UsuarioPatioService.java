package br.com.fiap.mottu_api.service;

import br.com.fiap.mottu_api.model.Role;
import br.com.fiap.mottu_api.model.UsuarioPatio;
import br.com.fiap.mottu_api.repository.UsuarioPatioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UsuarioPatioService implements UserDetailsService {

    private final UsuarioPatioRepository usuarioPatioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioPatioService(UsuarioPatioRepository usuarioPatioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioPatioRepository = usuarioPatioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsuarioPatio usuario = usuarioPatioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        if (!usuario.getAtivo()) {
            throw new UsernameNotFoundException("Usuário inativo: " + email);
        }

        return new User(
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getAtivo(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                getAuthorities(usuario.getRole()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    public UsuarioPatio salvar(UsuarioPatio usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioPatioRepository.save(usuario);
    }

    public UsuarioPatio buscarPorEmail(String email) {
        return usuarioPatioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }

    public List<UsuarioPatio> listarTodos() {
        return usuarioPatioRepository.findAll();
    }

    public UsuarioPatio buscarPorId(Long id) {
        return usuarioPatioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
    }

    public void deletar(Long id) {
        usuarioPatioRepository.deleteById(id);
    }
}