package br.com.controlefinanceiro.service;

import br.com.controlefinanceiro.infra.exceptions.AutenticacaoException;
import br.com.controlefinanceiro.interfaces.MessageService;
import br.com.controlefinanceiro.interfaces.TokenBlackListService;
import br.com.controlefinanceiro.interfaces.TokenService;
import br.com.controlefinanceiro.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    @Value("${api.security.token.secret}")
    private String secret;
    @Value("${api.security.token.expiration}")
    private long minuntosLimiteToken;
    private final MessageService messageService;
    private final TokenBlackListService tokenBlackListService;

    private static final String ISSUER = "API Controle Financeiro";

    @Override
    public String gerarToken(Usuario usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(dataExpiracao())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new AutenticacaoException(messageService.getMessage("api.auth.erro.gerar.token", exception.getMessage()), exception);
        }
    }

    @Override
    public String getSubject(String tokenJWT){
        try {
            if (tokenBlackListService.isTokenNaBlackList(tokenJWT)){
                throw new AutenticacaoException(messageService.getMessage("api.auth.token.blacklist"));
            }

            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new AutenticacaoException(messageService.getMessage("api.auth.token.invalido"));
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))
                .plusMinutes(minuntosLimiteToken)
                .toInstant(ZoneOffset.UTC);
    }
}
