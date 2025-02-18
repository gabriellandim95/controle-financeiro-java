package br.com.lm.controlefinanceiro.dto;

import br.com.lm.controlefinanceiro.enums.TipoCarteira;
import br.com.lm.controlefinanceiro.model.Carteira;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DadosCarteira(@NotBlank(message = "Titulo não pode ser vazio!")
                            @Size(min = 1, max = 20, message = "O Titulo deve ter entre {min} e {max} caracteres")
                            String titulo,
                            @Size(max = 100, message = "A Descrição deve ter até {max} caracteres")
                            String descricao,
                            @Positive(message = "O saldo não pode ser negativo.")
                            BigDecimal saldo,
                            TipoCarteira tipoCarteira) {
    public DadosCarteira(Carteira carteira){
        this(carteira.getTitulo(), carteira.getDescricao(), carteira.getSaldo(), carteira.getTipoCarteira());
    }
}
