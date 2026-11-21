package com.arenasports.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

import com.arenasports.model.Categoria;
import com.arenasports.model.Produto;
import com.arenasports.service.CategoriaService;
import com.arenasports.service.ProdutoService;
import com.arenasports.util.Alertas;
import com.arenasports.util.NegocioException;
import com.arenasports.util.Sessao;
import com.arenasports.util.TelaUtil;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ProdutosController {
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    @FXML private TextField txtBusca;
    @FXML private TableView<Produto> tblProdutos;
    @FXML private TableColumn<Produto, String> colNome;
    @FXML private TableColumn<Produto, String> colCategoria;
    @FXML private TableColumn<Produto, BigDecimal> colPreco;
    @FXML private TableColumn<Produto, Integer> colEstoque;
    @FXML private TableColumn<Produto, String> colCodigo;

    @FXML private Label lblFormTitulo;
    @FXML private TextField txtNome;
    @FXML private TextField txtDescricao;
    @FXML private TextField txtPreco;
    @FXML private TextField txtEstoque;
    @FXML private TextField txtCodigo;
    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private Button btnExcluir;

    private final ProdutoService produtoService = new ProdutoService();
    private final CategoriaService categoriaService = new CategoriaService();

    private Produto selecionado;

    @FXML
    private void initialize() {
        configurarColunas();
        configurarCampos();

        btnExcluir.setVisible(Sessao.getInstance().isAdmin());
        btnExcluir.setManaged(Sessao.getInstance().isAdmin());

        tblProdutos.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                preencherFormulario(novo);
            }
        });

        carregarCategorias();
        carregarTabela("");
        novo();
    }


    @FXML
    private void buscar() {
        carregarTabela(txtBusca.getText());
    }

    @FXML
    private void limparBusca() {
        txtBusca.clear();
        carregarTabela("");
    }

    @FXML
    private void novo() {
        selecionado = null;
        tblProdutos.getSelectionModel().clearSelection();
        txtNome.clear();
        txtDescricao.clear();
        txtPreco.clear();
        txtEstoque.clear();
        txtCodigo.clear();
        cmbCategoria.getSelectionModel().clearSelection();
        cmbCategoria.setValue(null);
        lblFormTitulo.setText("Novo produto");
        txtNome.requestFocus();
    }

    @FXML
    private void salvar() {
        try {

            Produto produto = new Produto();
            produto.setId(selecionado == null ? null : selecionado.getId());
            produto.setNome(txtNome.getText());
            produto.setDescricao(txtDescricao.getText());
            produto.setPreco(lerPreco());
            produto.setEstoque(lerEstoque());
            produto.setCodigoBarras(txtCodigo.getText());
            produto.setCategoria(cmbCategoria.getValue());

            produtoService.salvar(produto);   
            Alertas.info("Produto salvo com sucesso.");
            novo();
            carregarTabela(txtBusca.getText());
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }

    @FXML
    private void excluir() {
        if (selecionado == null) {
            Alertas.aviso("Selecione um produto na tabela para excluir.");
            return;
        }
        if (!Alertas.confirmar("Excluir o produto \"" + selecionado.getNome() + "\"?")) {
            return;
        }
        try {
            produtoService.excluir(selecionado.getId());
            Alertas.info("Produto excluído.");
            novo();
            carregarTabela(txtBusca.getText());
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }


    private void carregarTabela(String termo) {
        try {
            tblProdutos.getItems().setAll(produtoService.buscar(termo));
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }

    private void carregarCategorias() {
        try {
            cmbCategoria.getItems().setAll(categoriaService.listar());
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }

    private void preencherFormulario(Produto produto) {
        selecionado = produto;
        txtNome.setText(produto.getNome());
        txtDescricao.setText(produto.getDescricao() == null ? "" : produto.getDescricao());
        txtPreco.setText(produto.getPreco().toPlainString().replace('.', ','));
        txtEstoque.setText(String.valueOf(produto.getEstoque()));
        txtCodigo.setText(produto.getCodigoBarras());

        Categoria daLista = cmbCategoria.getItems().stream()
                .filter(c -> c.getId().equals(produto.getCategoria().getId()))
                .findFirst()
                .orElse(null);
        cmbCategoria.setValue(daLista);

        lblFormTitulo.setText("Editar produto");
    }


    private BigDecimal lerPreco() {
        String texto = txtPreco.getText() == null ? "" : txtPreco.getText().trim();
        if (texto.isEmpty()) {
            throw new NegocioException("Informe o preço.");
        }
        try {
            return new BigDecimal(texto.replace(',', '.')).setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new NegocioException("Preço inválido. Use apenas números, por exemplo 149,90.");
        }
    }

    private int lerEstoque() {
        String texto = txtEstoque.getText() == null ? "" : txtEstoque.getText().trim();
        if (texto.isEmpty()) {
            throw new NegocioException("Informe o estoque.");
        }
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new NegocioException("Estoque inválido. Use apenas números inteiros.");
        }
    }


    private void configurarColunas() {
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colCategoria.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategoria().getNome()));
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigoBarras()));

        colPreco.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getPreco()));
        colPreco.setCellFactory(col -> new TableCell<Produto, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal valor, boolean vazio) {
                super.updateItem(valor, vazio);
                setText(vazio || valor == null ? null : MOEDA.format(valor));
            }
        });
        colEstoque.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getEstoque()));

        tblProdutos.setRowFactory(tv -> new TableRow<Produto>() {
            @Override
            protected void updateItem(Produto produto, boolean vazio) {
                super.updateItem(produto, vazio);
                getStyleClass().remove("linha-estoque-baixo");
                if (!vazio && produto != null && produto.getEstoque() <= ProdutoService.LIMITE_ESTOQUE_BAIXO) {
                    getStyleClass().add("linha-estoque-baixo");
                }
            }
        });
    }

    private void configurarCampos() {
        TelaUtil.aplicarFiltro(txtNome, ".{0,100}");
        TelaUtil.aplicarFiltro(txtDescricao, ".{0,200}");
        TelaUtil.aplicarFiltro(txtPreco, "\\d{0,8}([.,]\\d{0,2})?");
        TelaUtil.aplicarFiltro(txtEstoque, "\\d{0,7}");
        TelaUtil.aplicarFiltro(txtCodigo, "\\d{0,18}");
        TelaUtil.aplicarFiltro(txtBusca, ".{0,100}");
    }
}
