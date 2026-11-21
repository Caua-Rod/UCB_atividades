package com.arenasports.service;

import java.time.LocalDateTime;
import java.util.List;

import com.arenasports.dao.PedidoDAO;
import com.arenasports.model.ItemPedido;
import com.arenasports.model.Pedido;
import com.arenasports.util.NegocioException;
import com.arenasports.util.Sessao;

public class PedidoService {
    private static final int QUANTIDADE_MAXIMA = 9999;

    private final PedidoDAO pedidoDAO = new PedidoDAO();

    public List<Pedido> listar() {
        Sessao.getInstance().exigirLogin();
        return pedidoDAO.listar();
    }

    public Pedido buscarPorId(int id) {
        Sessao.getInstance().exigirLogin();
        Pedido pedido = pedidoDAO.buscarPorId(id);
        if (pedido == null) {
            throw new NegocioException("Pedido não encontrado.");
        }
        return pedido;
    }

    public Pedido registrar(Pedido pedido) {
        Sessao sessao = Sessao.getInstance();
        sessao.exigirLogin();

        pedido.setUsuario(sessao.getUsuarioLogado());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.validar();

        if (pedido.getCliente().getId() == null) {
            throw new NegocioException("Escolha um cliente cadastrado.");
        }
        for (ItemPedido item : pedido.getItens()) {
            if (item.getProduto().getId() == null) {
                throw new NegocioException("Escolha apenas produtos cadastrados.");
            }
            if (item.getQuantidade() > QUANTIDADE_MAXIMA) {
                throw new NegocioException("Quantidade muito alta para \"" + item.getProduto().getNome()
                        + "\" (máximo " + QUANTIDADE_MAXIMA + ").");
            }
        }
        pedidoDAO.inserir(pedido); 
        return pedido;
    }

    public void cancelar(int id) {
        Sessao.getInstance().exigirAdmin();
        if (pedidoDAO.buscarPorId(id) == null) {
            throw new NegocioException("Pedido não encontrado.");
        }
        pedidoDAO.cancelar(id);
    }
}
