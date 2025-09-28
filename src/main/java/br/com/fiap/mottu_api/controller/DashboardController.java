package br.com.fiap.mottu_api.controller;

import br.com.fiap.mottu_api.model.Moto;
import br.com.fiap.mottu_api.model.Patio;
import br.com.fiap.mottu_api.model.UsuarioPatio;
import br.com.fiap.mottu_api.service.MotoService;
import br.com.fiap.mottu_api.service.PatioService;
import br.com.fiap.mottu_api.service.UsuarioPatioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final UsuarioPatioService usuarioPatioService;
    private final MotoService motoService;
    private final PatioService patioService;

    public DashboardController(UsuarioPatioService usuarioPatioService, MotoService motoService,
            PatioService patioService) {
        this.usuarioPatioService = usuarioPatioService;
        this.motoService = motoService;
        this.patioService = patioService;
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

            // Buscar dados reais
            List<Moto> todasMotos = motoService.listarTodos();
            List<Patio> todosPatios = patioService.listarTodos();

            // Calcular estatísticas por setor
            long setorA = todasMotos.stream().filter(m -> "Setor A".equals(m.getSetor())).count();
            long setorB = todasMotos.stream().filter(m -> "Setor B".equals(m.getSetor())).count();
            long setorC = todasMotos.stream().filter(m -> "Setor C".equals(m.getSetor())).count();
            long setorF = todasMotos.stream().filter(m -> "Setor F".equals(m.getSetor())).count();

            // Calcular estatísticas por status
            long disponiveis = todasMotos.stream().filter(m -> m.getStatus().name().equals("DISPONIVEL")).count();
            long manutencao = todasMotos.stream().filter(m -> m.getStatus().name().equals("MANUTENCAO")).count();
            long reservadas = todasMotos.stream().filter(m -> m.getStatus().name().equals("RESERVADA")).count();
            long problemas = todasMotos.stream().filter(
                    m -> m.getStatus().name().equals("DANOS_ESTRUTURAIS") || m.getStatus().name().equals("SINISTRO"))
                    .count();

            // Dashboard com dados reais
            model.addAttribute("usuario", usuario);
            model.addAttribute("role", usuario.getRole() != null ? usuario.getRole().name() : "USER");
            model.addAttribute("totalMotos", todasMotos.size());
            model.addAttribute("totalPatios", todosPatios.size());
            model.addAttribute("motosRecentes", todasMotos.stream().limit(5).toList());
            model.addAttribute("patiosRecentes", todosPatios.stream().limit(5).toList());

            // Estatísticas por setor
            model.addAttribute("setorA", setorA);
            model.addAttribute("setorB", setorB);
            model.addAttribute("setorC", setorC);
            model.addAttribute("setorF", setorF);

            // Estatísticas por status
            model.addAttribute("disponiveis", disponiveis);
            model.addAttribute("manutencao", manutencao);
            model.addAttribute("reservadas", reservadas);
            model.addAttribute("problemas", problemas);

            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar dashboard: " + e.getMessage());
            return "redirect:/login?error=true";
        }
    }
}
