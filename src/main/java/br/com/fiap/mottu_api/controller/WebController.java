package br.com.fiap.mottu_api.controller;

import br.com.fiap.mottu_api.model.UsuarioPatio;
import br.com.fiap.mottu_api.service.UsuarioPatioService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    private final UsuarioPatioService usuarioPatioService;

    public WebController(UsuarioPatioService usuarioPatioService) {
        this.usuarioPatioService = usuarioPatioService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("usuario", new UsuarioPatio());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String processarCadastro(@Valid UsuarioPatio usuario, BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "cadastro";
        }

        try {
            usuarioPatioService.salvar(usuario);
            redirectAttributes.addFlashAttribute("success",
                    "Usuário cadastrado com sucesso! Faça login para continuar.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao cadastrar usuário: " + e.getMessage());
            return "redirect:/cadastro";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UsuarioPatio usuario = usuarioPatioService.buscarPorEmail(email);

        model.addAttribute("usuario", usuario);
        model.addAttribute("role", usuario.getRole().name());

        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout=true";
    }
}
