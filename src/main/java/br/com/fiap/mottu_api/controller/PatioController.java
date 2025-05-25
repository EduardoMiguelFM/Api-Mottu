package br.com.fiap.mottu_api.controller;

import br.com.fiap.mottu_api.model.Patio;
import br.com.fiap.mottu_api.service.PatioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patios")
public class PatioController {

    private final PatioService patioService;

    public PatioController(PatioService patioService) {
        this.patioService = patioService;
    }

    @GetMapping
    public ResponseEntity<List<Patio>> listarTodos() {
        return ResponseEntity.ok(patioService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<Patio> salvar(@RequestBody @Valid Patio patio) {
        return ResponseEntity.status(201).body(patioService.salvar(patio));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Long>> statusGeral() {
        return ResponseEntity.ok(patioService.statusGeralPatio());
    }
}