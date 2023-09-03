package br.com.controlefinanceiro.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosAtualizadosUsuario(@NotBlank String login, @NotBlank String senha) {
}
