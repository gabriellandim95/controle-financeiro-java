package br.com.lm.controlefinanceiro.repository;

import br.com.lm.controlefinanceiro.enums.StatusConta;
import br.com.lm.controlefinanceiro.model.Conta;
import br.com.lm.controlefinanceiro.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    List<Conta> findAllByStatusContaAndDataVencimentoBetween(StatusConta statusConta, LocalDate dataInicio, LocalDate dataFim);
    List<Conta> findAllByStatusContaInAndDataVencimentoBefore(List<StatusConta> statusConta, LocalDate dataAtual);

    //findAllByStatusContaNotInAndDataVencimentoBetween

    Page<Conta> findAllByUsuario(Pageable pageable, Usuario usuario);

    Optional<Conta> findByUuid(String uuid);
}
