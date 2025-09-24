package br.com.fiap.mottu_api.controller;

import br.com.fiap.mottu_api.model.Patio;
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

    public PatioWebController(PatioService patioService) {
        this.patioService = patioService;
    }

    @GetMapping
    public String listarPatios(Model model) {
        List<Patio> patios = patioService.listarTodos();
        model.addAttribute("patios", patios);
        return "patios/lista";
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("patio", new Patio());
        return "patios/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable Long id, Model model) {
        Patio patio = patioService.buscarPorId(id);
        model.addAttribute("patio", patio);
        return "patios/formulario";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute Patio patio, BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "patios/formulario";
        }

        try {
            patioService.salvar(patio);
            redirectAttributes.addFlashAttribute("success", "Pátio salvo com sucesso!");
            return "redirect:/patios";
        } catch (Exception e) {
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
        Patio patio = patioService.buscarPorId(id);
        model.addAttribute("patio", patio);
        return "patios/detalhes";
    }
}
