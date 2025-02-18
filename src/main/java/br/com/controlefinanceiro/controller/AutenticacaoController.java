package br.com.controlefinanceiro.controller;

import br.com.controlefinanceiro.dto.DadosAutenticacao;
import br.com.controlefinanceiro.dto.DadosTokenJWT;
import br.com.controlefinanceiro.interfaces.AutenticacaoService;
import br.com.controlefinanceiro.service.AutenticacaoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/autenticacao")
@RequiredArgsConstructor
public class AutenticacaoController {
    /**
     * API de autenticação em desenvolvimento, esta classa deixara de existir.
     * ***/

    private final AutenticacaoService autenticacaoService;

    @Operation(summary = "Efetuar login.", method = "POST")
    @PostMapping(value = "/login")
    public ResponseEntity<DadosTokenJWT> login(@RequestBody @Valid DadosAutenticacao dados){
        return autenticacaoService.login(dados);
    }

    @Operation(summary = "Efetuar logout.", method = "POST")
    @PostMapping(value = "/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String authorizationHeader){
        return autenticacaoService.logout(authorizationHeader);
    }
}
