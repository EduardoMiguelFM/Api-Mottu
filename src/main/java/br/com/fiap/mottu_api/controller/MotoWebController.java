package br.com.fiap.mottu_api.controller;

import br.com.fiap.mottu_api.model.Moto;
import br.com.fiap.mottu_api.model.Patio;
import br.com.fiap.mottu_api.model.StatusMoto;
import br.com.fiap.mottu_api.service.MotoService;
import br.com.fiap.mottu_api.service.PatioService;
import jakarta.persistence.EntityNotFoundException;
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
    public String listarMotos(@RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "setor", required = false) String setor,
            @RequestParam(value = "cor", required = false) String cor,
            Model model) {
        List<Moto> motos;

        if (status != null || setor != null || cor != null) {
            StatusMoto statusEnum = null;
            if (status != null && !status.isEmpty()) {
                try {
                    statusEnum = StatusMoto.valueOf(status);
                } catch (IllegalArgumentException e) {
                    // Se o status não for válido, ignora o filtro
                    statusEnum = null;
                }
            }
            motos = motoService.filtrarPorStatusSetorCor(statusEnum, setor, cor);
        } else {
            motos = motoService.listarTodos();
        }

        model.addAttribute("motos", motos);
        model.addAttribute("statusList", StatusMoto.values());
        model.addAttribute("setorList",
                java.util.Arrays.asList("Setor A", "Setor B", "Setor C", "Setor D", "Setor E", "Setor F", "Setor G"));
        model.addAttribute("corList",
                java.util.Arrays.asList("Verde", "Azul", "Amarelo", "Laranja", "Cinza", "Vermelho", "Preto"));
        model.addAttribute("filtroStatus", status);
        model.addAttribute("filtroSetor", setor);
        model.addAttribute("filtroCor", cor);
        model.addAttribute("role", "USER"); // Valor padrão

        return "motos/lista";
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("moto", new Moto());
        model.addAttribute("patios", patioService.listarTodos());
        model.addAttribute("statusList", StatusMoto.values());
        model.addAttribute("role", "USER"); // Valor padrão
        return "motos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Moto moto = motoService.buscarPorId(id);

            // Verificar se a moto foi encontrada
            if (moto == null) {
                redirectAttributes.addFlashAttribute("error", "Moto não encontrada");
                return "redirect:/motos";
            }

            // Garantir que descrição não seja null
            if (moto.getDescricao() == null) {
                moto.setDescricao("");
            }

            // Garantir que setor e corSetor não sejam null
            if (moto.getSetor() == null) {
                moto.setSetor("");
            }
            if (moto.getCorSetor() == null) {
                moto.setCorSetor("");
            }

            // Verificar se o patio está carregado
            if (moto.getPatio() == null) {
                redirectAttributes.addFlashAttribute("error", "Pátio associado à moto não encontrado");
                return "redirect:/motos";
            }

            // Garantir que a lista de pátios existe
            List<Patio> patios = patioService.listarTodos();
            if (patios == null) {
                patios = new java.util.ArrayList<>();
            }

            model.addAttribute("moto", moto);
            model.addAttribute("patios", patios);
            model.addAttribute("statusList", StatusMoto.values());
            model.addAttribute("role", "USER");

            return "motos/formulario";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Moto não encontrada com ID: " + id);
            return "redirect:/motos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao carregar formulário de edição: " + e.getMessage());
            return "redirect:/motos";
        }
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute Moto moto, BindingResult result,
            RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patios", patioService.listarTodos());
            model.addAttribute("statusList", StatusMoto.values());
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

    @PostMapping({ "/deletar/{id}", "/{id}/delete" })
    public String deletar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            motoService.deletar(id);
            redirectAttributes.addFlashAttribute("success", "Moto deletada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao deletar moto: " + e.getMessage());
        }
        return "redirect:/motos";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable("id") Long id,
            @RequestParam(value = "patio.id", required = false) Long patioId,
            @Valid @ModelAttribute Moto moto,
            BindingResult result, RedirectAttributes redirectAttributes, Model model) {

        // Se o patio.id foi enviado via request param, carregar o Patio
        if (patioId != null && (moto.getPatio() == null || moto.getPatio().getId() == null)) {
            try {
                Patio patio = patioService.buscarPorId(patioId);
                moto.setPatio(patio);
            } catch (Exception e) {
                result.rejectValue("patio", "error.patio", "Pátio não encontrado");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("moto", moto);
            model.addAttribute("patios", patioService.listarTodos());
            model.addAttribute("statusList", StatusMoto.values());
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

    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable("id") Long id, Model model) {
        try {
            Moto moto = motoService.buscarPorId(id);
            model.addAttribute("moto", moto);
            model.addAttribute("role", "USER"); // Valor padrão
            return "motos/detalhes";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar detalhes da moto: " + e.getMessage());
            return "redirect:/motos";
        }
    }

    private void aplicarLogicaSetorCor(Moto moto) {
        StatusMoto status = moto.getStatus();
        if (status != null) {
            // Usa métodos do Service para evitar duplicação de código
            moto.setSetor(motoService.obterSetorPorStatus(status));
            moto.setCorSetor(motoService.obterCorPorStatus(status));

            // Gerar descrição baseada no status se não houver descrição
            if (moto.getDescricao() == null || moto.getDescricao().trim().isEmpty()) {
                moto.setDescricao(gerarDescricaoPorStatus(status, moto.getModelo()));
            }
        }
    }

    private String gerarDescricaoPorStatus(StatusMoto status, String modelo) {
        String modeloInfo = modelo != null ? modelo + " " : "Moto ";

        return switch (status) {
            case DISPONIVEL ->
                modeloInfo + "em perfeito estado de funcionamento, revisada e pronta para entrega ao cliente";
            case RESERVADA ->
                modeloInfo + "reservada para cliente, aguardando retirada. Verificar documentação antes da entrega";
            case MANUTENCAO ->
                modeloInfo + "em manutenção preventiva/corretiva. Aguardando finalização dos serviços técnicos";
            case FALTA_PECA ->
                modeloInfo + "aguardando peças de reposição. Serviço de manutenção interrompido temporariamente";
            case INDISPONIVEL ->
                modeloInfo + "temporariamente indisponível para uso. Aguardando liberação técnica ou administrativa";
            case DANOS_ESTRUTURAIS ->
                modeloInfo + "com danos estruturais identificados. Necessária avaliação técnica detalhada";
            case SINISTRO -> modeloInfo + "envolvida em sinistro. Aguardando perícia e definição de responsabilidades";
        };
    }

}
