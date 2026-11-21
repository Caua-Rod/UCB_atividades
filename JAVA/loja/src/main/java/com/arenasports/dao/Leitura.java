package com.arenasports.dao;

import java.util.List;

public interface Leitura<T> {

    T buscarPorId(int id);

    List<T> listar();
}
