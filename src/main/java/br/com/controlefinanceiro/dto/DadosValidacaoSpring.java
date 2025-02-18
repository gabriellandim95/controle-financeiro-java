package br.com.controlefinanceiro.dto;

import java.util.List;

public record DadosValidacaoSpring(List<DadosValidacaoSpringResponse> erros) {
}
