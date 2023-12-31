package br.com.controlefinanceiro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosNovoUsuario(@NotBlank String login, @Email String email, @NotBlank String senha) {

    @Override
    public String toString() {
        return "DadosNovoUsuario{" +
                "login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }
}
