package br.com.lm.controlefinanceiro.controller;

import br.com.lm.controlefinanceiro.dto.DadosCarteira;
import br.com.lm.controlefinanceiro.interfaces.CarteiraService;

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
@RequestMapping(value = "/carteiras")
@RequiredArgsConstructor
public class CarteiraController {

    private final CarteiraService carteiraService;

    @Operation(summary = "Cadastrando uma nova carteira.")
    @PostMapping
    public ResponseEntity<DadosCarteira> cadastrarCarteira(@RequestBody @Valid DadosCarteira dados, UriComponentsBuilder uriBuilder){
        return carteiraService.cadastrarCarteira(dados, uriBuilder);
    }

    @Operation(summary = "Alterando a carteira através do ID.")
    @PutMapping(value = "/{uuid}")
    public ResponseEntity<DadosCarteira> alterarCarteira(@PathVariable("uuid") String uuid, @RequestBody DadosCarteira dados){
        return carteiraService.alterarCarteira(uuid, dados);
    }

    @Operation(summary = "Listando todas as carteiras.")
    @GetMapping
    public ResponseEntity<Page<DadosCarteira>> listarCarteiras(@PageableDefault(size = 10, sort = {"titulo"}) Pageable pageable){
        return carteiraService.listarCarteiras(pageable);
    }

    @Operation(summary = "Detalhando carteira através do ID.")
    @GetMapping(value = "/{uuid}")
    public ResponseEntity<DadosCarteira> listarCarteiraByUuid(@PathVariable("uuid") String uuid){
        return carteiraService.detalharCarteiraByUuid(uuid);
    }

    @Operation(summary = "Deletando carteira através do ID")
    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity<Void> deletarCarteiraByUuid(@PathVariable("uuid") String uuid){
        return carteiraService.deletarCarteiraByUuid(uuid);
    }
}
