package br.com.controlefinanceiro.interfaces;

import br.com.controlefinanceiro.enums.TipoLogEvento;

public interface LogAcessoService {
    void gerarEvento(String usuario, String evento, TipoLogEvento tipoLogEvento);
}
