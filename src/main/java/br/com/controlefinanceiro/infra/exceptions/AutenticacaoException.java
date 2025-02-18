package br.com.controlefinanceiro.infra.exceptions;

public class AutenticacaoException extends RuntimeException {
    public AutenticacaoException(String message) {
        super(message);
    }

    public AutenticacaoException(String message, Throwable cause) {
    }
}
