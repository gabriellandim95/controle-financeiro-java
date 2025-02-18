package br.com.lm.controlefinanceiro.interfaces;

import br.com.lm.controlefinanceiro.model.Usuario;

public interface TokenService {
    String gerarToken(Usuario usuario);
    String getSubject(String tokenJWT);
}
