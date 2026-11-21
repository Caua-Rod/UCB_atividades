package com.arenasports.model;

import com.arenasports.util.NegocioException;

public class Usuario extends Pessoa {
    private String login;
    private String senhaHash;
    private Perfil perfil = Perfil.OPERADOR;
    private boolean ativo = true;

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    @Override
    public String getTipo() {
        return perfil == null ? "Usuário" : perfil.getRotulo();
    }

    @Override
    public void validar() {
        super.validar();
        if (login == null || !login.trim().matches("[A-Za-z0-9._]{3,30}")) {
            throw new NegocioException("O login deve ter de 3 a 30 caracteres (letras, números, ponto ou _).");
        }
        if (perfil == null) {
            throw new NegocioException("Escolha o perfil do usuário.");
        }
        if (senhaHash == null || senhaHash.isBlank()) {
            throw new NegocioException("O usuário precisa ter uma senha.");
        }
    }
}
