package com.arenasports.service;

import com.arenasports.dao.UsuarioDAO;
import com.arenasports.model.Perfil;
import com.arenasports.model.Usuario;
import com.arenasports.util.NegocioException;
import com.arenasports.util.SenhaUtil;
import com.arenasports.util.Sessao;

public class AuthService {
    private static final String ADMIN_LOGIN = "admin";
    private static final String ADMIN_SENHA_PADRAO = "admin123";

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void garantirAdminInicial() {
        if (usuarioDAO.contar() > 0) {
            return;
        }
        Usuario admin = new Usuario();
        admin.setNome("Administrador");
        admin.setLogin(ADMIN_LOGIN);
        admin.setPerfil(Perfil.ADMIN);
        admin.setAtivo(true);
        admin.setSenhaHash(SenhaUtil.gerarHash(ADMIN_SENHA_PADRAO));
        admin.validar();
        usuarioDAO.inserir(admin);
    }

    public Usuario autenticar(String login, String senha) {
        if (login == null || login.isBlank() || senha == null || senha.isEmpty()) {
            throw new NegocioException("Informe o login e a senha.");
        }
        Usuario usuario = usuarioDAO.buscarPorLogin(login);

        if (usuario == null || !SenhaUtil.verificar(senha, usuario.getSenhaHash())) {
            throw new NegocioException("Login ou senha inválidos.");
        }
        if (!usuario.isAtivo()) {
            throw new NegocioException("Este usuário está inativo. Procure um administrador.");
        }
        Sessao.getInstance().iniciar(usuario);
        return usuario;
    }

    public void sair() {
        Sessao.getInstance().encerrar();
    }

    public void alterarSenha(String senhaAtual, String novaSenha) {
        Sessao sessao = Sessao.getInstance();
        sessao.exigirLogin();
        Usuario usuario = sessao.getUsuarioLogado();

        if (!SenhaUtil.verificar(senhaAtual, usuario.getSenhaHash())) {
            throw new NegocioException("A senha atual está incorreta.");
        }
        SenhaUtil.validarSenha(novaSenha);

        String novoHash = SenhaUtil.gerarHash(novaSenha);
        usuarioDAO.atualizarSenha(usuario.getId(), novoHash);
        usuario.setSenhaHash(novoHash);
    }
}
