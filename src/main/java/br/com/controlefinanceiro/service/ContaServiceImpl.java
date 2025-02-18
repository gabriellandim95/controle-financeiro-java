package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.dto.DadosDetalhamentoConta;
import br.com.controlefinanceiro.dto.DadosConta;
import br.com.controlefinanceiro.enums.StatusConta;
import br.com.controlefinanceiro.enums.TipoLogEvento;
import br.com.controlefinanceiro.infra.exceptions.AutorException;
import br.com.controlefinanceiro.infra.exceptions.ContaException;
import br.com.controlefinanceiro.infra.exceptions.RegistroNaoEncontradoException;
import br.com.controlefinanceiro.interfaces.ContaService;
import br.com.controlefinanceiro.interfaces.LogAcessoService;
import br.com.controlefinanceiro.interfaces.MessageService;
import br.com.controlefinanceiro.interfaces.UsuarioService;
import br.com.controlefinanceiro.model.Carteira;
import br.com.controlefinanceiro.model.Conta;
import br.com.controlefinanceiro.model.HistoricoPagamento;
import br.com.controlefinanceiro.repository.CarteiraRepository;
import br.com.controlefinanceiro.repository.ContaRepository;
import br.com.controlefinanceiro.repository.HistoricoPagamentoRespository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContaServiceImpl implements ContaService {

    private final ContaRepository contaRepository;
    private final CarteiraRepository carteiraRepository;
    private final LogAcessoService logAcessoService;
    private final UsuarioService usuarioService;
    private final HistoricoPagamentoRespository historicoPagamentoRespository;
    private final MessageService messageService;

    @Override
    public ResponseEntity<DadosDetalhamentoConta> cadastrarConta(DadosConta dados, UriComponentsBuilder uriBuilder) {
        Conta novaConta = new Conta(dados);
        logAcessoService.gerarEvento(usuarioService.getDadosUsuarioLogado().getLogin(), "Nova Conta", TipoLogEvento.ACESSO_A_TELA_DE_CRIACAO);

        novaConta.setStatusConta(StatusConta.EM_ABERTO);
        novaConta.setUsuario(usuarioService.getDadosUsuarioLogado());
        contaRepository.save(novaConta);

        URI uri = uriBuilder.path("/conta/{id}").buildAndExpand(novaConta.getId()).toUri();

        log.info("Usuario {} cadastrou a conta {}", novaConta.getUsuario().getLogin(), novaConta.getTitulo());
        return ResponseEntity.created(uri).body(new DadosDetalhamentoConta(novaConta));
    }

    @Override
    public ResponseEntity<DadosDetalhamentoConta> alterarConta(String uuid, DadosConta dados) {
        Conta conta = getContaByUuid(uuid);
        verificaAutorDaConta(conta);

        logAcessoService.gerarEvento(usuarioService.getDadosUsuarioLogado().getLogin(), conta.toString(), TipoLogEvento.ACESSO_A_TELA_DE_EDICAO);

        conta.setTitulo(dados.titulo());
        conta.setDescricao(dados.descricao());
        conta.setDataVencimento(dados.dataVencimento());
        conta.setValor(dados.valor());
        conta.setStatusConta(dados.statusConta());
        conta.setUsuario(usuarioService.getDadosUsuarioLogado());
        contaRepository.save(conta);

        log.info("Usuario {} alterou a conta {}", usuarioService.getDadosUsuarioLogado().getLogin(), conta.getTitulo());
        return ResponseEntity.ok(new DadosDetalhamentoConta(conta));

    }

    @Override
    public ResponseEntity<Page<DadosDetalhamentoConta>> listarContas(Pageable pageable) {
        Page page = contaRepository.findAllByUsuario(pageable, usuarioService.getDadosUsuarioLogado()).map(DadosDetalhamentoConta::new);

        logAcessoService.gerarEvento(usuarioService.getDadosUsuarioLogado().getLogin(), "Listagem de contas", TipoLogEvento.ACESSO_A_LISTAGEM);
        log.info("Usuario {} listou suas contas.", usuarioService.getDadosUsuarioLogado().getLogin());
        return ResponseEntity.ok(page);
    }

    public ResponseEntity<DadosDetalhamentoConta> listarContaByUuid(String uuid) {
        Conta conta = getContaByUuid(uuid);
        verificaAutorDaConta(conta);

        logAcessoService.gerarEvento(usuarioService.getDadosUsuarioLogado().getUsername(), conta.toString(), TipoLogEvento.ACESSO_A_LISTAR_POR_ID);
        log.info("Usuario {} acessou a conta {}.", conta.getUsuario().getLogin(), conta.getTitulo());
        return ResponseEntity.ok(new DadosDetalhamentoConta(conta));
    }

    @Override
    public ResponseEntity<Void> deletarByUuid(String uuid) {
        Conta conta = getContaByUuid(uuid);
        verificaAutorDaConta(conta);
        contaRepository.delete(conta);

        log.info("Usuario {} deletou a conta {} com sucesso.", conta.getUsuario().getLogin(), conta.getTitulo());
        return ResponseEntity.noContent().build();
    }

    @Override
    @Transactional
    public ResponseEntity<Void> pagarContaByUuid(String uuidConta, String uuidCarteira) {
        Conta conta = getContaByUuid(uuidConta);
        verificaAutorDaConta(conta);

        if (conta.getStatusConta() == StatusConta.PAGO) {
            throw new ContaException(messageService.getMessage("api.conta.ja.paga", conta.getTitulo()));
        }

        Carteira carteira = carteiraRepository.findByUuid(uuidCarteira)
                .orElseThrow(() -> new RegistroNaoEncontradoException(
                        messageService.getMessage("api.carteira.id.nao.encontrada", uuidCarteira)));

        if (carteira.getSaldo().compareTo(conta.getValor()) < 0) {
            throw new ContaException(messageService.getMessage("api.carteira.saldo.insuficiente"));
        }

        conta.setStatusConta(StatusConta.PAGO);
        carteira.setSaldo(carteira.getSaldo().subtract(conta.getValor()));

        contaRepository.save(conta);
        carteiraRepository.save(carteira);

        HistoricoPagamento historicoPagamento = new HistoricoPagamento(carteira, conta, usuarioService.getDadosUsuarioLogado());
        historicoPagamentoRespository.save(historicoPagamento);

        log.info("Usuario {} pagou a conta {} utilizando a carteira {} com sucesso.",
                usuarioService.getDadosUsuarioLogado().getLogin(), uuidConta, uuidCarteira);

        return ResponseEntity.ok().build();
    }

    private Conta getContaByUuid(String uuid) {
        Conta conta = contaRepository.findByUuid(uuid)
                .orElseThrow(() -> new RegistroNaoEncontradoException(messageService.getMessage("api.conta.id.nao.encontrada", uuid)));

        log.info("Usuario {} obtendo dados da conta {}", conta.getUsuario().getLogin(), conta.getTitulo());
        return conta;
    }

    private void verificaAutorDaConta(Conta conta) {
        if (!usuarioService.getDadosUsuarioLogado().getEmail().equals(conta.getUsuario().getEmail())) {
            throw new AutorException(messageService.getMessage("api.conta.acesso.negado"));
        }
    }
}
