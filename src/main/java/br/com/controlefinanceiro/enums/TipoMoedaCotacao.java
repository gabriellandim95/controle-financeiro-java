package br.com.controlefinanceiro.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum TipoMoedaCotacao {
    EUR("Euro"),
    USD("Dólar americano"),
    CAD("Dólar canadense"),
    JPY("Iene"),
    CNY("Yuan Renminbi");

    private final String descricao;

    TipoMoedaCotacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static List<TipoMoedaCotacao> getTipoMoedaCotacaoList(){
        return new ArrayList<>(EnumSet.allOf(TipoMoedaCotacao.class));
    }
}