package br.com.lm.controlefinanceiro.carteira;

import br.com.lm.controlefinanceiro.dto.DadosCarteira;
import br.com.lm.controlefinanceiro.dto.DadosNovoUsuario;
import br.com.lm.controlefinanceiro.enums.TipoCarteira;
import br.com.lm.controlefinanceiro.enums.TipoLogEvento;
import br.com.lm.controlefinanceiro.interfaces.LogAcessoService;
import br.com.lm.controlefinanceiro.interfaces.MessageService;
import br.com.lm.controlefinanceiro.interfaces.UsuarioService;
import br.com.lm.controlefinanceiro.model.Carteira;
import br.com.lm.controlefinanceiro.model.Usuario;
import br.com.lm.controlefinanceiro.repository.CarteiraRepository;
import br.com.lm.controlefinanceiro.service.CarteiraServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CarteiraServiceTest {

    private static final String LISTAGE_DE_CARTEIRAS = "Listagem de carteiras";

    @Mock
    private CarteiraRepository carteiraRepository;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private LogAcessoService logAcessoService;
    @InjectMocks
    private CarteiraServiceImpl carteiraService;

    @Test
    void deveCadastrarCarteira() {
        DadosCarteira dadosCarteira = getDadosCarteira();
        Usuario usuarioLogado = getUsuarioLogado();
        URI uriEsperada = URI.create("http://localhost/carteiras/");

        Mockito.when(usuarioService.getDadosUsuarioLogado()).thenReturn(usuarioLogado);
        Mockito.when(carteiraRepository.save(Mockito.any(Carteira.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UriComponentsBuilder uriBuilderMock = Mockito.mock(UriComponentsBuilder.class);
        UriComponents uriComponentsMock = Mockito.mock(UriComponents.class);

        Mockito.when(uriBuilderMock.path(Mockito.anyString())).thenReturn(uriBuilderMock);
        Mockito.when(uriBuilderMock.buildAndExpand(Mockito.anyString())).thenReturn(uriComponentsMock);
        Mockito.when(uriComponentsMock.toUri()).thenReturn(uriEsperada);

        ResponseEntity<DadosCarteira> resultado = carteiraService.cadastrarCarteira(dadosCarteira, uriBuilderMock);

        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(dadosCarteira.titulo(), resultado.getBody().titulo());
        assertEquals(uriEsperada, resultado.getHeaders().getLocation());
    }

    @Test
    void deveAlterarCarteira() {
        DadosCarteira dadosCarteira = getDadosCarteira();
        Usuario usuarioLogado = getUsuarioLogado();
        Carteira carteiraExistente = getCarteiraValida();

        Mockito.when(usuarioService.getDadosUsuarioLogado()).thenReturn(usuarioLogado);
        Mockito.when(carteiraRepository.findByUuid(Mockito.anyString())).thenReturn(Optional.of(carteiraExistente));
        Mockito.when(carteiraRepository.save(Mockito.any(Carteira.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<DadosCarteira> resultado = carteiraService.alterarCarteira(carteiraExistente.getUuid(), dadosCarteira);

        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(dadosCarteira.titulo(), resultado.getBody().titulo());
        Mockito.verify(carteiraRepository).save(Mockito.any(Carteira.class));
    }

    @Test
    void deveListarCarteiras() {
        Usuario usuarioLogado = getUsuarioLogado();
        List<Carteira> carteiras = List.of(getCarteiraValida(), getCarteiraValida());
        Pageable pageable = PageRequest.of(0, 10, Sort.by("titulo"));
        Page<Carteira> pageCarteiras = new PageImpl<>(carteiras, pageable, carteiras.size());

        Mockito.when(usuarioService.getDadosUsuarioLogado()).thenReturn(usuarioLogado);
        Mockito.when(carteiraRepository.findAllByUsuario(pageable, usuarioLogado)).thenReturn(pageCarteiras);

        ResponseEntity<Page<DadosCarteira>> resultado = carteiraService.listarCarteiras(pageable);
        assertNotNull(resultado);
        assertNotNull(resultado.getBody());
        assertEquals(2, resultado.getBody().getTotalElements());
        assertEquals(carteiras.get(0).getTitulo(), resultado.getBody().getContent().get(0).titulo());
        assertEquals(HttpStatus.OK, resultado.getStatusCode());

        Mockito.verify(usuarioService, Mockito.times(3)).getDadosUsuarioLogado();
        Mockito.verify(carteiraRepository, Mockito.times(1)).findAllByUsuario(pageable, usuarioLogado);
        Mockito.verify(logAcessoService, Mockito.times(1)).gerarEvento(
                Mockito.eq(usuarioLogado.getLogin()),
                Mockito.eq(LISTAGE_DE_CARTEIRAS),
                Mockito.eq(TipoLogEvento.ACESSO_A_LISTAGEM)
        );
    }


    private DadosCarteira getDadosCarteira() {
        return new DadosCarteira("Titulo", "Descricao", BigDecimal.ZERO, TipoCarteira.POUPANCA);
    }

    private Usuario getUsuarioLogado() {
        DadosNovoUsuario dadosNovoUsuario = new DadosNovoUsuario("teste", "teste@email.com", "$2a$12$w4rri9K3okgAQ45yiv3wjO/5jYsp/yNM9h5luzu2CLpKs3DsDE.1K");
        return new Usuario(dadosNovoUsuario);
    }

    private Carteira getCarteiraValida() {
        Carteira carteira = new Carteira();
        carteira.setUuid("3031b1e3-728b-49b0-ba29-9e7db2efc254");
        carteira.setTitulo("Carteira de Teste");
        carteira.setDescricao("Descrição de teste");
        carteira.setUsuario(getUsuarioLogado());
        return carteira;
    }
}
