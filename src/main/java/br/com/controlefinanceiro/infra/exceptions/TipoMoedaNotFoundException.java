package br.com.controlefinanceiro.infra.exceptions;

public class TipoMoedaNotFoundException extends RuntimeException {
    public TipoMoedaNotFoundException(String moeda) {
        super(String.format("Moeda %s não encontrada, atualmente as seguintes moedas estão disponívels para consulta: EUR, USD, CAD, JPY, CNY.", moeda));
    }
}
