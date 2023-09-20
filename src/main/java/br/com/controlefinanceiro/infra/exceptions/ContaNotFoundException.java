package br.com.controlefinanceiro.infra.exceptions;

public class ContaNotFoundException extends RuntimeException {
    public ContaNotFoundException() {
        super("Carteira não encontrada!");
    }
}
