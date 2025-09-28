package br.com.fiap.mottu_api.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Object handleGeneral(Exception ex, WebRequest request) {
        // Verifica se é uma requisição para API (contém /api/ no path)
        String requestPath = request.getDescription(false);
        if (requestPath.contains("/api/")) {
            // Retorna JSON para APIs REST
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + ex.getMessage());
        } else {
            // Retorna página de erro para páginas web
            ModelAndView modelAndView = new ModelAndView("error");
            modelAndView.addObject("errorCode", "500");
            modelAndView.addObject("errorMessage", "Erro interno do servidor");
            modelAndView.addObject("errorDescription", "Ocorreu um erro interno. Tente novamente mais tarde.");
            return modelAndView;
        }
    }
}