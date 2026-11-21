package com.arenasports.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public final class Alertas {
    private Alertas() {
    }

    public static void info(String mensagem) {
        mostrar(AlertType.INFORMATION, "Informação", mensagem);
    }

    public static void aviso(String mensagem) {
        mostrar(AlertType.WARNING, "Atenção", mensagem);
    }

    public static void erro(String mensagem) {
        mostrar(AlertType.ERROR, "Erro", mensagem);
    }

    public static boolean confirmar(String mensagem) {
        Alert alerta = new Alert(AlertType.CONFIRMATION, mensagem, ButtonType.YES, ButtonType.NO);
        alerta.setTitle("Confirmação");
        alerta.setHeaderText(null);
        return alerta.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    private static void mostrar(AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo, mensagem, ButtonType.OK);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}
