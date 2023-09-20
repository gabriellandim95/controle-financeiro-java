package br.com.controlefinanceiro.infra.handler;

import br.com.controlefinanceiro.infra.exceptions.CarteiraNotFoundException;
import br.com.controlefinanceiro.infra.exceptions.ContaNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CarteiraNotFoundException.class)
    public ResponseEntity<String> handlerCarteiraNotFoundException(CarteiraNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ContaNotFoundException.class)
    public ResponseEntity<String> handlerContaNotFoundException(ContaNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
