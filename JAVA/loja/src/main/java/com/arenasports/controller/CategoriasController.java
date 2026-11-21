package com.arenasports.controller;

import com.arenasports.model.Categoria;
import com.arenasports.service.CategoriaService;
import com.arenasports.util.Alertas;
import com.arenasports.util.Sessao;
import com.arenasports.util.TelaUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CategoriasController {
    @FXML private TableView<Categoria> tblCategorias;
    @FXML private TableColumn<Categoria, String> colNome;
    @FXML private Label lblFormTitulo;
    @FXML private TextField txtNome;
    @FXML private Button btnExcluir;

    private final CategoriaService categoriaService = new CategoriaService();

    private Categoria selecionado;

    @FXML
    private void initialize() {
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        TelaUtil.aplicarFiltro(txtNome, ".{0,50}");

        btnExcluir.setVisible(Sessao.getInstance().isAdmin());
        btnExcluir.setManaged(Sessao.getInstance().isAdmin());

        tblCategorias.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                selecionado = novo;
                txtNome.setText(novo.getNome());
                lblFormTitulo.setText("Editar categoria");
            }
        });

        carregarTabela();
        novo();
    }

    @FXML
    private void novo() {
        selecionado = null;
        tblCategorias.getSelectionModel().clearSelection();
        txtNome.clear();
        lblFormTitulo.setText("Nova categoria");
        txtNome.requestFocus();
    }

    @FXML
    private void salvar() {
        try {
            Categoria categoria = new Categoria();
            categoria.setId(selecionado == null ? null : selecionado.getId());
            categoria.setNome(txtNome.getText());

            categoriaService.salvar(categoria);

            Alertas.info("Categoria salva com sucesso.");
            novo();
            carregarTabela();
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }

    @FXML
    private void excluir() {
        if (selecionado == null) {
            Alertas.aviso("Selecione uma categoria na tabela para excluir.");
            return;
        }
        if (!Alertas.confirmar("Excluir a categoria \"" + selecionado.getNome() + "\"?")) {
            return;
        }
        try {
            categoriaService.excluir(selecionado.getId());
            Alertas.info("Categoria excluída.");
            novo();
            carregarTabela();
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }

    private void carregarTabela() {
        try {
            tblCategorias.getItems().setAll(categoriaService.listar());
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }
}
