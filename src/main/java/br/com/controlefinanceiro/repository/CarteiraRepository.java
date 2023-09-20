package br.com.controlefinanceiro.repository;

import br.com.controlefinanceiro.model.Carteira;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarteiraRepository extends JpaRepository<Carteira, Long> {
}
