package com.arenasports.dao;

import com.arenasports.model.Perfil;
import com.arenasports.model.Usuario;
import com.arenasports.util.BancoUtil;
import com.arenasports.util.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements Crud<Usuario> {

    private static final String SELECT_BASE =
            "SELECT id_usuario, nome, telefone, login, senha_hash, perfil, ativo FROM usuario ";

    @Override
    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, telefone, login, senha_hash, perfil, ativo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNome().trim());
            ps.setString(2, BancoUtil.vazioParaNulo(usuario.getTelefone()));
            ps.setString(3, usuario.getLogin().trim());
            ps.setString(4, usuario.getSenhaHash());
            ps.setString(5, usuario.getPerfil().name());
            ps.setBoolean(6, usuario.isAtivo());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    usuario.setId(chaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "salvar o usuário");
        }
    }

    @Override
    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, telefone = ?, login = ?, perfil = ?, ativo = ? "
                + "WHERE id_usuario = ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getNome().trim());
            ps.setString(2, BancoUtil.vazioParaNulo(usuario.getTelefone()));
            ps.setString(3, usuario.getLogin().trim());
            ps.setString(4, usuario.getPerfil().name());
            ps.setBoolean(5, usuario.isAtivo());
            ps.setInt(6, usuario.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "atualizar o usuário");
        }
    }

    public void atualizarSenha(int id, String novoHash) {
        String sql = "UPDATE usuario SET senha_hash = ? WHERE id_usuario = ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, novoHash);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "alterar a senha");
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "excluir o usuário");
        }
    }

    @Override
    public Usuario buscarPorId(int id) {
        String sql = SELECT_BASE + "WHERE id_usuario = ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "buscar o usuário");
        }
    }

    public Usuario buscarPorLogin(String login) {
        String sql = SELECT_BASE + "WHERE login = ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "buscar o usuário");
        }
    }

    @Override
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY nome";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "listar os usuários");
        }
        return lista;
    }

    public int contar() {
        String sql = "SELECT COUNT(*) FROM usuario";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "contar os usuários");
        }
    }

    public int contarAdminsAtivos() {
        String sql = "SELECT COUNT(*) FROM usuario WHERE perfil = 'ADMIN' AND ativo = 1";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "contar os administradores");
        }
    }

    public boolean existeLogin(String login, Integer ignorarId) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE login = ? AND id_usuario <> ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login.trim());
            ps.setInt(2, ignorarId == null ? -1 : ignorarId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "verificar o login");
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id_usuario"));
        u.setNome(rs.getString("nome"));
        u.setTelefone(rs.getString("telefone"));
        u.setLogin(rs.getString("login"));
        u.setSenhaHash(rs.getString("senha_hash"));
        u.setPerfil(Perfil.deTexto(rs.getString("perfil")));
        u.setAtivo(rs.getBoolean("ativo"));
        return u;
    }
}
