package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.dto.DadosGeraisCotacaoMoeda;
import br.com.controlefinanceiro.enums.TipoMoedaCotacao;
import br.com.controlefinanceiro.infra.exceptions.TipoMoedaException;
import br.com.controlefinanceiro.infra.exceptions.TipoMoedaNotFoundException;
import br.com.controlefinanceiro.interfaces.CotacaoMoedaService;
import br.com.controlefinanceiro.interfaces.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class CotacaoMoedaServiceImpl implements CotacaoMoedaService {

    @Value("${api.cotacao.url}")
    private String urlCotacaoMoeda;
    private final WebClient webClient;
    private final MessageService messageService;

    @Override
    public ResponseEntity<DadosGeraisCotacaoMoeda> getCotacao(String codigoMoeda) {
        if (!TipoMoedaCotacao.getCodigos().contains(codigoMoeda)) {
            throw new TipoMoedaNotFoundException(messageService.getMessage("api.cotacao.moeda.nao.encontrada", codigoMoeda));
        }

        try {
            DadosGeraisCotacaoMoeda dados = webClient.get()
                    .uri(urlCotacaoMoeda + codigoMoeda)
                    .retrieve()
                    .onStatus(
                            status -> status.isError(),
                            response -> {
                                throw new TipoMoedaException(
                                        messageService.getMessage(
                                                "api.cotacao.retorno.status",
                                                response.statusCode().value()
                                        )
                                );
                            }
                    )
                    .bodyToMono(DadosGeraisCotacaoMoeda.class)
                    .block();

            return ResponseEntity.ok(dados);

        } catch (Exception e) {
            throw new TipoMoedaException(messageService.getMessage("api.cotacao.erro.desconhecido", e.getMessage()));
        }
    }
}