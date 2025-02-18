package br.com.controlefinanceiro.infra.handler;

import br.com.controlefinanceiro.dto.DadosErrorResponse;
import br.com.controlefinanceiro.dto.DadosValidacaoSpring;
import br.com.controlefinanceiro.dto.DadosValidacaoSpringResponse;
import br.com.controlefinanceiro.infra.exceptions.AutenticacaoException;
import br.com.controlefinanceiro.infra.exceptions.RegistroNaoEncontradoException;
import br.com.controlefinanceiro.infra.exceptions.TipoMoedaNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<DadosErrorResponse> handlerUsernameNotFoundException(AuthenticationException ex) {
        DadosErrorResponse dadosErrorResponse = new DadosErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dadosErrorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DadosValidacaoSpring> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<DadosValidacaoSpringResponse> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> new DadosValidacaoSpringResponse(erro.getField(), erro.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(new DadosValidacaoSpring(erros));
    }

    @ExceptionHandler(RegistroNaoEncontradoException.class)
    public ResponseEntity<DadosErrorResponse> handlerRegistroNotFoundException(RegistroNaoEncontradoException ex){
        DadosErrorResponse dadosErrorResponse = new DadosErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dadosErrorResponse);
    }

    @ExceptionHandler(TipoMoedaNotFoundException.class)
    public ResponseEntity<DadosErrorResponse> handlerTipoMoedaNotFoundException(TipoMoedaNotFoundException ex){
        DadosErrorResponse dadosErrorResponse = new DadosErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dadosErrorResponse);
    }

    @ExceptionHandler(AutenticacaoException.class)
    public ResponseEntity<DadosErrorResponse> handlerForbiddenStatus(AutenticacaoException ex){
        DadosErrorResponse dadosErrorResponse = new DadosErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dadosErrorResponse);
    }
}
