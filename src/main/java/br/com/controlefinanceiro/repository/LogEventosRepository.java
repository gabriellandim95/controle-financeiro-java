package br.com.controlefinanceiro.repository;

import br.com.controlefinanceiro.model.LogAcesso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEventosRepository  extends JpaRepository<LogAcesso, Integer> {
}
