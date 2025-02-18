package br.com.lm.controlefinanceiro.carteira;

import br.com.lm.controlefinanceiro.dto.DadosCarteira;
import br.com.lm.controlefinanceiro.dto.DadosNovoUsuario;
import br.com.lm.controlefinanceiro.enums.TipoCarteira;
import br.com.lm.controlefinanceiro.interfaces.CarteiraService;
import br.com.lm.controlefinanceiro.interfaces.UsuarioService;
import br.com.lm.controlefinanceiro.model.Carteira;
import br.com.lm.controlefinanceiro.model.Usuario;
import br.com.lm.controlefinanceiro.repository.CarteiraRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CarteiraServiceTest {
    @Mock
    private CarteiraRepository carteiraRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private CarteiraService carteiraService;

    @Test
    void deveCadastrarCarteira() {
        DadosCarteira dadosCarteira = getDadosCarteira();

        Mockito.when(usuarioService.getDadosUsuarioLogado()).thenReturn(getUsuarioLogado());
        Mockito.when(carteiraRepository.save(Mockito.any(Carteira.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<DadosCarteira> resultado = carteiraService.cadastrarCarteira(dadosCarteira, UriComponentsBuilder.newInstance());

        assertNotNull(resultado);
        assertEquals(dadosCarteira.titulo(), resultado.getBody().titulo());
    }

    private DadosCarteira getDadosCarteira() {
        return new DadosCarteira("Titulo", "Descricao", BigDecimal.ZERO, TipoCarteira.POUPANCA);
    }

    private Usuario getUsuarioLogado() {
        DadosNovoUsuario dadosNovoUsuario = new DadosNovoUsuario("teste", "teste@email.com", "$2a$12$w4rri9K3okgAQ45yiv3wjO/5jYsp/yNM9h5luzu2CLpKs3DsDE.1K");
        return new Usuario(dadosNovoUsuario);
    }
}
