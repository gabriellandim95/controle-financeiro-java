package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.dto.DadosCarteira;
import br.com.controlefinanceiro.enums.TipoLogEvento;
import br.com.controlefinanceiro.infra.exceptions.RegistroNotFoundException;
import br.com.controlefinanceiro.model.Carteira;
import br.com.controlefinanceiro.repository.CarteiraRepository;
import br.com.controlefinanceiro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class CarteiraService {

    @Autowired
    private CarteiraRepository carteiraRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private LogAcessoService logAcessoService;
    @Autowired
    private UsuarioService usuarioService;

    public ResponseEntity cadastrarCarteira(DadosCarteira dados, UriComponentsBuilder uriBuilder) {
        logAcessoService.gerarEvento(usuarioService.getDadosUsuario().getLogin(), "Nova Conta", TipoLogEvento.ACESSO_A_TELA_DE_CRIACAO);

        Carteira novaCarteira = new Carteira(dados, usuarioService.getDadosUsuario());
        carteiraRepository.save(novaCarteira);

        URI uri = uriBuilder.path("/carteira/{uuid}").buildAndExpand(novaCarteira.getUuid()).toUri();
        return ResponseEntity.created(uri).body(new DadosCarteira(novaCarteira));
    }

    public ResponseEntity alterarCarteira(String uuid, DadosCarteira dados) {
        Optional<Carteira> carteira = carteiraRepository.findByUuid(uuid);

        if (carteira.isPresent()){
            logAcessoService.gerarEvento(usuarioService.getDadosUsuario().getLogin(), carteira.get().toString(), TipoLogEvento.ACESSO_A_TELA_DE_EDICAO);

            carteira.get().setTitulo(dados.titulo());
            carteira.get().setDescricao(dados.descricao());
            carteira.get().setSaldo(dados.saldo());
            carteira.get().setTipoCarteira(dados.tipoCarteira());
            carteiraRepository.save(carteira.get());

            return ResponseEntity.ok(new DadosCarteira(carteira.get()));
        }
        throw new RegistroNotFoundException("Carteira");
    }

    public ResponseEntity<Page<DadosCarteira>> listarCarteiras(Pageable pageable) {
        Page page;
        if (usuarioService.getDadosUsuario().getNivelAcesso().equals("ROLE_ADMIN")){
            page = carteiraRepository.findAll(pageable).map(DadosCarteira::new);
        }else {
            page = carteiraRepository.findAllByUsuario(pageable, usuarioService.getDadosUsuario()).map(DadosCarteira::new);
        }

        logAcessoService.gerarEvento(usuarioService.getDadosUsuario().getLogin(), "Listagem de carteiras", TipoLogEvento.ACESSO_A_LISTAGEM);
        return ResponseEntity.ok(page);
    }

    public ResponseEntity listarCarteiraByUuid(String uuid) {
        Optional<Carteira> carteira = carteiraRepository.findByUuid(uuid);
        logAcessoService.gerarEvento(usuarioService.getDadosUsuario().getUsername(), carteira.get().toString(), TipoLogEvento.ACESSO_A_LISTAR_POR_ID);

        return carteira.isPresent() ? ResponseEntity.ok(new DadosCarteira(carteira.get())) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RegistroNotFoundException("Carteira"));
    }

    public ResponseEntity deletarCarteiraByUuid(String uuid) {
        Optional<Carteira> carteira = carteiraRepository.findByUuid(uuid);
        if (carteira.isPresent()){
            carteiraRepository.delete(carteira.get());
            return ResponseEntity.noContent().build();
        }
        throw new RegistroNotFoundException("Carteira");
    }
}
