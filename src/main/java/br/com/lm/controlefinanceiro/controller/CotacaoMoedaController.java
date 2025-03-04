package br.com.lm.controlefinanceiro.controller;

import br.com.lm.controlefinanceiro.dto.DadosGeraisCotacaoMoeda;
import br.com.lm.controlefinanceiro.interfaces.CotacaoMoedaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cotacoes")
@RequiredArgsConstructor
public class CotacaoMoedaController {

    private final CotacaoMoedaService cotacaoMoedaService;

    @Operation(summary = "Obter cotação atual das seguintes moedas: EUR, USD, CAD, JPY, CNY", method = "GET")
    @GetMapping(value = "/{moeda}")
    public ResponseEntity<DadosGeraisCotacaoMoeda> getCotacao(@PathVariable("moeda") String codigoMoeda) {
        return cotacaoMoedaService.getCotacao(codigoMoeda);
    }
}
