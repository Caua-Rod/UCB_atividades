package com.arenasports.controller;

import com.arenasports.model.Cliente;
import com.arenasports.service.ClienteService;
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

public class ClientesController {
    @FXML private TextField txtBusca;
    @FXML private TableView<Cliente> tblClientes;
    @FXML private TableColumn<Cliente, String> colNome;
    @FXML private TableColumn<Cliente, String> colTelefone;
    @FXML private TableColumn<Cliente, String> colEmail;

    @FXML private Label lblFormTitulo;
    @FXML private TextField txtNome;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEmail;
    @FXML private Button btnExcluir;

    private final ClienteService clienteService = new ClienteService();

    private Cliente selecionado;

    @FXML
    private void initialize() {
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colTelefone.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefone()));
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));

        TelaUtil.aplicarFiltro(txtNome, ".{0,100}");
        TelaUtil.aplicarFiltro(txtTelefone, "[\\d ()\\-]{0,15}");
        TelaUtil.aplicarFiltro(txtEmail, ".{0,100}");
        TelaUtil.aplicarFiltro(txtBusca, ".{0,100}");

        btnExcluir.setVisible(Sessao.getInstance().isAdmin());
        btnExcluir.setManaged(Sessao.getInstance().isAdmin());

        tblClientes.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                preencherFormulario(novo);
            }
        });

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
        tblClientes.getSelectionModel().clearSelection();
        txtNome.clear();
        txtTelefone.clear();
        txtEmail.clear();
        lblFormTitulo.setText("Novo cliente");
        txtNome.requestFocus();
    }

    @FXML
    private void salvar() {
        try {
            Cliente cliente = new Cliente();
            cliente.setId(selecionado == null ? null : selecionado.getId());
            cliente.setNome(txtNome.getText());
            cliente.setTelefone(txtTelefone.getText());
            cliente.setEmail(txtEmail.getText());

            clienteService.salvar(cliente);

            Alertas.info("Cliente salvo com sucesso.");
            novo();
            carregarTabela(txtBusca.getText());
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }

    @FXML
    private void excluir() {
        if (selecionado == null) {
            Alertas.aviso("Selecione um cliente na tabela para excluir.");
            return;
        }
        if (!Alertas.confirmar("Excluir o cliente \"" + selecionado.getNome() + "\"?")) {
            return;
        }
        try {
            clienteService.excluir(selecionado.getId());
            Alertas.info("Cliente excluído.");
            novo();
            carregarTabela(txtBusca.getText());
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }

    private void carregarTabela(String termo) {
        try {
            tblClientes.getItems().setAll(clienteService.buscar(termo));
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }

    private void preencherFormulario(Cliente cliente) {
        selecionado = cliente;
        txtNome.setText(cliente.getNome());
        txtTelefone.setText(cliente.getTelefone() == null ? "" : cliente.getTelefone());
        txtEmail.setText(cliente.getEmail() == null ? "" : cliente.getEmail());
        lblFormTitulo.setText("Editar cliente");
    }
}
