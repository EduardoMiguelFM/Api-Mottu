package br.com.fiap.mottu_api.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public Object handleNotFound(EntityNotFoundException ex, WebRequest request) {
        if (isApiRequest(request)) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Recurso não encontrado");
            error.put("error", ex.getMessage());
            error.put("status", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", "Dados inválidos");

        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage()));

        error.put("errors", fieldErrors);
        error.put("status", HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", "Violação de restrições");
        error.put("error", ex.getMessage());
        error.put("status", HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        if (isApiRequest(request)) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Argumento inválido");
            error.put("error", ex.getMessage());
            error.put("status", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(error);
        }
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Object handleGeneral(Exception ex, WebRequest request) {
        if (isApiRequest(request)) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Erro interno do servidor");
            error.put("error", ex.getMessage());
            error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } else {
            ModelAndView modelAndView = new ModelAndView("error");
            modelAndView.addObject("errorCode", "500");
            modelAndView.addObject("errorMessage", "Erro interno do servidor");
            modelAndView.addObject("errorDescription", "Ocorreu um erro interno. Tente novamente mais tarde.");
            return modelAndView;
        }
    }

    private boolean isApiRequest(WebRequest request) {
        return request.getDescription(false).contains("/api/");
    }
}