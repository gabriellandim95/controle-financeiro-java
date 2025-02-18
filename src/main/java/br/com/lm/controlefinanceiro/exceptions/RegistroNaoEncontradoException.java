package br.com.lm.controlefinanceiro.exceptions;

public class RegistroNaoEncontradoException extends RuntimeException {

    public RegistroNaoEncontradoException(String mensagem){
        super(mensagem);
    }
}
