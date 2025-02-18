package br.com.lm.controlefinanceiro.service;

import br.com.lm.controlefinanceiro.dto.DadosGeraisCotacaoMoeda;
import br.com.lm.controlefinanceiro.enums.TipoMoedaCotacao;
import br.com.lm.controlefinanceiro.exceptions.TipoMoedaException;
import br.com.lm.controlefinanceiro.exceptions.TipoMoedaNotFoundException;
import br.com.lm.controlefinanceiro.interfaces.CotacaoMoedaService;
import br.com.lm.controlefinanceiro.interfaces.MessageService;
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