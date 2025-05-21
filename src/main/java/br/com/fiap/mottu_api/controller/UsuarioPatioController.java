package br.com.fiap.mottu_api.controller;

import br.com.fiap.mottu_api.model.UsuarioPatio;
import br.com.fiap.mottu_api.service.UsuarioPatioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioPatioController {

    private final UsuarioPatioService usuarioService;

    public UsuarioPatioController(UsuarioPatioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioPatio>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<UsuarioPatio> salvar(@RequestBody @Valid UsuarioPatio usuario) {
        return ResponseEntity.status(201).body(usuarioService.salvar(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioPatio> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
