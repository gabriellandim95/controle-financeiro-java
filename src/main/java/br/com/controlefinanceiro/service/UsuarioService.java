package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.dto.DadosAtualizadosUsuario;
import br.com.controlefinanceiro.dto.DadosNovoUsuario;
import br.com.controlefinanceiro.model.Usuario;
import br.com.controlefinanceiro.repository.UsuarioRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private LogAcessoService logAcessoService;

    public ResponseEntity novoUsuario(DadosNovoUsuario dados) {
        Usuario novoUsuario = new Usuario(dados);

        List<Usuario> usuariosComLoginExistente = usuarioRepository.findAllByLogin(dados.login());
        List<Usuario> usuariosComEmailExistente = usuarioRepository.findAllByEmail(dados.email());

        if (!usuariosComLoginExistente.isEmpty()) {
            throw new ConstraintViolationException(String.format("O Login: %s não esta disponivel para uso, por favor tente outro.", dados.login()), null);
        }

        if (!usuariosComEmailExistente.isEmpty()) {
            throw new ConstraintViolationException(String.format("O E-mail: %s não esta disponivel para uso, por favor tente outro.", dados.email()), null);
        }

        usuarioRepository.save(novoUsuario);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity alterarUsuario(Integer id, DadosAtualizadosUsuario dados) {
        Usuario alterarUsuario = usuarioRepository.findByIdAndAtivoTrue(id);
        alterarUsuario.setLogin(dados.login());
        alterarUsuario.setSenha(dados.senha());
        usuarioRepository.save(alterarUsuario);

        return ResponseEntity.ok().build();

    }

    public ResponseEntity inativarUsuario(Integer id) {
        Usuario usuarioInativado = usuarioRepository.findByIdAndAtivoTrue(id);
        usuarioInativado.setAtivo(false);
        usuarioRepository.save(usuarioInativado);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity ativarUsuario(Integer id) {
        Usuario usuarioInativado = usuarioRepository.findByIdAndAtivoFalse(id);
        usuarioInativado.setAtivo(true);
        usuarioRepository.save(usuarioInativado);
        return ResponseEntity.ok().build();
    }

    public Usuario getDadosUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return (Usuario) usuarioRepository.findByLogin(authentication.getName());
        }
        return null;
    }
}
