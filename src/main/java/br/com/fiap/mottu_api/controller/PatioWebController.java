package br.com.fiap.mottu_api.controller;

import br.com.fiap.mottu_api.model.Moto;
import br.com.fiap.mottu_api.model.Patio;
import br.com.fiap.mottu_api.service.MotoService;
import br.com.fiap.mottu_api.service.PatioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/patios")
public class PatioWebController {

    private final PatioService patioService;
    private final MotoService motoService;

    public PatioWebController(PatioService patioService, MotoService motoService) {
        this.patioService = patioService;
        this.motoService = motoService;
    }

    @GetMapping
    public String listarPatios(Model model) {
        List<Patio> patios = patioService.listarTodos();
        model.addAttribute("patios", patios);
        model.addAttribute("role", "USER"); // Valor padrão
        return "patios/lista";
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("patio", new Patio());
        model.addAttribute("role", "USER"); // Valor padrão
        return "patios/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable Long id, Model model) {
        try {
            Patio patio = patioService.buscarPorId(id);
            model.addAttribute("patio", patio);
            model.addAttribute("role", "USER"); // Valor padrão
            return "patios/formulario";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar formulário de edição: " + e.getMessage());
            return "redirect:/patios";
        }
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute Patio patio, BindingResult result,
            RedirectAttributes redirectAttributes, Model model) {
        System.out.println(
                "DEBUG: Tentando salvar pátio - Nome: " + patio.getNome() + ", Endereço: " + patio.getEndereco());

        if (result.hasErrors()) {
            System.out.println("DEBUG: Erros de validação: " + result.getAllErrors());
            return "patios/formulario";
        }

        try {
            patioService.salvar(patio);
            redirectAttributes.addFlashAttribute("success", "Pátio salvo com sucesso!");
            return "redirect:/patios";
        } catch (Exception e) {
            System.out.println("DEBUG: Erro ao salvar pátio: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar pátio: " + e.getMessage());
            return "redirect:/patios/novo";
        }
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute Patio patio,
            BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "patios/formulario";
        }

        try {
            patioService.atualizar(id, patio);
            redirectAttributes.addFlashAttribute("success", "Pátio atualizado com sucesso!");
            return "redirect:/patios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar pátio: " + e.getMessage());
            return "redirect:/patios/editar/" + id;
        }
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            patioService.deletar(id);
            redirectAttributes.addFlashAttribute("success", "Pátio deletado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao deletar pátio: " + e.getMessage());
        }
        return "redirect:/patios";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        try {
            Patio patio = patioService.buscarPorId(id);
            List<Moto> motosDoPatio = motoService.listarTodos().stream()
                    .filter(moto -> moto.getPatio() != null && moto.getPatio().getId().equals(id))
                    .toList();

            // Calcular estatísticas
            long totalMotos = motosDoPatio.size();
            long disponiveis = motosDoPatio.stream().filter(m -> m.getStatus().name().equals("DISPONIVEL")).count();
            long manutencao = motosDoPatio.stream().filter(m -> m.getStatus().name().equals("MANUTENCAO")).count();
            long problemas = motosDoPatio.stream().filter(
                    m -> m.getStatus().name().equals("DANOS_ESTRUTURAIS") || m.getStatus().name().equals("SINISTRO"))
                    .count();

            model.addAttribute("patio", patio);
            model.addAttribute("motos", motosDoPatio);
            model.addAttribute("totalMotos", totalMotos);
            model.addAttribute("disponiveis", disponiveis);
            model.addAttribute("manutencao", manutencao);
            model.addAttribute("problemas", problemas);
            model.addAttribute("role", "USER"); // Valor padrão
            return "patios/detalhes";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar detalhes do pátio: " + e.getMessage());
            return "redirect:/patios";
        }
    }

    @PostMapping("/{id}/delete")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            patioService.excluir(id);
            redirectAttributes.addFlashAttribute("success", "Pátio excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao excluir pátio: " + e.getMessage());
        }
        return "redirect:/patios";
    }
}
