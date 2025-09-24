package br.com.fiap.mottu_api.controller;

import br.com.fiap.mottu_api.model.Moto;
import br.com.fiap.mottu_api.model.Patio;
import br.com.fiap.mottu_api.model.StatusMoto;
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
@RequestMapping("/motos")
public class MotoWebController {

    private final MotoService motoService;
    private final PatioService patioService;

    public MotoWebController(MotoService motoService, PatioService patioService) {
        this.motoService = motoService;
        this.patioService = patioService;
    }

    @GetMapping
    public String listarMotos(@RequestParam(required = false) String status,
            @RequestParam(required = false) String setor,
            @RequestParam(required = false) String cor,
            Model model) {
        List<Moto> motos;

        if (status != null || setor != null || cor != null) {
            StatusMoto statusEnum = status != null ? StatusMoto.valueOf(status) : null;
            motos = motoService.filtrarPorStatusSetorCor(statusEnum, setor, cor);
        } else {
            motos = motoService.listarTodos();
        }

        model.addAttribute("motos", motos);
        model.addAttribute("statusList", StatusMoto.values());
        model.addAttribute("filtroStatus", status);
        model.addAttribute("filtroSetor", setor);
        model.addAttribute("filtroCor", cor);

        return "motos/lista";
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("moto", new Moto());
        model.addAttribute("patios", patioService.listarTodos());
        model.addAttribute("statusList", StatusMoto.values());
        return "motos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable Long id, Model model) {
        Moto moto = motoService.buscarPorId(id);
        model.addAttribute("moto", moto);
        model.addAttribute("patios", patioService.listarTodos());
        model.addAttribute("statusList", StatusMoto.values());
        return "motos/formulario";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute Moto moto, BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "motos/formulario";
        }

        try {
            // Aplicar lógica de setor e cor baseado no status
            aplicarLogicaSetorCor(moto);

            motoService.salvar(moto);
            redirectAttributes.addFlashAttribute("success", "Moto salva com sucesso!");
            return "redirect:/motos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao salvar moto: " + e.getMessage());
            return "redirect:/motos/novo";
        }
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute Moto moto,
            BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "motos/formulario";
        }

        try {
            // Aplicar lógica de setor e cor baseado no status
            aplicarLogicaSetorCor(moto);

            motoService.atualizar(id, moto);
            redirectAttributes.addFlashAttribute("success", "Moto atualizada com sucesso!");
            return "redirect:/motos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar moto: " + e.getMessage());
            return "redirect:/motos/editar/" + id;
        }
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            motoService.deletar(id);
            redirectAttributes.addFlashAttribute("success", "Moto deletada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao deletar moto: " + e.getMessage());
        }
        return "redirect:/motos";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        Moto moto = motoService.buscarPorId(id);
        model.addAttribute("moto", moto);
        return "motos/detalhes";
    }

    private void aplicarLogicaSetorCor(Moto moto) {
        StatusMoto status = moto.getStatus();
        if (status != null) {
            moto.setSetor(obterSetorPorStatus(status));
            moto.setCorSetor(obterCorPorStatus(status));
        }
    }

    private String obterSetorPorStatus(StatusMoto status) {
        return switch (status) {
            case DISPONIVEL -> "Setor A";
            case RESERVADA -> "Setor B";
            case MANUTENCAO -> "Setor C";
            case FALTA_PECA -> "Setor D";
            case INDISPONIVEL -> "Setor E";
            case DANOS_ESTRUTURAIS -> "Setor F";
            case SINISTRO -> "Setor G";
        };
    }

    private String obterCorPorStatus(StatusMoto status) {
        return switch (status) {
            case DISPONIVEL -> "Verde";
            case RESERVADA -> "Azul";
            case MANUTENCAO -> "Amarelo";
            case FALTA_PECA -> "Laranja";
            case INDISPONIVEL -> "Cinza";
            case DANOS_ESTRUTURAIS -> "Vermelho";
            case SINISTRO -> "Preto";
        };
    }
}
