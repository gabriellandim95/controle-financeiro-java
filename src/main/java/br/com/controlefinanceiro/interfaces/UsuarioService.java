package br.com.controlefinanceiro.interfaces;

import br.com.controlefinanceiro.dto.DadosAtualizadosUsuario;
import br.com.controlefinanceiro.dto.DadosNovoUsuario;
import br.com.controlefinanceiro.model.Usuario;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {
    ResponseEntity novoUsuario(DadosNovoUsuario dados);
    ResponseEntity alterarUsuario(String uuid, DadosAtualizadosUsuario dados);
    ResponseEntity inativarUsuario(String uuid);
    ResponseEntity ativarUsuario(String uuid);
    Usuario getDadosUsuario();
}
