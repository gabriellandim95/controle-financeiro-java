package br.com.controlefinanceiro.controller;

import br.com.controlefinanceiro.dto.DadosConta;
import br.com.controlefinanceiro.dto.DadosDetalhamentoConta;
import br.com.controlefinanceiro.service.AutenticacaoService;
import br.com.controlefinanceiro.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/conta")
public class ContaController {
    @Autowired
    private ContaService contaService;
    @Autowired
    private AutenticacaoService autenticacaoService;
    @Autowired
    public ContaController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping(value = "/cadastrar")
    public ResponseEntity cadastrarConta(@RequestBody @Valid DadosConta dados, UriComponentsBuilder uriBuilder){
        UserDetails usuarioLogado = autenticacaoService.getCurrentUserDetails();
        return contaService.cadastrarConta(dados, uriBuilder, usuarioLogado);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity alterarConta(@PathVariable("id") Integer id, DadosConta dados){
        UserDetails usuarioLogado = autenticacaoService.getCurrentUserDetails();
        return contaService.alterarConta(id, dados, usuarioLogado);
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoConta>> listarContas(@PageableDefault(size = 10, sort = {"dataVencimento"}) Pageable pageable){
        UserDetails usuarioLogado = autenticacaoService.getCurrentUserDetails();
        return contaService.listarContas(pageable, usuarioLogado);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity listarContaById(@PathVariable("id") Integer id){
        UserDetails usuarioLogado = autenticacaoService.getCurrentUserDetails();
        return contaService.listarContaById(id, usuarioLogado);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deletarById(@PathVariable("id") Integer id){
        return contaService.deletarById(id);
    }
}