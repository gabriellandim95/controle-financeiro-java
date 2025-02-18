package br.com.lm.controlefinanceiro.dto;

import br.com.lm.controlefinanceiro.enums.StatusConta;
import br.com.lm.controlefinanceiro.model.Conta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Date;

public record DadosConta(@NotBlank (message = "O titulo não pode ser vazio!")
                         String titulo,
                         String descricao,
                         Date dataVencimento,
                         @PositiveOrZero(message = "O saldo da conta não pode zero ou negativo!")
                         BigDecimal valor,
                         StatusConta statusConta) {
    public DadosConta(Conta conta){
        this(conta.getTitulo(), conta.getDescricao(), conta.getDataVencimento(), conta.getValor(), conta.getStatusConta());
    }
}
