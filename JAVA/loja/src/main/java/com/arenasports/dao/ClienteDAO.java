package com.arenasports.dao;

import com.arenasports.model.Cliente;
import com.arenasports.util.BancoUtil;
import com.arenasports.util.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements Crud<Cliente> {

    private static final String SELECT_BASE = "SELECT id_cliente, nome, telefone, email FROM cliente ";

    @Override
    public void inserir(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, telefone, email) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencher(ps, cliente);
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    cliente.setId(chaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "salvar o cliente");
        }
    }

    @Override
    public void atualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, telefone = ?, email = ? WHERE id_cliente = ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, cliente);
            ps.setInt(4, cliente.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "atualizar o cliente");
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "excluir o cliente");
        }
    }

    @Override
    public Cliente buscarPorId(int id) {
        String sql = SELECT_BASE + "WHERE id_cliente = ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "buscar o cliente");
        }
    }

    @Override
    public List<Cliente> listar() {
        return consultar(SELECT_BASE + "ORDER BY nome", null);
    }

    public List<Cliente> buscarPorNome(String termo) {
        return consultar(SELECT_BASE + "WHERE nome LIKE ? ORDER BY nome", "%" + termo.trim() + "%");
    }

    /** Usado pelo service para evitar e-mail repetido (e-mail vazio não conta). */
    public boolean existeEmail(String email, Integer ignorarId) {
        if (BancoUtil.vazioParaNulo(email) == null) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM cliente WHERE email = ? AND id_cliente <> ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.trim());
            ps.setInt(2, ignorarId == null ? -1 : ignorarId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "verificar o e-mail");
        }
    }

    private List<Cliente> consultar(String sql, String parametro) {
        List<Cliente> lista = new ArrayList<>();
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
            throw BancoUtil.traduzir(e, "listar os clientes");
        }
        return lista;
    }

    private void preencher(PreparedStatement ps, Cliente c) throws SQLException {
        ps.setString(1, c.getNome().trim());
        ps.setString(2, BancoUtil.vazioParaNulo(c.getTelefone()));
        ps.setString(3, BancoUtil.vazioParaNulo(c.getEmail()));
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("id_cliente"));
        c.setNome(rs.getString("nome"));
        c.setTelefone(rs.getString("telefone"));
        c.setEmail(rs.getString("email"));
        return c;
    }
}
