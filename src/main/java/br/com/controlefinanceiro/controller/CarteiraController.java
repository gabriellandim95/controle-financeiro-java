package br.com.controlefinanceiro.controller;

import br.com.controlefinanceiro.dto.DadosCarteira;
import br.com.controlefinanceiro.service.CarteiraService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/carteira")
public class CarteiraController {

    @Autowired
    private CarteiraService carteiraService;

    @Operation(summary = "Cadastrando uma nova carteira.")
    @PostMapping(value = "/cadastrar")
    public ResponseEntity cadastrarCarteira(@RequestBody @Valid DadosCarteira dados, UriComponentsBuilder uriBuilder){
        return carteiraService.cadastrarCarteira(dados, uriBuilder);
    }

    @Operation(summary = "Alterando uma carteira através do ID")
    @PutMapping(value = "/{id}")
    public ResponseEntity alterarCarteira(@PathVariable("id") Long id, @RequestBody DadosCarteira dados){
        return carteiraService.alterarCarteira(id, dados);
    }
}
