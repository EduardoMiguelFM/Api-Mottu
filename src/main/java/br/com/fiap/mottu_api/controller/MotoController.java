package br.com.fiap.mottu_api.controller;

import br.com.fiap.mottu_api.dto.MotoDTO;
import br.com.fiap.mottu_api.dto.MotoResponseDTO;
import br.com.fiap.mottu_api.model.Moto;
import br.com.fiap.mottu_api.model.StatusMoto;
import br.com.fiap.mottu_api.service.MotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/motos")
@Tag(name = "Motos", description = "Endpoints para gestão de motos")
public class MotoController {

    private final MotoService motoService;

    public MotoController(MotoService motoService) {
        this.motoService = motoService;
    }

    @GetMapping("/todos")
    @Operation(summary = "Listar todas as motos", description = "Retorna uma lista com todas as motos cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de motos retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    public ResponseEntity<List<MotoResponseDTO>> listarTodos() {
        List<Moto> motos = motoService.listarTodos();
        List<MotoResponseDTO> dtos = motos.stream().map(motoService::toResponseDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Moto> buscarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(motoService.buscarPorId(id));
    }

    @GetMapping("/placa/{placa}")
    public ResponseEntity<Moto> buscarPorPlaca(@PathVariable("placa") String placa) {
        return ResponseEntity.ok(motoService.buscarPorPlaca(placa));
    }

    @GetMapping("/status")
    public ResponseEntity<List<Moto>> buscarPorStatus(@RequestParam("status") StatusMoto status) {
        return ResponseEntity.ok(motoService.buscarPorStatus(status));
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<Moto>> filtrarPorStatusSetorCor(
            @RequestParam(value = "status", required = false) StatusMoto status,
            @RequestParam(value = "setor", required = false) String setor,
            @RequestParam(value = "cor", required = false) String cor) {
        return ResponseEntity.ok(motoService.filtrarPorStatusSetorCor(status, setor, cor));
    }

    @PostMapping
    @Operation(summary = "Cadastrar nova moto", description = "Cadastra uma nova moto no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Moto cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ResponseEntity<MotoResponseDTO> salvar(@RequestBody @Valid MotoDTO dto) {
        Moto moto = motoService.salvar(dto);
        return ResponseEntity.status(201).body(motoService.toResponseDTO(moto));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Moto> atualizarPorId(@PathVariable("id") Long id, @RequestBody @Valid MotoDTO dto) {
        return ResponseEntity.ok(motoService.atualizar(id, dto));
    }

    @PutMapping("/placa/{placa}")
    public ResponseEntity<Moto> atualizarPorPlaca(@PathVariable("placa") String placa,
            @RequestBody @Valid MotoDTO dto) {
        return ResponseEntity.ok(motoService.atualizarPorPlaca(placa, dto));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable("id") Long id) {
        motoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/placa/{placa}")
    public ResponseEntity<Void> deletarPorPlaca(@PathVariable("placa") String placa) {
        motoService.deletarPorPlaca(placa);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patio/setor/{setor}/contagem")
    public ResponseEntity<Map<String, Object>> contarPorSetor(@PathVariable("setor") String setor) {
        long quantidade = motoService.contarMotosPorSetor(setor);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "DISPONIVEL");
        response.put("quantidade", quantidade);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patio/moto/{placa}/status")
    public ResponseEntity<Map<String, Object>> statusPorPlaca(@PathVariable("placa") String placa) {
        return ResponseEntity.ok(motoService.obterStatusPorPlaca(placa));
    }
}