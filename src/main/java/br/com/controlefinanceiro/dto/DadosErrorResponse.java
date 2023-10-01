package br.com.controlefinanceiro.dto;


import java.time.LocalDateTime;

public record DadosErrorResponse(Integer status, String message, LocalDateTime timeStamp) {
}
