package com.arenasports.dao;

public interface Crud<T> extends Leitura<T> {
    void inserir(T objeto);

    void atualizar(T objeto);

    void excluir(int id);
}
