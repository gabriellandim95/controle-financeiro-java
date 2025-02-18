package br.com.lm.controlefinanceiro.interfaces;

import br.com.lm.controlefinanceiro.dto.DadosAtualizadosUsuario;
import br.com.lm.controlefinanceiro.dto.DadosNovoUsuario;
import br.com.lm.controlefinanceiro.model.Usuario;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {
    ResponseEntity novoUsuario(DadosNovoUsuario dados);
    ResponseEntity alterarUsuario(String uuid, DadosAtualizadosUsuario dados);
    ResponseEntity inativarUsuario(String uuid);
    ResponseEntity ativarUsuario(String uuid);
    Usuario getDadosUsuarioLogado();
}
