package com.arenasports.service;

import java.util.List;

import com.arenasports.dao.ProdutoDAO;
import com.arenasports.model.Produto;
import com.arenasports.util.NegocioException;
import com.arenasports.util.Sessao;

public class ProdutoService {

    public static final int LIMITE_ESTOQUE_BAIXO = 5;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public List<Produto> listar() {
        Sessao.getInstance().exigirLogin();
        return produtoDAO.listar();
    }

    public List<Produto> buscar(String termo) {
        Sessao.getInstance().exigirLogin();
        if (termo == null || termo.isBlank()) {
            return produtoDAO.listar();
        }
        return produtoDAO.buscarPorNome(termo);
    }

    public List<Produto> listarEstoqueBaixo() {
        Sessao.getInstance().exigirLogin();
        return produtoDAO.listarEstoqueBaixo(LIMITE_ESTOQUE_BAIXO);
    }

    public void salvar(Produto produto) {
        Sessao.getInstance().exigirLogin();
        produto.validar();
        if (produto.getCategoria().getId() == null) {
            throw new NegocioException("Escolha uma categoria cadastrada.");
        }
        if (produtoDAO.existeCodigoBarras(produto.getCodigoBarras(), produto.getId())) {
            throw new NegocioException("Já existe um produto com esse código de barras.");
        }
        if (produto.getId() == null) {
            produtoDAO.inserir(produto);
        } else {
            produtoDAO.atualizar(produto);
        }
    }

    public void excluir(int id) {
        Sessao.getInstance().exigirAdmin();
        produtoDAO.excluir(id);
    }
}
