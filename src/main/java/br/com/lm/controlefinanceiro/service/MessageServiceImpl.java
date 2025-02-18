package br.com.lm.controlefinanceiro.service;

import br.com.lm.controlefinanceiro.interfaces.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageSource messageSource;

    @Override
    public String getMessage(String key, Object... params) {
        Locale locale = new Locale("pt", "BR");
        return messageSource.getMessage(key, params, locale);
    }
}
