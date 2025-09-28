package br.com.fiap.mottu_api.controller;

import br.com.fiap.mottu_api.model.Moto;
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
    public String editarFormulario(@PathVariable Long id, Model model) {
        try {
            Moto moto = motoService.buscarPorId(id);
            System.out.println("DEBUG: Moto encontrada - ID: " + moto.getId() + ", Descrição: " + moto.getDescricao());
            model.addAttribute("moto", moto);
            model.addAttribute("patios", patioService.listarTodos());
            model.addAttribute("statusList", StatusMoto.values());
            model.addAttribute("role", "USER"); // Valor padrão
            return "motos/formulario";
        } catch (Exception e) {
            System.out.println("DEBUG: Erro ao carregar moto: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Erro ao carregar formulário de edição: " + e.getMessage());
            return "redirect:/motos";
        }
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
            moto.setSetor(obterSetorPorStatus(status));
            moto.setCorSetor(obterCorPorStatus(status));

            // Gerar descrição baseada no status se não houver descrição
            if (moto.getDescricao() == null || moto.getDescricao().trim().isEmpty()) {
                moto.setDescricao(gerarDescricaoPorStatus(status, moto.getModelo()));
            }
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
