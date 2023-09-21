package br.com.controlefinanceiro.repository;

import br.com.controlefinanceiro.model.Carteira;
import br.com.controlefinanceiro.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarteiraRepository extends JpaRepository<Carteira, Long> {
    Page<Carteira> findAllByUsuario(Pageable pageable, Usuario usuario);
    Optional<Carteira> findByUuid(String uuid);
}
