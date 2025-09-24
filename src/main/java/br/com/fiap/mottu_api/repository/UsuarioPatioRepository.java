package br.com.fiap.mottu_api.repository;

import br.com.fiap.mottu_api.model.UsuarioPatio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioPatioRepository extends JpaRepository<UsuarioPatio, Long> {

    Optional<UsuarioPatio> findByEmail(String email);

    Optional<UsuarioPatio> findByEmailAndSenha(String email, String senha);

    Optional<UsuarioPatio> findByCpf(String cpf);
}