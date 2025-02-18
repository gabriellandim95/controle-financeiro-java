package br.com.controlefinanceiro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosNovoUsuario(@NotBlank(message = "O login nao pode ser vazio.")
                               @Size(max = 20, message = "O login deve ter no maximo {max} caracteres.")
                               String login,
                               @NotBlank(message = "O e-mail nao pode ser vazio.")
                               @Email(message = "O e-mail fornecido nao e válido.")
                               @Size(min = 5, max = 60, message = "O e-mail deve ter entre {min} e {max} caracteres.")
                               String email,
                               @NotBlank(message = "A senha nao pode ser vazia.")
                               @Size(min = 8, max = 30, message = "A senha deve ter entre {min} e {max} caracteres.")
                               String senha) {

    @Override
    public String toString() {
        return "DadosNovoUsuario{" +
                "login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }
}
