package br.com.controlefinanceiro.infra.handler;

import br.com.controlefinanceiro.infra.exceptions.RegistroNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegistroNotFoundException.class)
    public ResponseEntity<String> handlerCarteiraNotFoundException(RegistroNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
