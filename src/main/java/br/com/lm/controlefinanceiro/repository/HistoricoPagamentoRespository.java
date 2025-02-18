package br.com.lm.controlefinanceiro.repository;

import br.com.lm.controlefinanceiro.model.HistoricoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoPagamentoRespository extends JpaRepository<HistoricoPagamento, Long> {
}
