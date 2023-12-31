package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.dto.DadosAtualizadosUsuario;
import br.com.controlefinanceiro.dto.DadosNovoUsuario;
import br.com.controlefinanceiro.model.Usuario;
import br.com.controlefinanceiro.repository.UsuarioRepository;
import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger logger = LogManager.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private LogAcessoService logAcessoService;

    public ResponseEntity novoUsuario(DadosNovoUsuario dados) {
        logger.info(String.format("O seguinte objeto: %s esta sendo inserido no sistema.", dados.toString()));

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
        logger.info(String.format("Objeto: %s inserido no sistema com sucesso.", dados));

        return ResponseEntity.ok().build();
    }

    public ResponseEntity alterarUsuario(String uuid, DadosAtualizadosUsuario dados) {
        Usuario alterarUsuario = usuarioRepository.findByUuid(uuid);
        alterarUsuario.setLogin(dados.login());
        alterarUsuario.setSenha(dados.senha());
        usuarioRepository.save(alterarUsuario);

        return ResponseEntity.ok().build();

    }

    public ResponseEntity inativarUsuario(String uuid) {
        Usuario usuarioInativado = usuarioRepository.findByUuid(uuid);
        usuarioInativado.setAtivo(false);
        usuarioRepository.save(usuarioInativado);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity ativarUsuario(String uuid) {
        Usuario usuarioInativado = usuarioRepository.findByUuid(uuid);
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
