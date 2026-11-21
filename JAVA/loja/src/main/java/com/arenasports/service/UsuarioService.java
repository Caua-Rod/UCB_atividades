package com.arenasports.service;

import java.util.List;

import com.arenasports.dao.UsuarioDAO;
import com.arenasports.model.Perfil;
import com.arenasports.model.Usuario;
import com.arenasports.util.NegocioException;
import com.arenasports.util.SenhaUtil;
import com.arenasports.util.Sessao;

public class UsuarioService {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public List<Usuario> listar() {
        Sessao.getInstance().exigirAdmin();
        return usuarioDAO.listar();
    }

    public void salvar(Usuario usuario, String novaSenha) {
        Sessao sessao = Sessao.getInstance();
        sessao.exigirAdmin();

        boolean novo = usuario.getId() == null;
        boolean trocarSenha = novaSenha != null && !novaSenha.isBlank();

        if (novo || trocarSenha) {
            SenhaUtil.validarSenha(novaSenha);
        }
        if (novo) {
            usuario.setSenhaHash(SenhaUtil.gerarHash(novaSenha));
        }

        usuario.validar();
        if (usuarioDAO.existeLogin(usuario.getLogin(), usuario.getId())) {
            throw new NegocioException("Este login já está em uso.");
        }

        if (novo) {
            usuarioDAO.inserir(usuario);
            return;
        }

        if (usuario.getId().equals(sessao.getUsuarioLogado().getId()) && !usuario.isAtivo()) {
            throw new NegocioException("Você não pode desativar o seu próprio usuário.");
        }
        garantirAdminRestante(usuario);
        usuarioDAO.atualizar(usuario);

        if (trocarSenha) {
            String hash = SenhaUtil.gerarHash(novaSenha);
            usuarioDAO.atualizarSenha(usuario.getId(), hash);
            usuario.setSenhaHash(hash);
        }
        if (usuario.getId().equals(sessao.getUsuarioLogado().getId())) {
            sessao.iniciar(usuarioDAO.buscarPorId(usuario.getId()));
        }
    }

    public void excluir(int id) {
        Sessao sessao = Sessao.getInstance();
        sessao.exigirAdmin();

        if (sessao.getUsuarioLogado().getId() == id) {
            throw new NegocioException("Você não pode excluir o seu próprio usuário.");
        }
        Usuario atual = usuarioDAO.buscarPorId(id);
        if (atual == null) {
            throw new NegocioException("Usuário não encontrado.");
        }
        if (atual.getPerfil() == Perfil.ADMIN && atual.isAtivo() && usuarioDAO.contarAdminsAtivos() <= 1) {
            throw new NegocioException("Deve existir pelo menos um administrador ativo.");
        }
        usuarioDAO.excluir(id);
    }

    private void garantirAdminRestante(Usuario editado) {
        Usuario atual = usuarioDAO.buscarPorId(editado.getId());
        if (atual == null) {
            throw new NegocioException("Usuário não encontrado.");
        }
        boolean eraAdminAtivo = atual.getPerfil() == Perfil.ADMIN && atual.isAtivo();
        boolean continuaAdminAtivo = editado.getPerfil() == Perfil.ADMIN && editado.isAtivo();
        if (eraAdminAtivo && !continuaAdminAtivo && usuarioDAO.contarAdminsAtivos() <= 1) {
            throw new NegocioException("Deve existir pelo menos um administrador ativo.");
        }
    }
}
