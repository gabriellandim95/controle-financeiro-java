package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.enums.TipoLogEvento;
import br.com.controlefinanceiro.interfaces.LogAcessoService;
import br.com.controlefinanceiro.model.LogAcesso;
import br.com.controlefinanceiro.repository.LogEventosRepository;
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
