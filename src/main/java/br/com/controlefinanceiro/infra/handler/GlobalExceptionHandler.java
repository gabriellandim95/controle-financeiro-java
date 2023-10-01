package br.com.controlefinanceiro.infra.handler;

import br.com.controlefinanceiro.dto.DadosErrorResponse;
import br.com.controlefinanceiro.infra.exceptions.RegistroNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegistroNotFoundException.class)
    public ResponseEntity<DadosErrorResponse> handlerRegistroNotFoundException(RegistroNotFoundException ex){
        DadosErrorResponse dadosErrorResponse = new DadosErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dadosErrorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<DadosErrorResponse> handlerForbiddenStatus(RuntimeException ex){
        DadosErrorResponse dadosErrorResponse = new DadosErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dadosErrorResponse);
    }
}
