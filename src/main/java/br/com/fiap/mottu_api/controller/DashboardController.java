package br.com.fiap.mottu_api.controller;

import br.com.fiap.mottu_api.model.UsuarioPatio;
import br.com.fiap.mottu_api.service.UsuarioPatioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final UsuarioPatioService usuarioPatioService;

    public DashboardController(UsuarioPatioService usuarioPatioService) {
        this.usuarioPatioService = usuarioPatioService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            if (email == null || "anonymousUser".equals(email)) {
                return "redirect:/login";
            }

            UsuarioPatio usuario = usuarioPatioService.buscarPorEmail(email);

            // Verificações de segurança
            if (usuario == null) {
                model.addAttribute("error", "Usuário não encontrado");
                return "redirect:/login?error=true";
            }

            // Dashboard simples
            model.addAttribute("usuario", usuario);
            model.addAttribute("role", usuario.getRole() != null ? usuario.getRole().name() : "USER");
            model.addAttribute("totalMotos", 0);
            model.addAttribute("totalPatios", 0);
            model.addAttribute("motosRecentes", new java.util.ArrayList<>());
            model.addAttribute("patiosRecentes", new java.util.ArrayList<>());

            return "dashboard";
        } catch (Exception e) {
            e.printStackTrace(); // Log para debug
            model.addAttribute("error", "Erro ao carregar dashboard: " + e.getMessage());
            return "redirect:/login?error=true";
        }
    }
}
