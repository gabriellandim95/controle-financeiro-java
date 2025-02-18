package br.com.controlefinanceiro.interfaces;

import br.com.controlefinanceiro.dto.DadosCarteira;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

public interface CarteiraService {
    ResponseEntity<DadosCarteira> cadastrarCarteira(DadosCarteira dados, UriComponentsBuilder uriBuilder);
    ResponseEntity<DadosCarteira> alterarCarteira(String uuid, DadosCarteira dados);
    ResponseEntity<Page<DadosCarteira>> listarCarteiras(Pageable pageable);
    ResponseEntity listarCarteiraByUuid(String uuid);
    ResponseEntity deletarCarteiraByUuid(String uuid);
}
