package com.arenasports.dao;

import com.arenasports.model.Cliente;
import com.arenasports.model.ItemPedido;
import com.arenasports.model.Pedido;
import com.arenasports.model.Produto;
import com.arenasports.model.Usuario;
import com.arenasports.util.BancoUtil;
import com.arenasports.util.Conexao;
import com.arenasports.util.NegocioException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO implements Leitura<Pedido> {

    private static final String SELECT_BASE =
            "SELECT p.id_pedido, p.data_pedido, p.id_cliente, c.nome AS nome_cliente, "
            + "p.id_usuario, u.nome AS nome_usuario "
            + "FROM pedido p "
            + "JOIN cliente c ON c.id_cliente = p.id_cliente "
            + "JOIN usuario u ON u.id_usuario = p.id_usuario ";

    public void inserir(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedido (data_pedido, id_cliente, id_usuario) VALUES (?, ?, ?)";
        String sqlItem = "INSERT INTO item_pedido (id_pedido, id_produto, quantidade, preco_unitario) "
                + "VALUES (?, ?, ?, ?)";
        String sqlEstoque = "UPDATE produto SET estoque = estoque - ? WHERE id_produto = ? AND estoque >= ?";

        try (Connection conn = Conexao.getInstance().obterConexao()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setTimestamp(1, Timestamp.valueOf(pedido.getDataPedido()));
                    ps.setInt(2, pedido.getCliente().getId());
                    ps.setInt(3, pedido.getUsuario().getId());
                    ps.executeUpdate();
                    try (ResultSet chaves = ps.getGeneratedKeys()) {
                        if (!chaves.next()) {
                            throw new SQLException("O banco não devolveu o número do pedido.");
                        }
                        pedido.setId(chaves.getInt(1));
                    }
                }

                try (PreparedStatement psItem = conn.prepareStatement(sqlItem);
                     PreparedStatement psEstoque = conn.prepareStatement(sqlEstoque)) {
                    for (ItemPedido item : pedido.getItens()) {
                        // o UPDATE só funciona se houver estoque suficiente
                        psEstoque.setInt(1, item.getQuantidade());
                        psEstoque.setInt(2, item.getProduto().getId());
                        psEstoque.setInt(3, item.getQuantidade());
                        if (psEstoque.executeUpdate() == 0) {
                            throw new NegocioException("Estoque insuficiente para \""
                                    + item.getProduto().getNome() + "\".");
                        }
                        psItem.setInt(1, pedido.getId());
                        psItem.setInt(2, item.getProduto().getId());
                        psItem.setInt(3, item.getQuantidade());
                        psItem.setBigDecimal(4, item.getPrecoUnitario());
                        psItem.executeUpdate();
                    }
                }
                conn.commit();
            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                pedido.setId(null);
                throw e;
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "registrar o pedido");
        }
    }

    public void cancelar(int idPedido) {
        String sqlItens = "SELECT id_produto, quantidade FROM item_pedido WHERE id_pedido = ?";
        String sqlEstoque = "UPDATE produto SET estoque = estoque + ? WHERE id_produto = ?";
        String sqlExcluir = "DELETE FROM pedido WHERE id_pedido = ?";

        try (Connection conn = Conexao.getInstance().obterConexao()) {
            conn.setAutoCommit(false);
            try {
                List<int[]> devolucoes = new ArrayList<>();   // cada item: {idProduto, quantidade}
                try (PreparedStatement ps = conn.prepareStatement(sqlItens)) {
                    ps.setInt(1, idPedido);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            devolucoes.add(new int[]{rs.getInt("id_produto"), rs.getInt("quantidade")});
                        }
                    }
                }
                try (PreparedStatement ps = conn.prepareStatement(sqlEstoque)) {
                    for (int[] d : devolucoes) {
                        ps.setInt(1, d[1]);
                        ps.setInt(2, d[0]);
                        ps.executeUpdate();
                    }
                }
                try (PreparedStatement ps = conn.prepareStatement(sqlExcluir)) {
                    ps.setInt(1, idPedido);
                    ps.executeUpdate();
                }
                conn.commit();
            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "cancelar o pedido");
        }
    }

    @Override
    public Pedido buscarPorId(int id) {
        String sql = SELECT_BASE + "WHERE p.id_pedido = ?";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Pedido pedido = mapear(rs);
                carregarItens(conn, pedido);
                return pedido;
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "buscar o pedido");
        }
    }

    @Override
    public List<Pedido> listar() {
        List<Pedido> lista = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY p.data_pedido DESC, p.id_pedido DESC";
        try (Connection conn = Conexao.getInstance().obterConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            for (Pedido pedido : lista) {
                carregarItens(conn, pedido);
            }
        } catch (SQLException e) {
            throw BancoUtil.traduzir(e, "listar os pedidos");
        }
        return lista;
    }

    private void carregarItens(Connection conn, Pedido pedido) throws SQLException {
        String sql = "SELECT i.id_produto, pr.nome, pr.codigo_barras, i.quantidade, i.preco_unitario "
                + "FROM item_pedido i JOIN produto pr ON pr.id_produto = i.id_produto "
                + "WHERE i.id_pedido = ? ORDER BY pr.nome";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pedido.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getInt("id_produto"));
                    produto.setNome(rs.getString("nome"));
                    produto.setCodigoBarras(rs.getString("codigo_barras"));

                    ItemPedido item = new ItemPedido();
                    item.setProduto(produto);
                    item.setQuantidade(rs.getInt("quantidade"));
                    item.setPrecoUnitario(rs.getBigDecimal("preco_unitario"));
                    pedido.adicionarItem(item);
                }
            }
        }
    }

    private Pedido mapear(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id_cliente"));
        cliente.setNome(rs.getString("nome_cliente"));

        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id_usuario"));
        usuario.setNome(rs.getString("nome_usuario"));

        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("id_pedido"));
        pedido.setDataPedido(rs.getTimestamp("data_pedido").toLocalDateTime());
        pedido.setCliente(cliente);
        pedido.setUsuario(usuario);
        return pedido;
    }
}
