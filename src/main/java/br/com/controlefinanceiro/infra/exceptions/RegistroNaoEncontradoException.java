package br.com.controlefinanceiro.infra.exceptions;

public class RegistroNaoEncontradoException extends RuntimeException {

    public RegistroNaoEncontradoException(String objeto){
        super(String.format("%s não encontrada.", objeto));
    }
}
