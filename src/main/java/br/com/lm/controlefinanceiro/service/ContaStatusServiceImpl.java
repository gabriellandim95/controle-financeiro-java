package br.com.lm.controlefinanceiro.service;

import br.com.lm.controlefinanceiro.enums.StatusConta;
import br.com.lm.controlefinanceiro.interfaces.ContaStatusService;
import br.com.lm.controlefinanceiro.model.Conta;
import br.com.lm.controlefinanceiro.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContaStatusServiceImpl implements ContaStatusService {
    private final ContaRepository contaRepository;

    @Override
    public void atualizarStatusParaPertoDeVencer(Conta conta) {
        conta.setStatusConta(StatusConta.PERTO_DE_VENCER);
        contaRepository.save(conta);
    }

    @Override
    public void atualizarStatusParaVencida(Conta conta) {
        conta.setStatusConta(StatusConta.VENCIDA);
        contaRepository.save(conta);
    }
}
