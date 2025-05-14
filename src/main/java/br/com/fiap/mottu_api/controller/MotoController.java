package br.com.fiap.mottu_api.controller;

import br.com.fiap.mottu_api.dto.MotoDTO;
import br.com.fiap.mottu_api.model.Moto;
import br.com.fiap.mottu_api.model.StatusMoto;
import br.com.fiap.mottu_api.service.MotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motos")
@RequiredArgsConstructor
public class MotoController {

    private final MotoService motoService;

    @GetMapping
    public ResponseEntity<Page<Moto>> listar(@RequestParam StatusMoto status, Pageable pageable) {
        return ResponseEntity.ok(motoService.listar(status, pageable));
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

    @PostMapping
    public ResponseEntity<Moto> salvar(@RequestBody @Valid MotoDTO dto) {
        return ResponseEntity.ok(motoService.salvar(dto));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Moto> atualizarPorId(@PathVariable Long id, @RequestBody @Valid MotoDTO dto) {
        return ResponseEntity.ok(motoService.atualizar(id, dto));
    }

    @PutMapping("/placa/{placa}")
    public ResponseEntity<Moto> atualizarPorPlaca(@PathVariable String placa, @RequestBody @Valid MotoDTO dto) {
        return ResponseEntity.ok(motoService.atualizarPorPlaca(placa, dto));
    }

    @PostMapping("/visao-computacional")
    public ResponseEntity<Moto> atualizarPorVisao(@RequestBody @Valid MotoDTO dto) {
        return ResponseEntity.ok(motoService.atualizarPorPlaca(dto.getPlaca(), dto));
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