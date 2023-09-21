package br.com.controlefinanceiro.repository;

import br.com.controlefinanceiro.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    UserDetails findByLogin(String login);
    Usuario findByIdAndAtivoTrue (Integer id);
    Usuario findByIdAndAtivoFalse (Integer id);
    List<Usuario> findAllByLogin (String login);
    List<Usuario> findAllByEmail (String email);

    Usuario findByUuid(String uuid);
}
