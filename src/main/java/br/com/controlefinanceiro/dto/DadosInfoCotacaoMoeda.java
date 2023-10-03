package br.com.controlefinanceiro.dto;

import java.math.BigDecimal;
import java.util.Date;

public record DadosInfoCotacaoMoeda(Date timestamp, BigDecimal quote) {
}
