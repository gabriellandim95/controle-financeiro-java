package br.com.lm.controlefinanceiro.service;

import br.com.lm.controlefinanceiro.enums.TipoLogEvento;
import br.com.lm.controlefinanceiro.interfaces.LogAcessoService;
import br.com.lm.controlefinanceiro.model.LogAcesso;
import br.com.lm.controlefinanceiro.repository.LogEventosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogAcessoServiceImpl implements LogAcessoService {
    private final LogEventosRepository repository;

    @Override
    public void gerarEvento(String usuario, String evento, TipoLogEvento tipoLogEvento){
        LogAcesso logAcesso = new LogAcesso(usuario, evento, tipoLogEvento);
        repository.save(logAcesso);
    }
}
