package br.com.lm.controlefinanceiro.service;

import br.com.lm.controlefinanceiro.dto.DadosAutenticacao;
import br.com.lm.controlefinanceiro.dto.DadosTokenJWT;
import br.com.lm.controlefinanceiro.enums.TipoLogEvento;
import br.com.lm.controlefinanceiro.interfaces.*;
import br.com.lm.controlefinanceiro.model.Usuario;
import br.com.lm.controlefinanceiro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutenticacaoServiceImpl implements AutenticacaoService {
    /**
     * API de autenticação em desenvolvimento, esta classa deixara de existir.
     * ***/
    private final UsuarioRepository usuarioRepository;
    private final ObjectProvider<AuthenticationManager> authenticationManagerProvider;
    private final TokenService tokenService;
    private final LogAcessoService logAcessoService;
    private final TokenBlackListService tokenBlackListService;
    private final MessageService messageService;
    private final UsuarioService usuarioService;

    @Override
    public ResponseEntity<DadosTokenJWT> login(DadosAutenticacao dados) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        Authentication auth = authenticationManagerProvider.getObject().authenticate(authToken);
        String token = tokenService.gerarToken((Usuario) auth.getPrincipal());

        logAcessoService.gerarEvento(dados.login(), "Acessou o sistema.", TipoLogEvento.ACESSO_AO_SISTEMA);
        log.info("Usuario {} acessou o sistema.", dados.login());
        return ResponseEntity.ok(new DadosTokenJWT(token));
    }

    @Override
    public ResponseEntity logout(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        tokenBlackListService.addBlackList(token);
        Usuario usuario = usuarioService.getDadosUsuarioLogado();

        SecurityContextHolder.clearContext();
        log.info("Usuario {} saiu do sistema.", usuario.getLogin());
        return ResponseEntity.noContent().build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException(messageService.getMessage("api.auth.usuario.nao.encontrado")));
    }
}
