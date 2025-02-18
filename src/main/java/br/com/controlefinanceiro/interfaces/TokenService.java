package br.com.controlefinanceiro.interfaces;

import br.com.controlefinanceiro.model.Usuario;

public interface TokenService {
    String gerarToken(Usuario usuario);
    String getSubject(String tokenJWT);
}
