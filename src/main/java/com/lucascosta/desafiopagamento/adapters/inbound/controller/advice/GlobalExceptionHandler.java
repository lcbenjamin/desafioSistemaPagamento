package com.lucascosta.desafiopagamento.adapters.inbound.controller.advice;

import com.lucascosta.desafiopagamento.core.domain.exceptions.ExternalTransferUnauthorizedException;
import com.lucascosta.desafiopagamento.core.domain.exceptions.UserNotFoundException;
import com.lucascosta.desafiopagamento.core.domain.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ProblemDetail> handleMethodValidationException(ValidationException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        pd.setTitle("Erro de validação da requisição");
        pd.setType(URI.create("urn:problem-type:validation-error"));
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("code", "UNPROCESSABLE_ENTITY");
        log.warn("Validation error at {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.unprocessableEntity().body(pd);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleMethodUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Usuário ou entidade não encontrado");
        pd.setType(URI.create("urn:problem-type:notfound-error"));
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("code", "NOT_FOUND");
        log.warn("Not found error at {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ProblemDetail> handleMethodIllegalStateException(IllegalStateException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        pd.setTitle("Erro interno do servidor");
        pd.setType(URI.create("urn:problem-type:internal-server-error"));
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("code", "INTERNAL_SERVER_ERROR");
        log.error("Internal server error at {}: {}", request.getRequestURI(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Erro de validação da requisição");
        pd.setDetail("Um ou mais campos estão inválidos. Corrija e tente novamente.");
        pd.setType(URI.create("urn:problem-type:validation-error"));
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty("code", "BAD_REQUEST");
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        pd.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(ExternalTransferUnauthorizedException.class)
    public ResponseEntity<ProblemDetail> handleExternalTransferUnauthorizedException(ExternalTransferUnauthorizedException ex, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        pd.setTitle("Transferência não autorizada");
        pd.setType(URI.create("urn:problem-type:forbidden-error"));
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("timestamp", Instant.now().toString());
        pd.setProperty("code", "FORBIDDEN");
        log.warn("Forbidden error at {}: {}", request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(pd);
    }

}
