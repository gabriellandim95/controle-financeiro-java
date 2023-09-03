package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.dto.DadosDetalhamentoConta;
import br.com.controlefinanceiro.dto.DadosConta;
import br.com.controlefinanceiro.enums.StatusConta;
import br.com.controlefinanceiro.enums.TipoLogEvento;
import br.com.controlefinanceiro.model.Conta;
import br.com.controlefinanceiro.model.Usuario;
import br.com.controlefinanceiro.repository.ContaRepository;
import br.com.controlefinanceiro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ContaService {

    private static Logger LOGGER = Logger.getLogger(ContaService.class.getName());
    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private LogAcessoService logAcessoService;

    public ResponseEntity cadastrarConta(DadosConta dados, UriComponentsBuilder uriBuilder, UserDetails usuarioLogado) {
        Conta novaConta = new Conta(dados);
        Usuario usuario = (Usuario) usuarioRepository.findByLogin(usuarioLogado.getUsername());
        logAcessoService.gerarEvento(usuarioLogado.getUsername(), "", TipoLogEvento.ACESSO_A_TELA_DE_CRIACAO);

        novaConta.setStatusConta(StatusConta.EM_ABERTO);
        novaConta.setUsuario(usuario);
        contaRepository.save(novaConta);

        URI uri = uriBuilder.path("/conta/{id}").buildAndExpand(novaConta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoConta(novaConta));
    }

    public ResponseEntity alterarConta(Integer id, DadosConta dados, UserDetails usuarioLogado) {
        Conta conta = contaRepository.findFirstById(id);
        Usuario usuario = (Usuario) usuarioRepository.findByLogin(usuarioLogado.getUsername());
        logAcessoService.gerarEvento(usuarioLogado.getUsername(), conta.toString(), TipoLogEvento.ACESSO_A_TELA_DE_EDICAO);

        if (conta != null) {
            conta.setTitulo(dados.titulo());
            conta.setDescricao(dados.descricao());
            conta.setDataVencimento(dados.dataVencimento());
            conta.setValor(dados.valor());
            conta.setStatusConta(dados.statusConta());
            conta.setUsuario(usuario);
            contaRepository.save(conta);
        }
        return ResponseEntity.ok(new DadosDetalhamentoConta(conta));
    }

    public ResponseEntity<Page<DadosDetalhamentoConta>> listarContas(Pageable pageable, UserDetails usuarioLogado) {
        Page page;
        Usuario usuario = (Usuario) usuarioRepository.findByLogin(usuarioLogado.getUsername());
        if (usuario.getNivelAcesso().equals("ROLE_ADMIN")) {
            page = contaRepository.findAll(pageable).map(DadosDetalhamentoConta::new);
        } else {
            page = contaRepository.findAllByUsuario(pageable, usuario).map(DadosDetalhamentoConta::new);
        }

        logAcessoService.gerarEvento(usuarioLogado.getUsername(), "Listagem de contas", TipoLogEvento.ACESSO_A_LISTAGEM);
        return ResponseEntity.ok(page);
    }

    public ResponseEntity listarContaById(Integer id, UserDetails usuarioLogado) {
        Conta conta = contaRepository.findFirstById(id);
        logAcessoService.gerarEvento(usuarioLogado.getUsername(), conta.toString(), TipoLogEvento.ACESSO_A_LISTAR_POR_ID);
        return ResponseEntity.ok(new DadosDetalhamentoConta(conta));
    }

    public ResponseEntity deletarById(Integer id) {
        Conta conta = contaRepository.findFirstById(id);
        if (conta != null) {
            contaRepository.delete(conta);
        }
        return ResponseEntity.noContent().build();
    }

    @Scheduled(cron = "0 0 5 * * *") //fixedRate = 5000 | cro = "0 0 6 * * *"
    private void verificarContasPertoDeVencer() {
        LOGGER.info("Verificando contas perto do venciemnto: ");

        List<Conta> contas = contaRepository.findAll();
        if (!contas.isEmpty()) {
            for (Conta contaPertoDeVencer : contas) {
                if (!contaPertoDeVencer.getStatusConta().equals(StatusConta.CANCELADA) || !contaPertoDeVencer.getStatusConta().equals(StatusConta.PAGO)) {
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
                if (!contaVencida.getStatusConta().equals(StatusConta.CANCELADA) || !contaVencida.getStatusConta().equals(StatusConta.PAGO)) {
                    LOGGER.info(String.format("Alterando o status da conta %s para VENCIDA.", contaVencida.getTitulo()));
                    contaVencida.setStatusConta(StatusConta.VENCIDA);
                    contaRepository.save(contaVencida);
                }
            }
        }
    }
}
