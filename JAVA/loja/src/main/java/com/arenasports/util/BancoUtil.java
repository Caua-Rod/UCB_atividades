package com.arenasports.util;

import java.sql.SQLException;

public final class BancoUtil {
    private BancoUtil() {
    }

    public static DaoException traduzir(SQLException e, String acao) {
        switch (e.getErrorCode()) {
            case 1062:
                return new DaoException("Já existe um registro com esses dados (valor duplicado).", e);
            case 1451:
                return new DaoException("Não foi possível excluir: este registro está sendo usado em outro cadastro.", e);
            case 1452:
                return new DaoException("Não foi possível salvar: um registro relacionado não existe mais.", e);
            default:
                return new DaoException("Erro ao " + acao + " no banco de dados.", e);
        }
    }

    public static String vazioParaNulo(String texto) {
        return (texto == null || texto.isBlank()) ? null : texto.trim();
    }
}
