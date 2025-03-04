package br.com.lm.controlefinanceiro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosAtualizadosUsuario(@NotBlank(message = "O login nao pode ser vazio.")
                                      @Size(max = 20, message = "O login deve ter no maximo {max} caracteres.")
                                      String login,
                                      @NotBlank(message = "A senha nao pode ser vazia.")
                                      @Size(min = 8, max = 30, message = "A senha deve ter entre {min} e {max} caracteres.")
                                      String senha) {
}
