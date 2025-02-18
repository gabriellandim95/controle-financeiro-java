package br.com.lm.controlefinanceiro.controller;

import br.com.lm.controlefinanceiro.dto.DadosAtualizadosUsuario;
import br.com.lm.controlefinanceiro.dto.DadosNovoUsuario;
import br.com.lm.controlefinanceiro.interfaces.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Cadastro de um novo usuário.", method = "POST")
    @PostMapping(value = "/cadastrar")
    public ResponseEntity novoUsuario(@RequestBody @Valid DadosNovoUsuario dados){
        return usuarioService.novoUsuario(dados);
    }
    @Operation(summary = "Alterando dados de um usuário através do ID.", method = "PUT")
    @PutMapping(value = "/{uuid}")
    public ResponseEntity alterarUsuario(@PathVariable ("uuid") String uuid, @RequestBody @Valid DadosAtualizadosUsuario dados){
        return usuarioService.alterarUsuario(uuid, dados);
    }

    @Operation(summary = "Inativando um usuário através do ID.", method = "PUT")
    @PutMapping(value = "/inativar/{uuid}")
    public ResponseEntity inativarUsuario(@PathVariable ("uuid") String uuid){
        return usuarioService.inativarUsuario(uuid);
    }

    @Operation(summary = "Ativando um usuário através do ID.", method = "PUT")
    @PutMapping(value = "/ativar/{uuid}")
    public ResponseEntity ativarUsuario(@PathVariable ("uuid") String uuid){
        return usuarioService.ativarUsuario(uuid);
    }
}
