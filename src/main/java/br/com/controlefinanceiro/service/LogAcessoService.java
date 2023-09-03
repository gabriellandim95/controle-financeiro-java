package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.enums.TipoLogEvento;
import br.com.controlefinanceiro.model.LogAcesso;
import br.com.controlefinanceiro.repository.LogEventosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogAcessoService {
    private static final Logger logger = LoggerFactory.getLogger(LogAcesso.class);

    @Autowired
    private LogEventosRepository repository;

    public void gerarEvento(String usuario, String evento, TipoLogEvento tipoLogEvento){
        LogAcesso logAcesso = new LogAcesso(usuario, evento, tipoLogEvento);
        repository.save(logAcesso);
    }
}
