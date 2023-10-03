package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.dto.DadosGeraisCotacaoMoeda;
import br.com.controlefinanceiro.enums.TipoMoedaCotacao;
import br.com.controlefinanceiro.infra.exceptions.TipoMoedaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CotacaoMoedaService {
    private final RestTemplate restTemplate;
    @Value("${api.cotacao.url}")
    private String urlCotacaoMoeda;

    @Autowired
    public CotacaoMoedaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<DadosGeraisCotacaoMoeda> getCotacao(String codigoMoeda) {
        ResponseEntity<DadosGeraisCotacaoMoeda> response = restTemplate.getForEntity(urlCotacaoMoeda + codigoMoeda, DadosGeraisCotacaoMoeda.class);

        if (!TipoMoedaCotacao.getCodigos().contains(codigoMoeda)){
            throw new TipoMoedaNotFoundException(codigoMoeda);
        }

        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        } else {
            throw new RuntimeException("A API externa retornou o seguinte status: " + response.getStatusCode().value());
        }
    }
}