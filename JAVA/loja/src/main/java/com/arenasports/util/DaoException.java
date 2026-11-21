package com.arenasports.util;

public class DaoException extends RuntimeException {
    public DaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public DaoException(String mensagem) {
        super(mensagem);
    }
}
