package br.com.controlefinanceiro.controller;

import br.com.controlefinanceiro.dto.DadosAtualizadosUsuario;
import br.com.controlefinanceiro.dto.DadosNovoUsuario;
import br.com.controlefinanceiro.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping(value = "/cadastrar")
    public ResponseEntity novoUsuario(@RequestBody @Valid DadosNovoUsuario dados){
        return usuarioService.novoUsuario(dados);
    }

    @PutMapping(value = "/alterar/{id}")
    public ResponseEntity alterarUsuario(@PathVariable ("id") Integer id, @RequestBody @Valid DadosAtualizadosUsuario dados){
        return usuarioService.alterarUsuario(id, dados);
    }

    @PutMapping(value = "/inativar/{id}")
    public ResponseEntity inativarUsuario(@PathVariable ("id") Integer id){
        return usuarioService.inativarUsuario(id);
    }

    @PutMapping(value = "/ativar/{id}")
    public ResponseEntity ativarUsuario(@PathVariable ("id") Integer id){
        return usuarioService.ativarUsuario(id);
    }
}
