package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.dto.DadosGeraisCotacaoMoeda;
import br.com.controlefinanceiro.enums.TipoMoedaCotacao;
import br.com.controlefinanceiro.infra.exceptions.TipoMoedaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class CotacaoMoedaService {
    private final RestTemplate restTemplate;
    @Value("${api.cotacao.url}")
    private String urlCotacaoMoeda;

    @Autowired
    public CotacaoMoedaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<DadosGeraisCotacaoMoeda> getCotacao(String moeda) {
        ResponseEntity<DadosGeraisCotacaoMoeda> response = restTemplate.getForEntity(urlCotacaoMoeda + moeda, DadosGeraisCotacaoMoeda.class);

        if (!TipoMoedaCotacao.getTipoMoedaCotacaoList().contains(moeda)){
            throw new TipoMoedaNotFoundException(moeda);
        }

        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        } else {
            throw new RuntimeException("A API externa retornou o seguinte status: " + response.getStatusCode().value());
        }
    }
}