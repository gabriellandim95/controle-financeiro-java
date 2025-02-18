package br.com.lm.controlefinanceiro.repository;

import br.com.lm.controlefinanceiro.model.LogAcesso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEventosRepository  extends JpaRepository<LogAcesso, Integer> {
}
