package br.com.lm.controlefinanceiro.interfaces;

import br.com.lm.controlefinanceiro.dto.DadosGeraisCotacaoMoeda;
import org.springframework.http.ResponseEntity;

public interface CotacaoMoedaService {
    ResponseEntity<DadosGeraisCotacaoMoeda> getCotacao(String codigoMoeda);
}
