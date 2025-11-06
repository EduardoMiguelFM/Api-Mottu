package br.com.fiap.mottu_api.controller;

import br.com.fiap.mottu_api.model.Patio;
import br.com.fiap.mottu_api.service.PatioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patios")
@Tag(name = "Pátios", description = "Endpoints para gestão de pátios")
public class PatioController {

    private final PatioService patioService;

    public PatioController(PatioService patioService) {
        this.patioService = patioService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os pátios", description = "Retorna uma lista com todos os pátios cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pátios retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    public ResponseEntity<List<Patio>> listarTodos() {
        return ResponseEntity.ok(patioService.listarTodos());
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo pátio", description = "Cadastra um novo pátio no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pátio cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    public ResponseEntity<Patio> salvar(@RequestBody @Valid Patio patio) {
        return ResponseEntity.status(201).body(patioService.salvar(patio));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Long>> statusGeral() {
        return ResponseEntity.ok(patioService.statusGeralPatio());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patio> buscarPorId(@PathVariable("id") Long id) {
        try {
            Patio patio = patioService.buscarPorId(id);
            return ResponseEntity.ok(patio);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patio> atualizar(@PathVariable("id") Long id, @RequestBody @Valid Patio patio) {
        try {
            Patio patioAtualizado = patioService.atualizar(id, patio);
            return ResponseEntity.ok(patioAtualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
        try {
            patioService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}