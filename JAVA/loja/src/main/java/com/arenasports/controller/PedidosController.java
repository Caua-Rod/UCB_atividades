package com.arenasports.controller;

import com.arenasports.model.Cliente;
import com.arenasports.model.ItemPedido;
import com.arenasports.model.Pedido;
import com.arenasports.model.Produto;
import com.arenasports.service.ClienteService;
import com.arenasports.service.PedidoService;
import com.arenasports.service.ProdutoService;
import com.arenasports.util.Alertas;
import com.arenasports.util.NegocioException;
import com.arenasports.util.Sessao;
import com.arenasports.util.TelaUtil;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class PedidosController {
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
    private static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private ComboBox<Produto> cmbProduto;
    @FXML private TextField txtQuantidade;
    @FXML private TableView<ItemPedido> tblItens;
    @FXML private TableColumn<ItemPedido, String> colItemProduto;
    @FXML private TableColumn<ItemPedido, Integer> colItemQtd;
    @FXML private TableColumn<ItemPedido, BigDecimal> colItemPreco;
    @FXML private TableColumn<ItemPedido, BigDecimal> colItemSubtotal;
    @FXML private Label lblTotal;

    @FXML private TableView<Pedido> tblPedidos;
    @FXML private TableColumn<Pedido, Integer> colNumero;
    @FXML private TableColumn<Pedido, String> colData;
    @FXML private TableColumn<Pedido, String> colCliente;
    @FXML private TableColumn<Pedido, String> colAtendente;
    @FXML private TableColumn<Pedido, BigDecimal> colTotal;
    @FXML private TableView<ItemPedido> tblDetalhe;
    @FXML private TableColumn<ItemPedido, String> colDetProduto;
    @FXML private TableColumn<ItemPedido, Integer> colDetQtd;
    @FXML private TableColumn<ItemPedido, BigDecimal> colDetPreco;
    @FXML private TableColumn<ItemPedido, BigDecimal> colDetSubtotal;
    @FXML private Button btnCancelar;

    private final PedidoService pedidoService = new PedidoService();
    private final ClienteService clienteService = new ClienteService();
    private final ProdutoService produtoService = new ProdutoService();

    private Pedido pedidoAtual = new Pedido();

    @FXML
    private void initialize() {
        configurarAbaNovoPedido();
        configurarAbaHistorico();

        btnCancelar.setVisible(Sessao.getInstance().isAdmin());
        btnCancelar.setManaged(Sessao.getInstance().isAdmin());

        carregarClientes();
        carregarProdutos();
        carregarHistorico();
        novoPedido();
    }


    @FXML
    private void adicionarItem() {
        try {
            Produto produto = cmbProduto.getValue();
            if (produto == null) {
                throw new NegocioException("Escolha um produto.");
            }
            pedidoAtual.adicionarItem(produto, lerQuantidade()); 
            atualizarItens();
            txtQuantidade.setText("1");
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }

    @FXML
    private void removerItem() {
        ItemPedido item = tblItens.getSelectionModel().getSelectedItem();
        if (item == null) {
            Alertas.aviso("Selecione um item da tabela para remover.");
            return;
        }
        pedidoAtual.removerItem(item);
        atualizarItens();
    }

    @FXML
    private void finalizar() {
        try {
            pedidoAtual.setCliente(cmbCliente.getValue());
            Pedido salvo = pedidoService.registrar(pedidoAtual); 

            Alertas.info("Pedido nº " + salvo.getId() + " registrado.\nTotal: " + MOEDA.format(salvo.getTotal()));
            novoPedido();
            carregarProdutos();  
            carregarHistorico();
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e); 
        }
    }

    @FXML
    private void novoPedido() {
        pedidoAtual = new Pedido();
        cmbCliente.setValue(null);
        cmbProduto.setValue(null);
        txtQuantidade.setText("1");
        atualizarItens();
    }

    private void atualizarItens() {
        tblItens.getItems().setAll(pedidoAtual.getItens());
        lblTotal.setText(MOEDA.format(pedidoAtual.getTotal()));
    }

    private int lerQuantidade() {
        String texto = txtQuantidade.getText() == null ? "" : txtQuantidade.getText().trim();
        if (texto.isEmpty()) {
            throw new NegocioException("Informe a quantidade.");
        }
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new NegocioException("Quantidade inválida. Use apenas números inteiros.");
        }
    }

    private void configurarAbaNovoPedido() {
        TelaUtil.aplicarFiltro(txtQuantidade, "\\d{0,4}");

        // no ComboBox de produtos aparece nome, preço e estoque
        cmbProduto.setConverter(new StringConverter<Produto>() {
            @Override
            public String toString(Produto p) {
                return p == null ? "" : p.getNome() + " - " + MOEDA.format(p.getPreco())
                        + " (estoque: " + p.getEstoque() + ")";
            }

            @Override
            public Produto fromString(String texto) {
                return null;
            }
        });

        configurarColunasItens(colItemProduto, colItemQtd, colItemPreco, colItemSubtotal);
    }


    @FXML
    private void atualizarHistorico() {
        carregarHistorico();
    }

    @FXML
    private void cancelarPedido() {
        Pedido pedido = tblPedidos.getSelectionModel().getSelectedItem();
        if (pedido == null) {
            Alertas.aviso("Selecione um pedido na tabela para cancelar.");
            return;
        }
        if (!Alertas.confirmar("Cancelar o pedido nº " + pedido.getId()
                + "?\nOs produtos voltarão ao estoque.")) {
            return;
        }
        try {
            pedidoService.cancelar(pedido.getId());
            Alertas.info("Pedido cancelado.");
            carregarHistorico();
            carregarProdutos(); 
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }

    private void configurarAbaHistorico() {
        colNumero.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getId()));
        colData.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDataPedido().format(DATA_HORA)));
        colCliente.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCliente().getNome()));
        colAtendente.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsuario().getNome()));
        colTotal.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getTotal()));
        formatarMoeda(colTotal);

        configurarColunasItens(colDetProduto, colDetQtd, colDetPreco, colDetSubtotal);

        tblPedidos.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo == null) {
                tblDetalhe.getItems().clear();
            } else {
                tblDetalhe.getItems().setAll(novo.getItens());
            }
        });
    }


    private void carregarClientes() {
        try {
            cmbCliente.getItems().setAll(clienteService.listar());
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }

    private void carregarProdutos() {
        try {
            cmbProduto.getItems().setAll(produtoService.listar());
            cmbProduto.setValue(null);
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }

    private void carregarHistorico() {
        try {
            tblPedidos.getItems().setAll(pedidoService.listar());
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }

    private void configurarColunasItens(TableColumn<ItemPedido, String> produto,
                                        TableColumn<ItemPedido, Integer> quantidade,
                                        TableColumn<ItemPedido, BigDecimal> preco,
                                        TableColumn<ItemPedido, BigDecimal> subtotal) {
        produto.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProduto().getNome()));
        quantidade.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getQuantidade()));
        preco.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getPrecoUnitario()));
        subtotal.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getSubtotal()));
        formatarMoeda(preco);
        formatarMoeda(subtotal);
    }

    private <T> void formatarMoeda(TableColumn<T, BigDecimal> coluna) {
        coluna.setCellFactory(col -> new TableCell<T, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal valor, boolean vazio) {
                super.updateItem(valor, vazio);
                setText(vazio || valor == null ? null : MOEDA.format(valor));
            }
        });
    }
}
