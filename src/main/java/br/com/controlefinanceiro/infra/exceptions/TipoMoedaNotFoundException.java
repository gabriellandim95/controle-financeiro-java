package br.com.controlefinanceiro.infra.exceptions;

public class TipoMoedaNotFoundException extends RuntimeException {
    public TipoMoedaNotFoundException(String message) {
        super(message);
    }
}
