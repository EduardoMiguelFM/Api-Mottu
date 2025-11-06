package br.com.fiap.mottu_api.service;

import br.com.fiap.mottu_api.model.Patio;
import br.com.fiap.mottu_api.model.StatusMoto;
import br.com.fiap.mottu_api.repository.MotoRepository;
import br.com.fiap.mottu_api.repository.PatioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PatioService {

    private final PatioRepository patioRepository;
    private final MotoRepository motoRepository;

    public PatioService(PatioRepository patioRepository, MotoRepository motoRepository) {
        this.patioRepository = patioRepository;
        this.motoRepository = motoRepository;
    }

    public List<Patio> listarTodos() {
        return patioRepository.findAll();
    }

    public Patio salvar(Patio patio) {
        return patioRepository.save(patio);
    }

    public Patio atualizar(Long id, Patio patio) {
        Patio patioExistente = buscarPorId(id);
        patioExistente.setNome(patio.getNome());
        patioExistente.setEndereco(patio.getEndereco());
        return patioRepository.save(patioExistente);
    }

    public Patio buscarPorId(Long id) {
        return patioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pátio não encontrado"));
    }

    public void deletar(Long id) {
        Patio patio = buscarPorId(id);
        patioRepository.delete(patio);
    }

    public Map<String, Long> statusGeralPatio() {
        return Arrays.stream(StatusMoto.values())
                .collect(Collectors.toMap(
                        Enum::name,
                        status -> motoRepository.findAll().stream()
                                .filter(m -> m.getStatus() == status)
                                .count()));
    }
}