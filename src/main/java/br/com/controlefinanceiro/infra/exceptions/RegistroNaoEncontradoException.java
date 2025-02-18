package br.com.controlefinanceiro.infra.exceptions;

public class RegistroNaoEncontradoException extends RuntimeException {

    public RegistroNaoEncontradoException(String mensagem){
        super(mensagem);
    }
}
