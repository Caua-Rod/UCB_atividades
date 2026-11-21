package com.arenasports.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class SenhaUtil {
    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int ITERACOES = 120_000;
    private static final int TAMANHO_SALT = 16;    
    private static final int TAMANHO_HASH = 256; 

    private SenhaUtil() {
    }

    public static void validarSenha(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new NegocioException("Informe a senha.");
        }
        if (senha.length() < 6) {
            throw new NegocioException("A senha deve ter pelo menos 6 caracteres.");
        }
        if (senha.length() > 50) {
            throw new NegocioException("A senha pode ter no máximo 50 caracteres.");
        }
    }

    public static String gerarHash(String senha) {
        byte[] salt = new byte[TAMANHO_SALT];
        new SecureRandom().nextBytes(salt);
        byte[] hash = calcular(senha.toCharArray(), salt, ITERACOES);
        return ITERACOES + ":" + Base64.getEncoder().encodeToString(salt)
                + ":" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verificar(String senha, String hashSalvo) {
        if (senha == null || hashSalvo == null) {
            return false;
        }
        try {
            String[] partes = hashSalvo.split(":");
            if (partes.length != 3) {
                return false;
            }
            int iteracoes = Integer.parseInt(partes[0]);
            byte[] salt = Base64.getDecoder().decode(partes[1]);
            byte[] esperado = Base64.getDecoder().decode(partes[2]);
            byte[] calculado = calcular(senha.toCharArray(), salt, iteracoes);
            return MessageDigest.isEqual(esperado, calculado);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] calcular(char[] senha, byte[] salt, int iteracoes) {
        try {
            PBEKeySpec spec = new PBEKeySpec(senha, salt, iteracoes, TAMANHO_HASH);
            return SecretKeyFactory.getInstance(ALGORITMO).generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Não foi possível processar a senha.", e);
        }
    }
}
