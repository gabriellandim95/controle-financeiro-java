package br.com.controlefinanceiro.infra.exceptions;

public class CarteiraNotFoundException extends RuntimeException {
    public CarteiraNotFoundException(){
        super("Carteira não encontrada!");
    }
}
