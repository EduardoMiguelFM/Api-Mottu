package br.com.fiap.mottu_api.controller;

import br.com.fiap.mottu_api.model.UsuarioPatio;
import br.com.fiap.mottu_api.service.UsuarioPatioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação mobile")
public class AuthController {

    private final UsuarioPatioService usuarioPatioService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioPatioService usuarioPatioService, PasswordEncoder passwordEncoder) {
        this.usuarioPatioService = usuarioPatioService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @Operation(summary = "Login mobile", description = "Autentica usuário para aplicação mobile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            UsuarioPatio usuario = usuarioPatioService.buscarPorEmail(request.getEmail());

            if (usuario != null && passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Login realizado com sucesso");
                response.put("user", Map.of(
                        "id", usuario.getId(),
                        "nome", usuario.getNome(),
                        "email", usuario.getEmail(),
                        "role", usuario.getRole(),
                        "funcao", usuario.getFuncao(),
                        "ativo", usuario.getAtivo()));
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Email ou senha incorretos");
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro interno do servidor");
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/validate")
    @Operation(summary = "Validar token", description = "Valida se o usuário está autenticado")
    public ResponseEntity<Map<String, Object>> validate() {
        Map<String, Object> response = new HashMap<>();
        response.put("valid", true);
        response.put("message", "Token válido");
        return ResponseEntity.ok(response);
    }

    // Classe interna para request de login
    public static class LoginRequest {
        private String email;
        private String senha;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }
    }
}
