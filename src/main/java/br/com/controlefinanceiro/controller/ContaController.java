package br.com.controlefinanceiro.controller;

import br.com.controlefinanceiro.dto.DadosConta;
import br.com.controlefinanceiro.dto.DadosDetalhamentoConta;
import br.com.controlefinanceiro.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "Efetuar cadastro de uma nova conta.", method = "POST")
    @PostMapping(value = "/cadastrar")
    public ResponseEntity cadastrarConta(@RequestBody @Valid DadosConta dados, UriComponentsBuilder uriBuilder){
        return contaService.cadastrarConta(dados, uriBuilder);
    }
    @Operation(summary = "Alterar os dados de uma conta através do UUID.", method = "PUT")
    @PutMapping(value = "/{uuid}")
    public ResponseEntity alterarConta(@PathVariable("uuid") String uuid, @RequestBody DadosConta dados){
        return contaService.alterarConta(uuid, dados);
    }

    @Operation(summary = "Listagem paginada de todas as contas ordenadas pela data de vencimento.", method = "GET")
    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoConta>> listarContas(@PageableDefault(size = 10, sort = {"dataVencimento"}) Pageable pageable){
        return contaService.listarContas(pageable);
    }

    @Operation(summary = "Detalhamento da conta através do UUID.", method = "GET")
    @GetMapping(value = "/{uuid}")
    public ResponseEntity listarContaById(@PathVariable("uuid") String uuid){
        return contaService.listarContaByUuid(uuid);
    }

    @Operation(summary = "Deletando uma conta através do UUID.", method = "DELETE")
    @DeleteMapping(value = "/{uuid}")
    public ResponseEntity deletarById(@PathVariable("uuid") String uuid){
        return contaService.deletarByUuid(uuid);
    }

    @Operation(summary = "Pagando uma conta através do UUID", method = "PUT")
    @PutMapping(value = "/pagar/{uuidConta}/{uuidCarteira}")
    public ResponseEntity pagarContaByUuid(@PathVariable("uuidConta") String uuid, @PathVariable("uuidCarteira") String uuidCarteira){
        return contaService.pagarContaByUuid(uuid, uuidCarteira);
    }
}