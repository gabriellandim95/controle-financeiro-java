package br.com.lm.controlefinanceiro.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TipoMoedaCotacao {

    EUR("EUR","Euro"),
    USD("USD","Dólar americano"),
    CAD("CAD","Dólar canadense"),
    JPY("JPY","Iene"),
    CNY("CNY","Yuan Renminbi");

    private final String codigo;
    private final String descricao;

    TipoMoedaCotacao(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public static List<String> getCodigos() {
        return Arrays.stream(TipoMoedaCotacao.values())
                .map(TipoMoedaCotacao::getCodigo)
                .collect(Collectors.toList());
    }
}