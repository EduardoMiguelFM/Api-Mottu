package br.com.fiap.mottu_api.service;

import br.com.fiap.mottu_api.dto.MotoDTO;
import br.com.fiap.mottu_api.dto.MotoResponseDTO;
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

import java.util.*;

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

    public List<Moto> listarTodos() {
        return motoRepository.findAll();
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
                .filter(m -> (setor == null || setor.isEmpty()
                        || (m.getSetor() != null && m.getSetor().equalsIgnoreCase(setor))))
                .filter(m -> (cor == null || cor.isEmpty()
                        || (m.getCorSetor() != null && m.getCorSetor().equalsIgnoreCase(cor))))
                .toList();
    }

    public Moto salvar(MotoDTO dto) {
        Patio patio = patioRepository.findByNome(dto.getNomePatio())
                .orElseThrow(() -> new EntityNotFoundException("Pátio '" + dto.getNomePatio() + "' não encontrado"));

        Moto moto = new Moto(null, dto.getModelo(), dto.getPlaca(), dto.getStatus(),
                definirSetorPorStatus(dto.getStatus()), definirCorPorStatus(dto.getStatus()), dto.getDescricao(),
                patio);

        return motoRepository.save(moto);
    }

    public Moto salvar(Moto moto) {
        return motoRepository.save(moto);
    }

    public Moto atualizar(Long id, MotoDTO dto) {
        Moto moto = buscarPorId(id);
        Patio patio = patioRepository.findByNome(dto.getNomePatio())
                .orElseThrow(() -> new EntityNotFoundException("Pátio '" + dto.getNomePatio() + "' não encontrado"));

        moto.setModelo(dto.getModelo());
        moto.setPlaca(dto.getPlaca());
        moto.setStatus(dto.getStatus());
        moto.setSetor(definirSetorPorStatus(dto.getStatus()));
        moto.setCorSetor(definirCorPorStatus(dto.getStatus()));
        moto.setPatio(patio);
        return motoRepository.save(moto);
    }

    public Moto atualizar(Long id, Moto moto) {
        Moto motoExistente = buscarPorId(id);
        motoExistente.setModelo(moto.getModelo());
        motoExistente.setPlaca(moto.getPlaca());
        motoExistente.setStatus(moto.getStatus());
        motoExistente.setSetor(moto.getSetor());
        motoExistente.setCorSetor(moto.getCorSetor());
        motoExistente.setPatio(moto.getPatio());
        return motoRepository.save(motoExistente);
    }

    public Moto atualizarPorPlaca(String placa, MotoDTO dto) {
        Moto moto = buscarPorPlaca(placa);
        Patio patio = patioRepository.findByNome(dto.getNomePatio())
                .orElseThrow(() -> new EntityNotFoundException("Pátio '" + dto.getNomePatio() + "' não encontrado"));

        moto.setModelo(dto.getModelo());
        moto.setPlaca(dto.getPlaca());
        moto.setStatus(dto.getStatus());
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

    public long contarMotosPorSetor(String setor) {
        return motoRepository.findAll().stream()
                .filter(m -> m.getSetor().equalsIgnoreCase("Setor " + setor))
                .count();
    }

    public Map<String, Object> obterStatusPorPlaca(String placa) {
        Moto moto = buscarPorPlaca(placa);
        Map<String, Object> response = new HashMap<>();
        response.put("status", moto.getStatus());
        response.put("setor", moto.getSetor().replace("Setor ", ""));
        response.put("cor", moto.getCorSetor());
        return response;
    }

    public MotoResponseDTO toResponseDTO(Moto moto) {
        MotoResponseDTO dto = new MotoResponseDTO();
        dto.setId(moto.getId());
        dto.setModelo(moto.getModelo());
        dto.setPlaca(moto.getPlaca());
        dto.setStatus(moto.getStatus());
        dto.setSetor(moto.getSetor());
        dto.setCorSetor(moto.getCorSetor());
        dto.setNomePatio(moto.getPatio().getNome());
        return dto;
    }
}