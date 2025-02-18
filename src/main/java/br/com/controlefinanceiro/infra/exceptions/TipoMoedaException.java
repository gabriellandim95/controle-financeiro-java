package br.com.controlefinanceiro.infra.exceptions;

public class TipoMoedaException extends RuntimeException {
    public TipoMoedaException(String message) {
        super(message);
    }
}
