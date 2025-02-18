package br.com.controlefinanceiro.interfaces;

import br.com.controlefinanceiro.dto.DadosGeraisCotacaoMoeda;
import org.springframework.http.ResponseEntity;

public interface CotacaoMoedaService {
    ResponseEntity<DadosGeraisCotacaoMoeda> getCotacao(String codigoMoeda);
}
