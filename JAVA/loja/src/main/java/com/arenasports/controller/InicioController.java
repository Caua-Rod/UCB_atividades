package com.arenasports.controller;

import java.util.List;

import com.arenasports.model.Produto;
import com.arenasports.service.ProdutoService;
import com.arenasports.util.Alertas;
import com.arenasports.util.DaoException;
import com.arenasports.util.NegocioException;
import com.arenasports.util.Sessao;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class InicioController {
    @FXML private Label lblBoasVindas;
    @FXML private ListView<String> lstEstoqueBaixo;

    private final ProdutoService produtoService = new ProdutoService();

    @FXML
    private void initialize() {
        lblBoasVindas.setText("Bem-vindo, " + Sessao.getInstance().getUsuarioLogado().getNome() + "!");
        try {
            List<Produto> produtos = produtoService.listarEstoqueBaixo();
            if (produtos.isEmpty()) {
                lstEstoqueBaixo.getItems().add("Nenhum produto com estoque baixo.");
            }
            for (Produto p : produtos) {
                lstEstoqueBaixo.getItems().add(p.getNome() + "  -  estoque: " + p.getEstoque());
            }
        } catch (NegocioException | DaoException e) {
            Alertas.erro(e.getMessage());
        }
    }
}
