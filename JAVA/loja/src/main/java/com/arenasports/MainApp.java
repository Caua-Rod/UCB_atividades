package com.arenasports;

import java.io.IOException;

import com.arenasports.service.AuthService;
import com.arenasports.util.Alertas;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private static Stage palco;

    @Override
    public void start(Stage stage) {
        palco = stage;
        palco.setTitle("Arena Sports");

        try {
            new AuthService().garantirAdminInicial();
        } catch (RuntimeException e) {
            Alertas.erro(e.getMessage());
            Platform.exit();
            return;
        }
        mostrarLogin();
        palco.show();
    }

    public static void mostrarLogin() {
        trocarTela("/view/login.fxml", 480, 520);
    }

    public static void mostrarPrincipal() {
        trocarTela("/view/principal.fxml", 1100, 680);
    }

    private static void trocarTela(String caminhoFxml, double largura, double altura) {
        try {
            Parent raiz = FXMLLoader.load(MainApp.class.getResource(caminhoFxml));
            Scene cena = new Scene(raiz, largura, altura);
            cena.getStylesheets().add(MainApp.class.getResource("/css/estilo.css").toExternalForm());
            palco.setScene(cena);
            palco.centerOnScreen();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();  
            Alertas.erro("Não foi possível abrir a tela. Veja o console para mais detalhes.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
