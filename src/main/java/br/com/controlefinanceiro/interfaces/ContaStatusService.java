package br.com.controlefinanceiro.interfaces;

import br.com.controlefinanceiro.model.Conta;

public interface ContaStatusService {
    void atualizarStatusParaPertoDeVencer(Conta conta);
    void atualizarStatusParaVencida(Conta conta);
}
