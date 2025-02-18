package br.com.controlefinanceiro.interfaces;

import br.com.controlefinanceiro.dto.DadosAutenticacao;
import br.com.controlefinanceiro.dto.DadosTokenJWT;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AutenticacaoService extends UserDetailsService {
    ResponseEntity<DadosTokenJWT> login(DadosAutenticacao dados);
    ResponseEntity logout(String authorizationHeader);
}
