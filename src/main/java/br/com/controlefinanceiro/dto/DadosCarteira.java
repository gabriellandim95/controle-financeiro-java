package br.com.controlefinanceiro.dto;

import br.com.controlefinanceiro.enums.TipoCarteira;
import br.com.controlefinanceiro.model.Carteira;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DadosCarteira(@NotBlank(message = "Titulo não pode ser vazio!")
                            String titulo,
                            String descricao,
                            @Positive(message = "O saldo não pode ser negativo.")
                            BigDecimal saldo,
                            TipoCarteira tipoCarteira) {
    public DadosCarteira(Carteira carteira){
        this(carteira.getTitulo(), carteira.getDescricao(), carteira.getSaldo(), carteira.getTipoCarteira());
    }
}
