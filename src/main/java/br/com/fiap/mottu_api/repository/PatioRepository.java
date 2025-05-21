package br.com.fiap.mottu_api.repository;

import br.com.fiap.mottu_api.model.Patio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatioRepository extends JpaRepository<Patio, Long> {
    Optional<Patio> findByNome(String nome);
}
