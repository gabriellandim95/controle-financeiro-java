package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.interfaces.TokenBlackListService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlackListServiceImpl implements TokenBlackListService {
    private final Set<String> blackList = new HashSet<>();

    public void addBlackList(String token) {
        blackList.add(token);
    }

    public Boolean isTokenNaBlackList(String token) {
        return blackList.contains(token);
    }
}
