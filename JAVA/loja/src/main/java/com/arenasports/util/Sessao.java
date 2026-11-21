package com.arenasports.util;

import com.arenasports.model.Perfil;
import com.arenasports.model.Usuario;

public class Sessao {
    private static final Sessao INSTANCIA = new Sessao();

    private Usuario usuarioLogado;

    private Sessao() {
    }

    public static Sessao getInstance() {
        return INSTANCIA;
    }

    public void iniciar(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public void encerrar() {
        this.usuarioLogado = null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public boolean isLogado() {
        return usuarioLogado != null;
    }

    public boolean isAdmin() {
        return usuarioLogado != null && usuarioLogado.getPerfil() == Perfil.ADMIN;
    }

    public void exigirLogin() {
        if (!isLogado()) {
            throw new NegocioException("Faça login para continuar.");
        }
    }

    public void exigirAdmin() {
        exigirLogin();
        if (!isAdmin()) {
            throw new NegocioException("Apenas administradores podem executar esta ação.");
        }
    }
}
