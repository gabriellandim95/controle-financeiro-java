package br.com.controlefinanceiro.controller;

import br.com.controlefinanceiro.dto.DadosAutenticacao;
import br.com.controlefinanceiro.dto.DadosTokenJWT;
import br.com.controlefinanceiro.enums.TipoLogEvento;
import br.com.controlefinanceiro.model.Usuario;
import br.com.controlefinanceiro.service.LogAcessoService;
import br.com.controlefinanceiro.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticaction;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private LogAcessoService logAcessoService;

    @Operation(summary = "Efetuar login.", method = "POST")
    @PostMapping
    public ResponseEntity login(@RequestBody @Valid DadosAutenticacao dados){
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        Authentication auth =  authenticaction.authenticate(authToken);
        String token = tokenService.gerarToken((Usuario) auth.getPrincipal());

        logAcessoService.gerarEvento(dados.login(), "Acessou o sistema.", TipoLogEvento.ACESSO_AO_SISTEMA);

        return ResponseEntity.ok(new DadosTokenJWT(token));
    }
}
