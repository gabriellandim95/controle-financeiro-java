package br.com.controlefinanceiro.dto;

import java.math.BigDecimal;

public record DadosGeraisCotacaoMoeda(Boolean success, String terms, String privacy, DadosQueryCotacaoMoeda query, DadosInfoCotacaoMoeda info, BigDecimal result) {
}
