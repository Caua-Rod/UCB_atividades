package com.arenasports.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.arenasports.util.NegocioException;

public class Pedido implements Validavel {
    private Integer id;
    private LocalDateTime dataPedido = LocalDateTime.now();
    private Cliente cliente;
    private Usuario usuario;
    private final List<ItemPedido> itens = new ArrayList<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public void adicionarItem(Produto produto, int quantidade) {
        if (produto == null) {
            throw new NegocioException("Escolha um produto.");
        }
        if (quantidade <= 0) {
            throw new NegocioException("A quantidade deve ser maior que zero.");
        }
        for (ItemPedido item : itens) {
            if (item.getProduto().getId() != null && item.getProduto().getId().equals(produto.getId())) {
                conferirEstoque(produto, item.getQuantidade() + quantidade);
                item.setQuantidade(item.getQuantidade() + quantidade);
                return;
            }
        }
        conferirEstoque(produto, quantidade);
        itens.add(new ItemPedido(produto, quantidade));
    }

    private void conferirEstoque(Produto produto, int quantidadeTotal) {
        if (quantidadeTotal > produto.getEstoque()) {
            throw new NegocioException("Estoque insuficiente para \"" + produto.getNome()
                    + "\". Disponível: " + produto.getEstoque() + ".");
        }
    }

    public void adicionarItem(ItemPedido item) {
        if (item == null) {
            throw new NegocioException("Item inválido.");
        }
        itens.add(item);
    }

    public void removerItem(ItemPedido item) {
        itens.remove(item);
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedido item : itens) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    @Override
    public void validar() {
        if (cliente == null) {
            throw new NegocioException("Escolha o cliente do pedido.");
        }
        if (usuario == null) {
            throw new NegocioException("O pedido precisa de um usuário responsável.");
        }
        if (itens.isEmpty()) {
            throw new NegocioException("Adicione pelo menos um item ao pedido.");
        }
        for (ItemPedido item : itens) {
            item.validar();
        }
    }
}
