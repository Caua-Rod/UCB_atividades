package com.arenasports.dao;

import com.arenasports.model.Categoria;
import com.arenasports.util.BancoUtil;
import com.arenasports.util.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO implements Crud<Categoria> {

    @Override
    public void inserir(Categoria categoria) {
        String sql = "INSERT INTO categoria (nome) VALUES (?)";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, categoria.getNome().trim());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    categoria.setId(chaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "salvar a categoria");
        }
    }

    @Override
    public void atualizar(Categoria categoria) {
        String sql = "UPDATE categoria SET nome = ? WHERE id_categoria = ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoria.getNome().trim());
            ps.setInt(2, categoria.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "atualizar a categoria");
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "excluir a categoria");
        }
    }

    @Override
    public Categoria buscarPorId(int id) {
        String sql = "SELECT id_categoria, nome FROM categoria WHERE id_categoria = ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "buscar a categoria");
        }
    }

    @Override
    public List<Categoria> listar() {
        String sql = "SELECT id_categoria, nome FROM categoria ORDER BY nome";
        List<Categoria> lista = new ArrayList<>();
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "listar as categorias");
        }
        return lista;
    }

    public boolean existeNome(String nome, Integer ignorarId) {
        String sql = "SELECT COUNT(*) FROM categoria WHERE nome = ? AND id_categoria <> ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome.trim());
            ps.setInt(2, ignorarId == null ? -1 : ignorarId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "verificar a categoria");
        }
    }

    private Categoria mapear(ResultSet rs) throws SQLException {
        return new Categoria(rs.getInt("id_categoria"), rs.getString("nome"));
    }
}
