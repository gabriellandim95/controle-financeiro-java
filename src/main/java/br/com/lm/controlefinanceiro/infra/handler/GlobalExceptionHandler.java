package br.com.lm.controlefinanceiro.infra.handler;

import br.com.lm.controlefinanceiro.dto.DadosErrorResponse;
import br.com.lm.controlefinanceiro.dto.DadosValidacaoSpring;
import br.com.lm.controlefinanceiro.dto.DadosValidacaoSpringResponse;
import br.com.lm.controlefinanceiro.exceptions.*;
import com.auth0.jwt.exceptions.TokenExpiredException;
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

    @ExceptionHandler(TipoMoedaException.class)
    public ResponseEntity<DadosErrorResponse> handlerTipoMoedaException(TipoMoedaException ex){
        DadosErrorResponse dadosErrorResponse = new DadosErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dadosErrorResponse);
    }

    @ExceptionHandler(AutorException.class)
    public ResponseEntity<DadosErrorResponse> autores(AutorException ex) {
        DadosErrorResponse dadosErrorResponse = new DadosErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dadosErrorResponse);
    }

    @ExceptionHandler(ContaException.class)
    public ResponseEntity<DadosErrorResponse> handlerContaException(ContaException ex) {
        DadosErrorResponse dadosErrorResponse = new DadosErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dadosErrorResponse);
    }

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

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<DadosErrorResponse> handlerJWTVerificationException(TokenExpiredException ex){
        DadosErrorResponse dadosErrorResponse = new DadosErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dadosErrorResponse);
    }
}
