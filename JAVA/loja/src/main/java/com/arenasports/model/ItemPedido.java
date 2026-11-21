package com.arenasports.model;

import com.arenasports.util.NegocioException;
import java.math.BigDecimal;

public class ItemPedido implements Validavel {
    private Produto produto;
    private int quantidade;
    private BigDecimal precoUnitario;

    public ItemPedido() {
    }

    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto == null ? null : produto.getPreco();
    }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    public BigDecimal getSubtotal() {
        if (precoUnitario == null) {
            return BigDecimal.ZERO;
        }
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    @Override
    public void validar() {
        if (produto == null) {
            throw new NegocioException("O item precisa de um produto.");
        }
        if (quantidade <= 0) {
            throw new NegocioException("A quantidade de \"" + produto.getNome() + "\" deve ser maior que zero.");
        }
        if (precoUnitario == null || precoUnitario.signum() < 0) {
            throw new NegocioException("Preço inválido no item \"" + produto.getNome() + "\".");
        }
    }
}
