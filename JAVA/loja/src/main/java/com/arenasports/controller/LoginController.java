package com.arenasports.controller;

import com.arenasports.MainApp;
import com.arenasports.service.AuthService;
import com.arenasports.util.Alertas;
import com.arenasports.util.DaoException;
import com.arenasports.util.NegocioException;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML private TextField txtLogin;
    @FXML private PasswordField txtSenha;

    private final AuthService authService = new AuthService();

    @FXML
    private void initialize() {
        limitarTamanho(txtLogin, 30);
        limitarTamanho(txtSenha, 50);
    }

    @FXML
    private void entrar() {
        try {
            authService.autenticar(txtLogin.getText(), txtSenha.getText());
            MainApp.mostrarPrincipal();
        } catch (NegocioException e) {
            Alertas.aviso(e.getMessage());
            txtSenha.clear();
            txtSenha.requestFocus();
        } catch (DaoException e) {
            Alertas.erro(e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            Alertas.erro("Ocorreu um erro inesperado. Tente novamente.");
        }
    }

    private void limitarTamanho(TextField campo, int maximo) {
        campo.textProperty().addListener((obs, antigo, novo) -> {
            if (novo != null && novo.length() > maximo) {
                campo.setText(antigo);
            }
        });
    }
}
