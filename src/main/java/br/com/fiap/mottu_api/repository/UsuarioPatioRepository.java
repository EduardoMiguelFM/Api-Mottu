package br.com.fiap.mottu_api.repository;

import br.com.fiap.mottu_api.model.UsuarioPatio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioPatioRepository extends JpaRepository<UsuarioPatio, Long> {
}