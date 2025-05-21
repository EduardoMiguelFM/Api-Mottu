package br.com.fiap.mottu_api.controller;

import br.com.fiap.mottu_api.dto.MotoDTO;
import br.com.fiap.mottu_api.dto.MotoResponseDTO;
import br.com.fiap.mottu_api.model.Moto;
import br.com.fiap.mottu_api.model.StatusMoto;
import br.com.fiap.mottu_api.service.MotoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motos")
public class MotoController {

    private final MotoService motoService;

    public MotoController(MotoService motoService) {
        this.motoService = motoService;
    }

    @GetMapping("/todos")
    public ResponseEntity<List<MotoResponseDTO>> listarTodos() {
        List<Moto> motos = motoService.listarTodos();
        List<MotoResponseDTO> dtos = motos.stream()
                .map(motoService::toResponseDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }



    @GetMapping("/id/{id}")
    public ResponseEntity<Moto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(motoService.buscarPorId(id));
    }

    @GetMapping("/placa/{placa}")
    public ResponseEntity<Moto> buscarPorPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(motoService.buscarPorPlaca(placa));
    }

    @GetMapping("/status")
    public ResponseEntity<List<Moto>> buscarPorStatus(@RequestParam StatusMoto status) {
        return ResponseEntity.ok(motoService.buscarPorStatus(status));
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<Moto>> filtrarPorStatusSetorCor(
            @RequestParam(required = false) StatusMoto status,
            @RequestParam(required = false) String setor,
            @RequestParam(required = false) String cor) {
        return ResponseEntity.ok(motoService.filtrarPorStatusSetorCor(status, setor, cor));
    }

    @PostMapping
    public ResponseEntity<MotoResponseDTO> salvar(@RequestBody @Valid MotoDTO dto) {
        Moto moto = motoService.salvar(dto);
        return ResponseEntity.status(201).body(motoService.toResponseDTO(moto));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Moto> atualizarPorId(@PathVariable Long id, @RequestBody @Valid MotoDTO dto) {
        return ResponseEntity.ok(motoService.atualizar(id, dto));
    }

    @PutMapping("/placa/{placa}")
    public ResponseEntity<Moto> atualizarPorPlaca(@PathVariable String placa, @RequestBody @Valid MotoDTO dto) {
        return ResponseEntity.ok(motoService.atualizarPorPlaca(placa, dto));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        motoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/placa/{placa}")
    public ResponseEntity<Void> deletarPorPlaca(@PathVariable String placa) {
        motoService.deletarPorPlaca(placa);
        return ResponseEntity.noContent().build();
    }
}