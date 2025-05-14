package br.com.fiap.mottu_api.service;

import br.com.fiap.mottu_api.dto.MotoDTO;
import br.com.fiap.mottu_api.model.Moto;
import br.com.fiap.mottu_api.model.Patio;
import br.com.fiap.mottu_api.model.StatusMoto;
import br.com.fiap.mottu_api.repository.MotoRepository;
import br.com.fiap.mottu_api.repository.PatioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MotoService {

    private final MotoRepository motoRepository;
    private final PatioRepository patioRepository;

    @Cacheable("motos")
    public Page<Moto> listar(StatusMoto status, Pageable pageable) {
        return motoRepository.findByStatus(status, pageable);
    }

    public List<Moto> buscarPorStatus(StatusMoto status) {
        return motoRepository.findAllByStatus(status);
    }

    public Moto buscarPorId(Long id) {
        return motoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada por ID"));
    }

    public Moto buscarPorPlaca(String placa) {
        return motoRepository.findByPlaca(placa)
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada por placa"));
    }

    public Moto salvar(MotoDTO dto) {
        Patio patio = patioRepository.findById(dto.getPatioId())
                .orElseThrow(() -> new EntityNotFoundException("Pátio não encontrado"));
        Moto moto = new Moto(null, dto.getModelo(), dto.getPlaca(), dto.getStatus(), dto.getCoordenadaGps(), dto.getDataUltimaManutencao(), dto.getDescricaoProblema(), patio);
        return motoRepository.save(moto);
    }

    public Moto atualizar(Long id, MotoDTO dto) {
        Moto moto = buscarPorId(id);
        Patio patio = patioRepository.findById(dto.getPatioId())
                .orElseThrow(() -> new EntityNotFoundException("Pátio não encontrado"));

        moto.setModelo(dto.getModelo());
        moto.setPlaca(dto.getPlaca());
        moto.setStatus(dto.getStatus());
        moto.setCoordenadaGps(dto.getCoordenadaGps());
        moto.setDataUltimaManutencao(dto.getDataUltimaManutencao());
        moto.setDescricaoProblema(dto.getDescricaoProblema());
        moto.setPatio(patio);
        return motoRepository.save(moto);
    }

    public Moto atualizarPorPlaca(String placa, MotoDTO dto) {
        Moto moto = buscarPorPlaca(placa);
        Patio patio = patioRepository.findById(dto.getPatioId())
                .orElseThrow(() -> new EntityNotFoundException("Pátio não encontrado"));

        moto.setModelo(dto.getModelo());
        moto.setPlaca(dto.getPlaca());
        moto.setStatus(dto.getStatus());
        moto.setCoordenadaGps(dto.getCoordenadaGps());
        moto.setDataUltimaManutencao(dto.getDataUltimaManutencao());
        moto.setDescricaoProblema(dto.getDescricaoProblema());
        moto.setPatio(patio);
        return motoRepository.save(moto);
    }

    public void deletar(Long id) {
        if (!motoRepository.existsById(id)) {
            throw new EntityNotFoundException("Moto não encontrada para exclusão por ID");
        }
        motoRepository.deleteById(id);
    }

    public void deletarPorPlaca(String placa) {
        Moto moto = buscarPorPlaca(placa);
        motoRepository.deleteByPlaca(moto.getPlaca());
    }
}
