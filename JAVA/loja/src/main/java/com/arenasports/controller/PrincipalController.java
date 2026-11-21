package com.arenasports.controller;

import java.io.IOException;
import java.net.URL;

import com.arenasports.MainApp;
import com.arenasports.model.Usuario;
import com.arenasports.service.AuthService;
import com.arenasports.util.Alertas;
import com.arenasports.util.Sessao;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class PrincipalController {
    @FXML private Label lblUsuario;
    @FXML private Button btnUsuarios;
    @FXML private StackPane areaConteudo;

    private final AuthService authService = new AuthService();

    @FXML
    private void initialize() {
        Sessao sessao = Sessao.getInstance();
        Usuario usuario = sessao.getUsuarioLogado();
        lblUsuario.setText(usuario.getNome() + " (" + usuario.getPerfil().getRotulo() + ")");

        btnUsuarios.setVisible(sessao.isAdmin());
        btnUsuarios.setManaged(sessao.isAdmin());

        abrirInicio();
    }

    @FXML private void abrirInicio()     { abrir("/view/inicio.fxml"); }
    @FXML private void abrirProdutos()   { abrir("/view/produtos.fxml"); }
    @FXML private void abrirCategorias() { abrir("/view/categorias.fxml"); }
    @FXML private void abrirClientes()   { abrir("/view/clientes.fxml"); }
    @FXML private void abrirPedidos()    { abrir("/view/pedidos.fxml"); }
    @FXML private void abrirUsuarios()   { abrir("/view/usuarios.fxml"); }

    @FXML
    private void sair() {
        if (Alertas.confirmar("Deseja sair do sistema?")) {
            authService.sair();
            MainApp.mostrarLogin();
        }
    }

    private void abrir(String caminhoFxml) {
        URL url = getClass().getResource(caminhoFxml);
        if (url == null) {
            Alertas.info("Esta tela ainda não foi criada.");
            return;
        }
        try {
            Node tela = FXMLLoader.load(url);
            areaConteudo.getChildren().setAll(tela);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            Alertas.erro("Não foi possível abrir a tela. Veja o console para mais detalhes.");
        }
    }
}
