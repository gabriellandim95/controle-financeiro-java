package br.com.controlefinanceiro.controller;

import br.com.controlefinanceiro.dto.DadosAtualizadosUsuario;
import br.com.controlefinanceiro.dto.DadosNovoUsuario;
import br.com.controlefinanceiro.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Cadastro de um novo usuário.", method = "POST")
    @PostMapping(value = "/cadastrar")
    public ResponseEntity novoUsuario(@RequestBody @Valid DadosNovoUsuario dados){
        return usuarioService.novoUsuario(dados);
    }
    @Operation(summary = "Alterando dados de um usuário através do UUID.", method = "PUT")
    @PutMapping(value = "/alterar/{uuid}")
    public ResponseEntity alterarUsuario(@PathVariable ("uuid") String uuid, @RequestBody @Valid DadosAtualizadosUsuario dados){
        return usuarioService.alterarUsuario(uuid, dados);
    }

    @Operation(summary = "Inativando um usuário através do UUID.", method = "PUT")
    @PutMapping(value = "/inativar/{uuid}")
    public ResponseEntity inativarUsuario(@PathVariable ("uuid") String uuid){
        return usuarioService.inativarUsuario(uuid);
    }

    @Operation(summary = "Ativando um usuário através do UUID.", method = "PUT")
    @PutMapping(value = "/ativar/{id}")
    public ResponseEntity ativarUsuario(@PathVariable ("uuid") String uuid){
        return usuarioService.ativarUsuario(uuid);
    }
}
