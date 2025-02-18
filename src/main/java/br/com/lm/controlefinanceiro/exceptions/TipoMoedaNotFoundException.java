package br.com.lm.controlefinanceiro.exceptions;

public class TipoMoedaNotFoundException extends RuntimeException {
    public TipoMoedaNotFoundException(String message) {
        super(message);
    }
}
