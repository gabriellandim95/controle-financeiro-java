package br.com.controlefinanceiro.repository;

import br.com.controlefinanceiro.model.HistoricoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoPagamentoRespository extends JpaRepository<HistoricoPagamento, Long> {
}
