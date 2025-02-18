package br.com.lm.controlefinanceiro.interfaces;

import br.com.lm.controlefinanceiro.dto.DadosConta;
import br.com.lm.controlefinanceiro.dto.DadosDetalhamentoConta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public interface ContaService {
    ResponseEntity<DadosDetalhamentoConta> cadastrarConta(DadosConta dados, UriComponentsBuilder uriBuilder);
    ResponseEntity<DadosDetalhamentoConta> alterarConta(String uuid, DadosConta dados);
    ResponseEntity<Page<DadosDetalhamentoConta>> listarContas(Pageable pageable);
    ResponseEntity<Void> deletarByUuid(String uuid);
    ResponseEntity<Void> pagarContaByUuid(String uuidConta, String uuidCarteira);
    ResponseEntity<DadosDetalhamentoConta> listarContaByUuid(String uuid);
}
