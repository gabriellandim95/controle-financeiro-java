package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.dto.DadosCarteira;
import br.com.controlefinanceiro.enums.TipoLogEvento;
import br.com.controlefinanceiro.infra.exceptions.RegistroNaoEncontradoException;
import br.com.controlefinanceiro.interfaces.CarteiraService;
import br.com.controlefinanceiro.model.Carteira;
import br.com.controlefinanceiro.repository.CarteiraRepository;
import br.com.controlefinanceiro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarteiraServiceImpl implements CarteiraService {

    private CarteiraRepository carteiraRepository;
    private UsuarioRepository usuarioRepository;
    private LogAcessoServiceImpl logAcessoServiceImpl;
    private UsuarioServiceImpl usuarioServiceImpl;

    @Override
    public ResponseEntity<DadosCarteira> cadastrarCarteira(DadosCarteira dados, UriComponentsBuilder uriBuilder) {
        log.info("teste");
        logAcessoServiceImpl.gerarEvento(usuarioServiceImpl.getDadosUsuario().getLogin(), "Nova Conta", TipoLogEvento.ACESSO_A_TELA_DE_CRIACAO);

        Carteira novaCarteira = new Carteira(dados, usuarioServiceImpl.getDadosUsuario());
        carteiraRepository.save(novaCarteira);

        URI uri = uriBuilder.path("/carteira/{uuid}").buildAndExpand(novaCarteira.getUuid()).toUri();
        return ResponseEntity.created(uri).body(new DadosCarteira(novaCarteira));
    }

    @Override
    public ResponseEntity<DadosCarteira> alterarCarteira(String uuid, DadosCarteira dados) {
        Optional<Carteira> carteira = carteiraRepository.findByUuid(uuid);

        if (carteira.isPresent()){
            logAcessoServiceImpl.gerarEvento(usuarioServiceImpl.getDadosUsuario().getLogin(), carteira.get().toString(), TipoLogEvento.ACESSO_A_TELA_DE_EDICAO);

            carteira.get().setTitulo(dados.titulo());
            carteira.get().setDescricao(dados.descricao());
            carteira.get().setSaldo(dados.saldo());
            carteira.get().setTipoCarteira(dados.tipoCarteira());
            carteiraRepository.save(carteira.get());

            return ResponseEntity.ok(new DadosCarteira(carteira.get()));
        }
        throw new RegistroNaoEncontradoException("Carteira");
    }

    @Override
    public ResponseEntity<Page<DadosCarteira>> listarCarteiras(Pageable pageable) {
        Page page;
        if (usuarioServiceImpl.getDadosUsuario().getNivelAcesso().equals("ROLE_ADMIN")){
            page = carteiraRepository.findAll(pageable).map(DadosCarteira::new);
        }else {
            page = carteiraRepository.findAllByUsuario(pageable, usuarioServiceImpl.getDadosUsuario()).map(DadosCarteira::new);
        }

        logAcessoServiceImpl.gerarEvento(usuarioServiceImpl.getDadosUsuario().getLogin(), "Listagem de carteiras", TipoLogEvento.ACESSO_A_LISTAGEM);
        return ResponseEntity.ok(page);
    }

    @Override
    public ResponseEntity listarCarteiraByUuid(String uuid) {
        Optional<Carteira> carteira = carteiraRepository.findByUuid(uuid);
        logAcessoServiceImpl.gerarEvento(usuarioServiceImpl.getDadosUsuario().getUsername(), carteira.get().toString(), TipoLogEvento.ACESSO_A_LISTAR_POR_ID);

        return carteira.isPresent() ? ResponseEntity.ok(new DadosCarteira(carteira.get())) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RegistroNaoEncontradoException("Carteira"));
    }

    @Override
    public ResponseEntity deletarCarteiraByUuid(String uuid) {
        Optional<Carteira> carteira = carteiraRepository.findByUuid(uuid);
        if (carteira.isPresent()){
            carteiraRepository.delete(carteira.get());
            return ResponseEntity.noContent().build();
        }
        throw new RegistroNaoEncontradoException("Carteira");
    }
}
