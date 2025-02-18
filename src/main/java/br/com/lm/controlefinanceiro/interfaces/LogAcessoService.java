package br.com.lm.controlefinanceiro.interfaces;

import br.com.lm.controlefinanceiro.enums.TipoLogEvento;

public interface LogAcessoService {
    void gerarEvento(String usuario, String evento, TipoLogEvento tipoLogEvento);
}
