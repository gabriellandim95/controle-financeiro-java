package br.com.lm.controlefinanceiro.dto;


import java.time.LocalDateTime;

public record DadosErrorResponse(Integer status, String mensagem, LocalDateTime data) {
}
