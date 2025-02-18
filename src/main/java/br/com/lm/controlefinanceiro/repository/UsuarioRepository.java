package br.com.lm.controlefinanceiro.repository;

import br.com.lm.controlefinanceiro.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<UserDetails> findByLogin(String login);
    Usuario findByIdAndAtivoTrue (Integer id);
    Usuario findByIdAndAtivoFalse (Integer id);
    List<Usuario> findAllByLogin (String login);
    List<Usuario> findAllByEmail (String email);

    Optional<Usuario> findByUuid(String uuid);
}
