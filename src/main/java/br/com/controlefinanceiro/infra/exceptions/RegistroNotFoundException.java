package br.com.controlefinanceiro.infra.exceptions;

public class RegistroNotFoundException extends RuntimeException {

    public RegistroNotFoundException(String objeto){
        super(String.format("%s não encontrada.", objeto));
    }
}
