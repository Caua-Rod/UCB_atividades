package com.arenasports.dao;

import com.arenasports.model.Categoria;
import com.arenasports.model.Produto;
import com.arenasports.util.BancoUtil;
import com.arenasports.util.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO implements Crud<Produto> {

    private static final String SELECT_BASE =
            "SELECT p.id_produto, p.nome, p.descricao, p.preco, p.estoque, p.codigo_barras, "
            + "p.id_categoria, c.nome AS nome_categoria "
            + "FROM produto p JOIN categoria c ON c.id_categoria = p.id_categoria ";

    @Override
    public void inserir(Produto produto) {
        String sql = "INSERT INTO produto (nome, descricao, preco, estoque, codigo_barras, id_categoria) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencher(ps, produto);
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    produto.setId(chaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "salvar o produto");
        }
    }

    @Override
    public void atualizar(Produto produto) {
        String sql = "UPDATE produto SET nome = ?, descricao = ?, preco = ?, estoque = ?, "
                + "codigo_barras = ?, id_categoria = ? WHERE id_produto = ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, produto);
            ps.setInt(7, produto.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "atualizar o produto");
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM produto WHERE id_produto = ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "excluir o produto");
        }
    }

    @Override
    public Produto buscarPorId(int id) {
        String sql = SELECT_BASE + "WHERE p.id_produto = ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "buscar o produto");
        }
    }

    @Override
    public List<Produto> listar() {
        return consultar(SELECT_BASE + "ORDER BY p.nome", null);
    }

    public List<Produto> buscarPorNome(String termo) {
        return consultar(SELECT_BASE + "WHERE p.nome LIKE ? ORDER BY p.nome", "%" + termo.trim() + "%");
    }

    /** Produtos com estoque igual ou abaixo do limite (alerta de reposição). */
    public List<Produto> listarEstoqueBaixo(int limite) {
        List<Produto> lista = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE p.estoque <= ? ORDER BY p.estoque, p.nome";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "listar o estoque baixo");
        }
        return lista;
    }

    public boolean existeCodigoBarras(String codigo, Integer ignorarId) {
        String sql = "SELECT COUNT(*) FROM produto WHERE codigo_barras = ? AND id_produto <> ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo.trim());
            ps.setInt(2, ignorarId == null ? -1 : ignorarId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "verificar o código de barras");
        }
    }

    private List<Produto> consultar(String sql, String parametro) {
        List<Produto> lista = new ArrayList<>();
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (parametro != null) {
                ps.setString(1, parametro);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "listar os produtos");
        }
        return lista;
    }

    private void preencher(PreparedStatement ps, Produto p) throws SQLException {
        ps.setString(1, p.getNome().trim());
        ps.setString(2, BancoUtil.vazioParaNulo(p.getDescricao()));
        ps.setBigDecimal(3, p.getPreco());
        ps.setInt(4, p.getEstoque());
        ps.setString(5, p.getCodigoBarras().trim());
        ps.setInt(6, p.getCategoria().getId());
    }

    private Produto mapear(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setId(rs.getInt("id_produto"));
        p.setNome(rs.getString("nome"));
        p.setDescricao(rs.getString("descricao"));
        p.setPreco(rs.getBigDecimal("preco"));
        p.setEstoque(rs.getInt("estoque"));
        p.setCodigoBarras(rs.getString("codigo_barras"));
        p.setCategoria(new Categoria(rs.getInt("id_categoria"), rs.getString("nome_categoria")));
        return p;
    }
}
