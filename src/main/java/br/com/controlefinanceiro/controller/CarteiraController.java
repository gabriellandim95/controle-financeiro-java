package br.com.controlefinanceiro.controller;

import br.com.controlefinanceiro.dto.DadosCarteira;
import br.com.controlefinanceiro.service.CarteiraServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/carteira")
@RequiredArgsConstructor
public class CarteiraController {

    private final CarteiraServiceImpl carteiraServiceImpl;

    @Operation(summary = "Cadastrando uma nova carteira.")
    @PostMapping(value = "/cadastrar")
    public ResponseEntity cadastrarCarteira(@RequestBody @Valid DadosCarteira dados, UriComponentsBuilder uriBuilder){
        return carteiraServiceImpl.cadastrarCarteira(dados, uriBuilder);
    }

    @Operation(summary = "Alterando uma carteira.")
    @PutMapping(value = "/{uuid}")
    public ResponseEntity alterarCarteira(@PathVariable("uuid") String uuid, @RequestBody DadosCarteira dados){
        return carteiraServiceImpl.alterarCarteira(uuid, dados);
    }

    @Operation(summary = "Listando todas as carteiras.")
    @GetMapping
    public ResponseEntity<Page<DadosCarteira>> listarCarteiras(@PageableDefault(size = 10, sort = {"titulo"}) Pageable pageable){
        return carteiraServiceImpl.listarCarteiras(pageable);
    }

    @Operation(summary = "Detalhando carteira.")
    @GetMapping(value = "/{uuid}")
    public ResponseEntity listarCarteiraByUuid(@PathVariable("uuid") String uuid){
        return carteiraServiceImpl.listarCarteiraByUuid(uuid);
    }

    @Operation(summary = "Deletando carteira através do ID")
    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity deletarCarteiraByUuid(@PathVariable("uuid") String uuid){
        return carteiraServiceImpl.deletarCarteiraByUuid(uuid);
    }
}
