package br.com.fiap.mottu_api.repository;

import br.com.fiap.mottu_api.model.Patio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatioRepository extends JpaRepository<Patio, Long> {
}
