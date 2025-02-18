package br.com.controlefinanceiro.infra.exceptions;

public class ContaException extends RuntimeException {
    public ContaException(String message) {
        super(message);
    }
}
