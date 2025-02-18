package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.dto.DadosGeraisCotacaoMoeda;
import br.com.controlefinanceiro.enums.TipoMoedaCotacao;
import br.com.controlefinanceiro.infra.exceptions.TipoMoedaNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CotacaoMoedaServiceImpl {

    private final RestTemplate restTemplate;
    @Value("${api.cotacao.url}")
    private String urlCotacaoMoeda;

    public ResponseEntity<DadosGeraisCotacaoMoeda> getCotacao(String codigoMoeda) {
        /*
        *
        * TODO: Implementar o FeingClient
        *
        * */
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