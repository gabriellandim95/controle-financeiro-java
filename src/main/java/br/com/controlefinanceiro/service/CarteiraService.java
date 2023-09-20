package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.dto.DadosCarteira;
import br.com.controlefinanceiro.enums.TipoLogEvento;
import br.com.controlefinanceiro.infra.exceptions.CarteiraNotFoundException;
import br.com.controlefinanceiro.model.Carteira;
import br.com.controlefinanceiro.repository.CarteiraRepository;
import br.com.controlefinanceiro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

        URI uri = uriBuilder.path("/carteira/{id}").buildAndExpand(novaCarteira.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosCarteira(novaCarteira));
    }

    public ResponseEntity alterarCarteira(Long id, DadosCarteira dados) {
        Optional<Carteira> carteira = carteiraRepository.findById(id);

        if (carteira.isPresent()){
            logAcessoService.gerarEvento(usuarioService.getDadosUsuario().getLogin(), carteira.toString(), TipoLogEvento.ACESSO_A_TELA_DE_EDICAO);

            carteira.get().setTitulo(dados.titulo());
            carteira.get().setDescricao(dados.descricao());
            carteira.get().setValor(dados.valor());
            carteira.get().setTipoCarteira(dados.tipoCarteira());
            carteiraRepository.save(carteira.get());

            return ResponseEntity.ok(new DadosCarteira(carteira.get()));
        }
        throw new CarteiraNotFoundException();
    }
}
