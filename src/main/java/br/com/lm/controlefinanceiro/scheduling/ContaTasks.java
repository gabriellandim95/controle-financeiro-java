package br.com.lm.controlefinanceiro.scheduling;

import br.com.lm.controlefinanceiro.enums.StatusConta;
import br.com.lm.controlefinanceiro.interfaces.ContaStatusService;
import br.com.lm.controlefinanceiro.model.Conta;
import br.com.lm.controlefinanceiro.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContaTasks {
    private final ContaStatusService contaStatusService;
    private final ContaRepository contaRepository;

    @Scheduled(cron = "0 0 5 * * *")
    private void verificarContasPertoDeVencer() {
        List<Conta> contasPertoDeVencer = contaRepository.findAllByStatusContaAndDataVencimentoBetween(
                StatusConta.EM_ABERTO,
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        contasPertoDeVencer.forEach(conta -> contaStatusService.atualizarStatusParaPertoDeVencer(conta));
    }

    @Scheduled(cron = "0 0 6 * * *")
    private void verificarContasVencidas() {
        List<Conta> contasVencidas = contaRepository.findAllByStatusContaInAndDataVencimentoBefore(
                Arrays.asList(StatusConta.EM_ABERTO, StatusConta.PERTO_DE_VENCER),
                LocalDate.now()
        );

        contasVencidas.forEach(conta -> contaStatusService.atualizarStatusParaVencida(conta));
    }
}
