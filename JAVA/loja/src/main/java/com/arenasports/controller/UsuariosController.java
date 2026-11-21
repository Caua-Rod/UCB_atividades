package com.arenasports.controller;

import com.arenasports.model.Perfil;
import com.arenasports.model.Usuario;
import com.arenasports.service.UsuarioService;
import com.arenasports.util.Alertas;
import com.arenasports.util.TelaUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class UsuariosController {
    @FXML private TableView<Usuario> tblUsuarios;
    @FXML private TableColumn<Usuario, String> colNome;
    @FXML private TableColumn<Usuario, String> colLogin;
    @FXML private TableColumn<Usuario, String> colPerfil;
    @FXML private TableColumn<Usuario, String> colAtivo;

    @FXML private Label lblFormTitulo;
    @FXML private TextField txtNome;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtLogin;
    @FXML private Label lblSenha;
    @FXML private PasswordField txtSenha;
    @FXML private ComboBox<Perfil> cmbPerfil;
    @FXML private CheckBox chkAtivo;

    private final UsuarioService usuarioService = new UsuarioService();

    private Usuario selecionado;

    @FXML
    private void initialize() {
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colLogin.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLogin()));
        colPerfil.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPerfil().getRotulo()));
        colAtivo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isAtivo() ? "Sim" : "Não"));

        cmbPerfil.getItems().setAll(Perfil.values());

        TelaUtil.aplicarFiltro(txtNome, ".{0,100}");
        TelaUtil.aplicarFiltro(txtTelefone, "[\\d ()\\-]{0,15}");
        TelaUtil.aplicarFiltro(txtLogin, "[A-Za-z0-9._]{0,30}");
        TelaUtil.aplicarFiltro(txtSenha, ".{0,50}");

        tblUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                preencherFormulario(novo);
            }
        });

        carregarTabela();
        novo();
    }

    @FXML
    private void novo() {
        selecionado = null;
        tblUsuarios.getSelectionModel().clearSelection();
        txtNome.clear();
        txtTelefone.clear();
        txtLogin.clear();
        txtSenha.clear();
        cmbPerfil.setValue(Perfil.OPERADOR);
        chkAtivo.setSelected(true);
        lblFormTitulo.setText("Novo usuário");
        lblSenha.setText("Senha");
        txtNome.requestFocus();
    }

    @FXML
    private void salvar() {
        try {
            Usuario usuario = new Usuario();
            usuario.setId(selecionado == null ? null : selecionado.getId());
            usuario.setNome(txtNome.getText());
            usuario.setTelefone(txtTelefone.getText());
            usuario.setLogin(txtLogin.getText());
            usuario.setPerfil(cmbPerfil.getValue());
            usuario.setAtivo(chkAtivo.isSelected());
            usuario.setSenhaHash(selecionado == null ? null : selecionado.getSenhaHash());

            usuarioService.salvar(usuario, txtSenha.getText());

            Alertas.info("Usuário salvo com sucesso.");
            novo();
            carregarTabela();
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }

    @FXML
    private void excluir() {
        if (selecionado == null) {
            Alertas.aviso("Selecione um usuário na tabela para excluir.");
            return;
        }
        if (!Alertas.confirmar("Excluir o usuário \"" + selecionado.getLogin() + "\"?")) {
            return;
        }
        try {
            usuarioService.excluir(selecionado.getId());
            Alertas.info("Usuário excluído.");
            novo();
            carregarTabela();
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }

    private void carregarTabela() {
        try {
            tblUsuarios.getItems().setAll(usuarioService.listar());
        } catch (RuntimeException e) {
            TelaUtil.tratarErro(e);
        }
    }

    private void preencherFormulario(Usuario usuario) {
        selecionado = usuario;
        txtNome.setText(usuario.getNome());
        txtTelefone.setText(usuario.getTelefone() == null ? "" : usuario.getTelefone());
        txtLogin.setText(usuario.getLogin());
        txtSenha.clear();
        cmbPerfil.setValue(usuario.getPerfil());
        chkAtivo.setSelected(usuario.isAtivo());
        lblFormTitulo.setText("Editar usuário");
        lblSenha.setText("Nova senha (em branco = manter a atual)");
    }
}
