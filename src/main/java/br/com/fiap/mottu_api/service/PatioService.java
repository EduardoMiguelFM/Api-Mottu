package br.com.fiap.mottu_api.service;

import br.com.fiap.mottu_api.model.Patio;
import br.com.fiap.mottu_api.repository.PatioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatioService {

    private final PatioRepository patioRepository;

    public PatioService(PatioRepository patioRepository) {
        this.patioRepository = patioRepository;
    }

    public List<Patio> listarTodos() {
        return patioRepository.findAll();
    }

    public Patio salvar(Patio patio) {
        return patioRepository.save(patio);
    }

    public Patio buscarPorId(Long id) {
        return patioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pátio não encontrado"));
    }

    public void deletar(Long id) {
        Patio patio = buscarPorId(id);
        patioRepository.delete(patio);
    }
}