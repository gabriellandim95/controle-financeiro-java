package br.com.lm.controlefinanceiro.dto;

import java.math.BigDecimal;

public record DadosGeraisCotacaoMoeda(Boolean success, String terms, String privacy, DadosQueryCotacaoMoeda query, BigDecimal result) {
}
