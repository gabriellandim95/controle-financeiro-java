package br.com.lm.controlefinanceiro.service;

import br.com.lm.controlefinanceiro.dto.DadosCarteira;
import br.com.lm.controlefinanceiro.enums.TipoLogEvento;
import br.com.lm.controlefinanceiro.exceptions.AutorException;
import br.com.lm.controlefinanceiro.exceptions.RegistroNaoEncontradoException;
import br.com.lm.controlefinanceiro.interfaces.CarteiraService;
import br.com.lm.controlefinanceiro.interfaces.LogAcessoService;
import br.com.lm.controlefinanceiro.interfaces.MessageService;
import br.com.lm.controlefinanceiro.interfaces.UsuarioService;
import br.com.lm.controlefinanceiro.model.Carteira;
import br.com.lm.controlefinanceiro.repository.CarteiraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarteiraServiceImpl implements CarteiraService {

    private static final String NOVA_CONTA = "Nova Conta";
    private static final String LISTAGE_DE_CARTEIRAS = "Listagem de carteiras";
    private final CarteiraRepository carteiraRepository;
    private final LogAcessoService logAcessoService;
    private final UsuarioService usuarioService;
    private final MessageService messageService;

    @Override
    public ResponseEntity<DadosCarteira> cadastrarCarteira(DadosCarteira dados, UriComponentsBuilder uriBuilder) {
        logAcessoService.gerarEvento(usuarioService.getDadosUsuarioLogado().getLogin(), NOVA_CONTA, TipoLogEvento.ACESSO_A_TELA_DE_CRIACAO);

        Carteira novaCarteira = new Carteira(dados, usuarioService.getDadosUsuarioLogado());
        carteiraRepository.save(novaCarteira);

        URI uri = uriBuilder.path("/carteiras/{uuid}").buildAndExpand(novaCarteira.getUuid()).toUri();
        log.info("Usuario {} cadastrou a carteira {} com sucesso.", novaCarteira.getUsuario().getLogin(), novaCarteira.getTitulo());
        return ResponseEntity.created(uri).body(new DadosCarteira(novaCarteira));
    }

    @Override
    public ResponseEntity<DadosCarteira> alterarCarteira(String uuid, DadosCarteira dados) {
        Carteira carteira = getCarteiraByUuid(uuid);
        verificaAutorDaCarteira(carteira);

        logAcessoService.gerarEvento(usuarioService.getDadosUsuarioLogado().getLogin(), carteira.toString(), TipoLogEvento.ACESSO_A_TELA_DE_EDICAO);

        carteira.setTitulo(dados.titulo());
        carteira.setDescricao(dados.descricao());
        carteira.setSaldo(dados.saldo());
        carteira.setTipoCarteira(dados.tipoCarteira());
        carteiraRepository.save(carteira);

        log.info("Usuario {} alterou a carteira {} com sucesso.", carteira.getUsuario().getLogin(), carteira.getTitulo());
        return ResponseEntity.ok(new DadosCarteira(carteira));
    }

    @Override
    public ResponseEntity<Page<DadosCarteira>> listarCarteiras(Pageable pageable) {
        Page page = carteiraRepository.findAllByUsuario(pageable, usuarioService.getDadosUsuarioLogado()).map(DadosCarteira::new);

        logAcessoService.gerarEvento(usuarioService.getDadosUsuarioLogado().getLogin(), LISTAGE_DE_CARTEIRAS, TipoLogEvento.ACESSO_A_LISTAGEM);
        log.info("Usuario {} listou todas as suas carteiras.", usuarioService.getDadosUsuarioLogado().getLogin());
        return ResponseEntity.ok(page);
    }

    @Override
    public ResponseEntity<DadosCarteira> detalharCarteiraByUuid(String uuid) {
        Carteira carteira = getCarteiraByUuid(uuid);
        verificaAutorDaCarteira(carteira);
        logAcessoService.gerarEvento(usuarioService.getDadosUsuarioLogado().getUsername(), carteira.toString(), TipoLogEvento.ACESSO_A_LISTAR_POR_ID);

        log.info("Usuario {} acessou a carteira {}.", carteira.getUsuario().getLogin(), carteira.getTitulo());
        return ResponseEntity.ok(new DadosCarteira(carteira));
    }

    @Override
    public ResponseEntity<Void> deletarCarteiraByUuid(String uuid) {
        Carteira carteira = getCarteiraByUuid(uuid);
        verificaAutorDaCarteira(carteira);
        carteiraRepository.delete(carteira);

        log.info("Usuario {} deletou a carteira {} com sucesso.", carteira.getUsuario().getLogin(), carteira.getTitulo());
        return ResponseEntity.noContent().build();

    }

    private Carteira getCarteiraByUuid(String uuid) {
        Carteira carteira = carteiraRepository.findByUuid(uuid)
                .orElseThrow(() -> new RegistroNaoEncontradoException(messageService.getMessage("api.carteira.id.nao.encontrada", uuid)));

        log.info("Usuario {} obtendo dados da carteira {}", carteira.getUsuario().getLogin(), carteira.getTitulo());
        return carteira;
    }

    private void verificaAutorDaCarteira(Carteira carteira) {
        if (!usuarioService.getDadosUsuarioLogado().getEmail().equals(carteira.getUsuario().getEmail())) {
            throw new AutorException(messageService.getMessage("api.carteira.acesso.negado"));
        }
    }
}
