package com.arenasports.model;

import com.arenasports.util.NegocioException;
import java.math.BigDecimal;

public class Produto implements Validavel {
    private static final BigDecimal PRECO_MAXIMO = new BigDecimal("99999999.99");

    private Integer id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int estoque;
    private String codigoBarras;
    private Categoria categoria;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public int getEstoque() { return estoque; }
    public void setEstoque(int estoque) { this.estoque = estoque; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    @Override
    public void validar() {
        if (nome == null || nome.isBlank()) {
            throw new NegocioException("Informe o nome do produto.");
        }
        if (nome.trim().length() > 100) {
            throw new NegocioException("O nome do produto pode ter no máximo 100 caracteres.");
        }
        if (descricao != null && descricao.length() > 200) {
            throw new NegocioException("A descrição pode ter no máximo 200 caracteres.");
        }
        if (preco == null || preco.signum() < 0) {
            throw new NegocioException("Informe um preço válido (zero ou maior).");
        }
        if (preco.compareTo(PRECO_MAXIMO) > 0) {
            throw new NegocioException("O preço informado é alto demais.");
        }
        if (estoque < 0) {
            throw new NegocioException("O estoque não pode ser negativo.");
        }
        if (codigoBarras == null || !codigoBarras.trim().matches("\\d{8,18}")) {
            throw new NegocioException("O código de barras deve ter de 8 a 18 números.");
        }
        if (categoria == null) {
            throw new NegocioException("Escolha a categoria do produto.");
        }
    }

    @Override
    public String toString() {
        return nome;
    }
}
