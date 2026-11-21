package com.arenasports.util;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public final class TelaUtil {
    private TelaUtil() {
    }

    public static void tratarErro(RuntimeException e) {
        if (e instanceof NegocioException) {
            Alertas.aviso(e.getMessage());
        } else if (e instanceof DaoException) {
            Alertas.erro(e.getMessage());
        } else {
            e.printStackTrace();
            Alertas.erro("Ocorreu um erro inesperado. Tente novamente.");
        }
    }

    public static void aplicarFiltro(TextField campo, String regex) {
        campo.setTextFormatter(new TextFormatter<String>(
                mudanca -> mudanca.getControlNewText().matches(regex) ? mudanca : null));
    }
}
