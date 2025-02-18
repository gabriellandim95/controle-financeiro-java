package br.com.lm.controlefinanceiro.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptUtils {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String converterParaBCrypt(String senha) {
        return encoder.encode(senha);
    }
}
