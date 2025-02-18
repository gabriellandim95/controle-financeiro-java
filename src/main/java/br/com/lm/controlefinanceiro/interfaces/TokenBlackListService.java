package br.com.lm.controlefinanceiro.interfaces;

public interface TokenBlackListService {
    void addBlackList(String token);
    Boolean isTokenNaBlackList(String token);
}
