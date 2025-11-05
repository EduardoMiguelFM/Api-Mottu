package br.com.fiap.mottu_api.repository;

import br.com.fiap.mottu_api.model.Moto;
import br.com.fiap.mottu_api.model.StatusMoto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MotoRepository extends JpaRepository<Moto, Long> {
    Page<Moto> findByStatus(StatusMoto status, Pageable pageable);

    Optional<Moto> findByPlaca(String placa);

    List<Moto> findAllByStatus(StatusMoto status);

    void deleteByPlaca(String placa);

    @Query("SELECT m FROM Moto m JOIN FETCH m.patio WHERE m.id = :id")
    Optional<Moto> findByIdWithPatio(@Param("id") Long id);
}