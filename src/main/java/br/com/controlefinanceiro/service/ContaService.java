package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.dto.DadosDetalhamentoConta;
import br.com.controlefinanceiro.dto.DadosConta;
import br.com.controlefinanceiro.enums.StatusConta;
import br.com.controlefinanceiro.enums.TipoLogEvento;
import br.com.controlefinanceiro.infra.exceptions.RegistroNotFoundException;
import br.com.controlefinanceiro.model.Carteira;
import br.com.controlefinanceiro.model.Conta;
import br.com.controlefinanceiro.model.HistoricoPagamento;
import br.com.controlefinanceiro.repository.CarteiraRepository;
import br.com.controlefinanceiro.repository.ContaRepository;
import br.com.controlefinanceiro.repository.HistoricoPagamentoRespository;
import br.com.controlefinanceiro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.util.logging.Logger;

@Service
public class ContaService {

    private static Logger LOGGER = Logger.getLogger(ContaService.class.getName());
    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private CarteiraRepository carteiraRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private LogAcessoService logAcessoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private HistoricoPagamentoRespository historicoPagamentoRespository;

    public ContaService() {
    }

    public ResponseEntity cadastrarConta(DadosConta dados, UriComponentsBuilder uriBuilder) {
        Conta novaConta = new Conta(dados);
        logAcessoService.gerarEvento(usuarioService.getDadosUsuario().getLogin(), "Nova Conta", TipoLogEvento.ACESSO_A_TELA_DE_CRIACAO);

        novaConta.setStatusConta(StatusConta.EM_ABERTO);
        novaConta.setUsuario(usuarioService.getDadosUsuario());
        contaRepository.save(novaConta);

        URI uri = uriBuilder.path("/conta/{id}").buildAndExpand(novaConta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoConta(novaConta));
    }

    public ResponseEntity alterarConta(String uuid, DadosConta dados) {
        Optional<Conta> conta = contaRepository.findByUuid(uuid);

        if (conta.isPresent()) {
            logAcessoService.gerarEvento(usuarioService.getDadosUsuario().getLogin(), conta.toString(), TipoLogEvento.ACESSO_A_TELA_DE_EDICAO);

            conta.get().setTitulo(dados.titulo());
            conta.get().setDescricao(dados.descricao());
            conta.get().setDataVencimento(dados.dataVencimento());
            conta.get().setValor(dados.valor());
            conta.get().setStatusConta(dados.statusConta());
            conta.get().setUsuario(usuarioService.getDadosUsuario());
            contaRepository.save(conta.get());

            return ResponseEntity.ok(new DadosDetalhamentoConta(conta.get()));
        }
        throw new RegistroNotFoundException("Conta");
    }

    public ResponseEntity<Page<DadosDetalhamentoConta>> listarContas(Pageable pageable) {
        Page page;
        if (usuarioService.getDadosUsuario().getNivelAcesso().equals("ROLE_ADMIN")) {
            page = contaRepository.findAll(pageable).map(DadosDetalhamentoConta::new);
        } else {
            page = contaRepository.findAllByUsuario(pageable, usuarioService.getDadosUsuario()).map(DadosDetalhamentoConta::new);
        }

        logAcessoService.gerarEvento(usuarioService.getDadosUsuario().getLogin(), "Listagem de contas", TipoLogEvento.ACESSO_A_LISTAGEM);
        return ResponseEntity.ok(page);
    }

    public ResponseEntity listarContaByUuid(String uuid) {
        Optional<Conta> conta = contaRepository.findByUuid(uuid);
        if (conta.isPresent()) {
            logAcessoService.gerarEvento(usuarioService.getDadosUsuario().getUsername(), conta.toString(), TipoLogEvento.ACESSO_A_LISTAR_POR_ID);
            return ResponseEntity.ok(new DadosDetalhamentoConta(conta.get()));
        }
        throw new RegistroNotFoundException("Conta");
    }

    public ResponseEntity deletarByUuid(String uuid) {
        Optional<Conta> conta = contaRepository.findByUuid(uuid);
        if (conta.isPresent()) {
            contaRepository.delete(conta.get());
            return ResponseEntity.noContent().build();
        }
        throw new RegistroNotFoundException("Conta");
    }

    public ResponseEntity pagarContaByUuid(String uuidConta, String uuidCarteira) {
        Optional<Conta> conta = contaRepository.findByUuid(uuidConta);
        Optional<Carteira> carteira = carteiraRepository.findByUuid(uuidCarteira);

        if (conta.isPresent()){
            conta.get().setStatusConta(StatusConta.PAGO);
            carteira.get().setSaldo(carteira.get().getSaldo().subtract(conta.get().getValor()));

            contaRepository.save(conta.get());
            carteiraRepository.save(carteira.get());

            HistoricoPagamento historicoPagamento = new HistoricoPagamento(carteira.get(), conta.get(), usuarioService.getDadosUsuario());
            historicoPagamentoRespository.save(historicoPagamento);
        }else {
            throw new RegistroNotFoundException("Conta");
        }
        return ResponseEntity.ok().build();
    }

    @Scheduled(cron = "0 0 5 * * *") //fixedRate = 5000 | cro = "0 0 6 * * *"
    private void verificarContasPertoDeVencer() {
        LOGGER.info("Verificando contas perto do venciemnto: ");

        List<Conta> contas = contaRepository.findAll();
        if (!contas.isEmpty()) {
            for (Conta contaPertoDeVencer : contas) {
                if (!Arrays.asList(StatusConta.getPagoVencidoCancelado()).contains(contaPertoDeVencer.getStatusConta())) {
                    LocalDate dataVencimento = contaPertoDeVencer.getDataVencimento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate dataAtual = LocalDate.now();
                    Long diasRestantes = ChronoUnit.DAYS.between(dataAtual, dataVencimento);

                    if (diasRestantes <= 5 && diasRestantes >= 1) {
                        LOGGER.info(String.format("Alterando o status da conta %s para PERTO DE VENCER.", contaPertoDeVencer.getTitulo()));
                        contaPertoDeVencer.setStatusConta(StatusConta.PERTO_DE_VENCER);
                        contaRepository.save(contaPertoDeVencer);
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 0 6 * * *") //fixedRate = 5000 | cro = "0 0 6 * * *"
    private void verificarContasVencidas() {
        LOGGER.info("Verificando contas vencidas: ");
        List<Conta> contas = contaRepository.findAllByDataVencimentoBefore(new Date());
        if (!contas.isEmpty()) {
            for (Conta contaVencida : contas) {
                if (!Arrays.asList(StatusConta.getPagoVencidoCancelado()).contains(contaVencida.getStatusConta())) {
                    LOGGER.info(String.format("Alterando o status da conta %s para VENCIDA.", contaVencida.getTitulo()));
                    contaVencida.setStatusConta(StatusConta.VENCIDA);
                    contaRepository.save(contaVencida);
                }
            }
        }
    }
}
