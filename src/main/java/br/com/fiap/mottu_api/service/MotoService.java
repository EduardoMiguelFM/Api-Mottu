package br.com.fiap.mottu_api.service;

import br.com.fiap.mottu_api.dto.MotoDTO;
import br.com.fiap.mottu_api.model.Moto;
import br.com.fiap.mottu_api.model.Patio;
import br.com.fiap.mottu_api.model.StatusMoto;
import br.com.fiap.mottu_api.repository.MotoRepository;
import br.com.fiap.mottu_api.repository.PatioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class MotoService {

    private final MotoRepository motoRepository;
    private final PatioRepository patioRepository;

    public MotoService(MotoRepository motoRepository, PatioRepository patioRepository) {
        this.motoRepository = motoRepository;
        this.patioRepository = patioRepository;
    }

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

    private String gerarCoordenadaGpsSimulada() {
        double baseLat = -23.5505;
        double baseLng = -46.6333;
        Random random = new Random();
        double offsetLat = (random.nextDouble() - 0.5) / 1000;
        double offsetLng = (random.nextDouble() - 0.5) / 1000;
        return String.format("%.6f, %.6f", baseLat + offsetLat, baseLng + offsetLng);
    }

    private String definirSetorPorStatus(StatusMoto status) {
        return switch (status) {
            case DISPONIVEL -> "Setor A";
            case RESERVADA -> "Setor B";
            case MANUTENCAO -> "Setor C";
            case FALTA_PECA -> "Setor D";
            case INDISPONIVEL -> "Setor E";
            case DANOS_ESTRUTURAIS -> "Setor F";
            case SINISTRO -> "Setor G";
        };
    }

    private String definirCorPorStatus(StatusMoto status) {
        return switch (status) {
            case DISPONIVEL -> "Verde";
            case RESERVADA -> "Azul";
            case MANUTENCAO -> "Amarelo";
            case FALTA_PECA -> "Laranja";
            case INDISPONIVEL -> "Cinza";
            case DANOS_ESTRUTURAIS -> "Vermelho";
            case SINISTRO -> "Preto";
        };
    }

    public List<Moto> filtrarPorStatusSetorCor(StatusMoto status, String setor, String cor) {
        return motoRepository.findAll().stream()
                .filter(m -> (status == null || m.getStatus() == status))
                .filter(m -> (setor == null || m.getSetor().equalsIgnoreCase(setor)))
                .filter(m -> (cor == null || m.getCorSetor().equalsIgnoreCase(cor)))
                .toList();
    }

    public Moto salvar(MotoDTO dto) {
        Patio patio = patioRepository.findById(dto.getPatioId())
                .orElseThrow(() -> new EntityNotFoundException("Pátio não encontrado"));

        String gps = (dto.getCoordenadaGps() == null || dto.getCoordenadaGps().isBlank())
                ? gerarCoordenadaGpsSimulada() : dto.getCoordenadaGps();

        Moto moto = new Moto(null, dto.getModelo(), dto.getPlaca(), dto.getStatus(), gps,
                definirSetorPorStatus(dto.getStatus()), definirCorPorStatus(dto.getStatus()), patio);
        return motoRepository.save(moto);
    }

    public Moto atualizar(Long id, MotoDTO dto) {
        Moto moto = buscarPorId(id);
        Patio patio = patioRepository.findById(dto.getPatioId())
                .orElseThrow(() -> new EntityNotFoundException("Pátio não encontrado"));

        moto.setModelo(dto.getModelo());
        moto.setPlaca(dto.getPlaca());
        moto.setStatus(dto.getStatus());
        moto.setCoordenadaGps((dto.getCoordenadaGps() == null || dto.getCoordenadaGps().isBlank())
                ? gerarCoordenadaGpsSimulada() : dto.getCoordenadaGps());
        moto.setSetor(definirSetorPorStatus(dto.getStatus()));
        moto.setCorSetor(definirCorPorStatus(dto.getStatus()));
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
        moto.setCoordenadaGps((dto.getCoordenadaGps() == null || dto.getCoordenadaGps().isBlank())
                ? gerarCoordenadaGpsSimulada() : dto.getCoordenadaGps());
        moto.setSetor(definirSetorPorStatus(dto.getStatus()));
        moto.setCorSetor(definirCorPorStatus(dto.getStatus()));
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