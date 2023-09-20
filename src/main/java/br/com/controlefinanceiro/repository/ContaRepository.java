package br.com.controlefinanceiro.repository;

import br.com.controlefinanceiro.model.Conta;
import br.com.controlefinanceiro.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    Conta findFirstById(Long id);
    List<Conta> findAllByDataVencimentoBefore(Date date);

    Page<Conta> findAllByUsuario(Pageable pageable, Usuario usuario);
}
