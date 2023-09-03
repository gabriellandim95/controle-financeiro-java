package br.com.controlefinanceiro.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosAutenticacao(@NotBlank String login, @NotBlank String senha) {
}
