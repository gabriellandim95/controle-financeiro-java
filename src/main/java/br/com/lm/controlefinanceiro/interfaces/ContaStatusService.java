package br.com.lm.controlefinanceiro.interfaces;

import br.com.lm.controlefinanceiro.model.Conta;

public interface ContaStatusService {
    void atualizarStatusParaPertoDeVencer(Conta conta);
    void atualizarStatusParaVencida(Conta conta);
}
