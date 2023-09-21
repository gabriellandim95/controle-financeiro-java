package br.com.controlefinanceiro.controller;

import br.com.controlefinanceiro.dto.DadosCarteira;
import br.com.controlefinanceiro.service.CarteiraService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
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

    @Operation(summary = "Alterando uma carteira através do UUID.")
    @PutMapping(value = "/{uuid}")
    public ResponseEntity alterarCarteira(@PathVariable("uuid") String uuid, @RequestBody DadosCarteira dados){
        return carteiraService.alterarCarteira(uuid, dados);
    }

    @Operation(summary = "Listando todas as carteiras.")
    @GetMapping
    public ResponseEntity<Page<DadosCarteira>> listarCarteiras(@PageableDefault(size = 10, sort = {"titulo"}) Pageable pageable){
        return carteiraService.listarCarteiras(pageable);
    }

    @Operation(summary = "Detalhando carteira através do UUID.")
    @GetMapping(value = "/{uuid}")
    public ResponseEntity listarCarteiraByUuid(@PathVariable("uuid") String uuid){
        return carteiraService.listarCarteiraByUuid(uuid);
    }

    @Operation(summary = "Deletando carteira através do UUID")
    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity deletarCarteiraByUuid(@PathVariable("uuid") String uuid){
        return carteiraService.deletarCarteiraByUuid(uuid);
    }
}
