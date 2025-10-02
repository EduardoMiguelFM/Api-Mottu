package br.com.fiap.mottu_api.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String requestUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        // Se a requisição foi para /error e não há status de erro, redirecionar para
        // login
        if (status == null && requestUri != null && requestUri.contains("/error")) {
            return "redirect:/login";
        }

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorCode", "404");
                model.addAttribute("errorMessage", "Página não encontrada");
                model.addAttribute("errorDescription", "A página que você está procurando não existe.");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errorCode", "500");
                model.addAttribute("errorMessage", "Erro interno do servidor");
                model.addAttribute("errorDescription", "Ocorreu um erro interno. Tente novamente mais tarde.");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("errorCode", "403");
                model.addAttribute("errorMessage", "Acesso negado");
                model.addAttribute("errorDescription", "Você não tem permissão para acessar esta página.");
            } else if (statusCode == HttpStatus.OK.value()) {
                // Status 200 (OK) não é um erro, redirecionar para login
                return "redirect:/login";
            } else {
                model.addAttribute("errorCode", statusCode.toString());
                model.addAttribute("errorMessage", "Erro " + statusCode);
                model.addAttribute("errorDescription", "Ocorreu um erro inesperado.");
            }
        } else {
            // Se não há status de erro, redirecionar para login
            return "redirect:/login";
        }

        return "error";
    }
}