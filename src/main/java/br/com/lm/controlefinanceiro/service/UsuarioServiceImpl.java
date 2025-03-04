package br.com.lm.controlefinanceiro.service;

import br.com.lm.controlefinanceiro.dto.DadosAtualizadosUsuario;
import br.com.lm.controlefinanceiro.dto.DadosNovoUsuario;
import br.com.lm.controlefinanceiro.exceptions.AutenticacaoException;
import br.com.lm.controlefinanceiro.exceptions.RegistroNaoEncontradoException;
import br.com.lm.controlefinanceiro.exceptions.UsuarioExistenteException;
import br.com.lm.controlefinanceiro.interfaces.MessageService;
import br.com.lm.controlefinanceiro.interfaces.UsuarioService;
import br.com.lm.controlefinanceiro.model.Usuario;
import br.com.lm.controlefinanceiro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final MessageService messageService;

    @Override
    public ResponseEntity novoUsuario(DadosNovoUsuario dados) {
        Usuario novoUsuario = new Usuario(dados);

        List<Usuario> usuariosComLoginExistente = usuarioRepository.findAllByLogin(dados.login());
        List<Usuario> usuariosComEmailExistente = usuarioRepository.findAllByEmail(dados.email());

        if (!usuariosComLoginExistente.isEmpty()) {
            throw new UsuarioExistenteException(messageService.getMessage("api.usuario.login.existente", dados.login()));
        }

        if (!usuariosComEmailExistente.isEmpty()) {
            throw new UsuarioExistenteException(messageService.getMessage("api.usuario.email.existente", dados.login()));
        }

        usuarioRepository.save(novoUsuario);
        log.info("Usuario {} inserido no sistema com sucesso.", dados.login());

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity alterarUsuario(String uuid, DadosAtualizadosUsuario dados) {
        Usuario alterarUsuario = getUsuarioByUuid(uuid);

        alterarUsuario.setLogin(dados.login());
        alterarUsuario.setSenha(dados.senha());
        usuarioRepository.save(alterarUsuario);

        log.info("Usuario {} alterado com sucesso.", dados.login());
        return ResponseEntity.ok().build();

    }

    @Override
    public ResponseEntity inativarUsuario(String uuid) {
        Usuario usuario = getUsuarioByUuid(uuid);

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);

        log.info("Usuario {} foi inativado.", usuario.getLogin());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity ativarUsuario(String uuid) {
        Usuario usuario = getUsuarioByUuid(uuid);

        usuario.setAtivo(true);
        usuarioRepository.save(usuario);

        log.info("Usuario {} foi ativado.", usuario.getLogin());
        return ResponseEntity.ok().build();
    }

    @Override
    public Usuario getDadosUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Usuario usuario = (Usuario) usuarioRepository.findByLogin(authentication.getName())
                    .orElseThrow(() -> new RegistroNaoEncontradoException(messageService.getMessage("api.usuario.login.nao.encontrado", authentication.getName())));

            log.info("Obtendo os dados do usuario logado {}", usuario.getLogin());
            return usuario;
        }
        throw new AutenticacaoException(messageService.getMessage("api.usuario.nao.autenticado"));
    }

    private Usuario getUsuarioByUuid(String uuid) {
        Usuario usuario = usuarioRepository.findByUuid(uuid)
                .orElseThrow(() -> new RegistroNaoEncontradoException(messageService.getMessage("api.usuario.id.nao.encontrado")));

        log.info("Obtendo os dados do usuario atraves do id {}", usuario.getUuid());
        return usuario;
    }
}
