package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.enums.StatusConta;
import br.com.controlefinanceiro.interfaces.ContaStatusService;
import br.com.controlefinanceiro.model.Conta;
import br.com.controlefinanceiro.repository.ContaRepository;
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
