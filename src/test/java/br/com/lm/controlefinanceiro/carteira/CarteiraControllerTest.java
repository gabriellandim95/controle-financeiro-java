package br.com.lm.controlefinanceiro.carteira;

import br.com.lm.controlefinanceiro.controller.CarteiraController;
import br.com.lm.controlefinanceiro.dto.DadosCarteira;
import br.com.lm.controlefinanceiro.enums.TipoCarteira;
import br.com.lm.controlefinanceiro.interfaces.CarteiraService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CarteiraControllerTest {

    private static final String UUID = "d1c9a819-bcd5-462d-92e4-a7fb4d8d6319";
    @InjectMocks
    private CarteiraController carteiraController;

    @Mock
    private CarteiraService carteiraService;

    @Test
    void deveCadastrarCarteira() {
        URI uri = URI.create("http://localhost/api/controlefinanceiro/carteiras");

        UriComponentsBuilder uriComponentsBuilderMock = Mockito.mock(UriComponentsBuilder.class);
        Mockito.when(carteiraService.cadastrarCarteira(Mockito.any(DadosCarteira.class), Mockito.any(UriComponentsBuilder.class)))
                .thenReturn(ResponseEntity.created(uri).body(getDadosCarteira()));

        ResponseEntity<DadosCarteira> response = carteiraController.cadastrarCarteira(getDadosCarteira(), uriComponentsBuilderMock);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(getDadosCarteira(), response.getBody());
    }

    @Test
    void deveAlterarCarteira() {
        Mockito.when(carteiraService.alterarCarteira(Mockito.any(String.class), Mockito.any(DadosCarteira.class)))
                .thenReturn(ResponseEntity.ok().body(getDadosCarteira()));

        ResponseEntity<DadosCarteira> response = carteiraController.alterarCarteira(UUID, getDadosCarteira());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(getDadosCarteira(), response.getBody());
    }

    @Test
    void deveListarCarteiras() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("titulo"));
        Page<DadosCarteira> page = new PageImpl<>(List.of(
                new DadosCarteira("Carteira 1", "Descrição 1", BigDecimal.ZERO, TipoCarteira.POUPANCA),
                new DadosCarteira("Carteira 2", "Descrição 2", BigDecimal.TEN, TipoCarteira.CONTA_CORRENTE)
        ));

        Mockito.when(carteiraService.listarCarteiras(pageable)).thenReturn(ResponseEntity.ok(page));

        ResponseEntity<Page<DadosCarteira>> response = carteiraController.listarCarteiras(pageable);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(page, response.getBody());
    }

    @Test
    void deveDetalharCarteiraPorUuid() {
        DadosCarteira resposta = getDadosCarteira();
        Mockito.when(carteiraService.detalharCarteiraByUuid(UUID)).thenReturn(ResponseEntity.ok(resposta));

        ResponseEntity<DadosCarteira> response = carteiraController.listarCarteiraByUuid(UUID);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(resposta, response.getBody());
    }

    @Test
    void deveDeletarCarteiraPorUuid() {
        Mockito.when(carteiraService.deletarCarteiraByUuid(UUID)).thenReturn(ResponseEntity.noContent().build());

        ResponseEntity<Void> response = carteiraController.deletarCarteiraByUuid(UUID);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    private DadosCarteira getDadosCarteira() {
        return new DadosCarteira("Titulo", "Descricao", BigDecimal.ZERO, TipoCarteira.POUPANCA);
    }
}
