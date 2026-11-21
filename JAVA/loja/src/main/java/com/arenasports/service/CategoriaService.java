package com.arenasports.service;

import java.util.List;

import com.arenasports.dao.CategoriaDAO;
import com.arenasports.model.Categoria;
import com.arenasports.util.NegocioException;
import com.arenasports.util.Sessao;

public class CategoriaService {
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    public List<Categoria> listar() {
        Sessao.getInstance().exigirLogin();
        return categoriaDAO.listar();
    }

    public void salvar(Categoria categoria) {
        Sessao.getInstance().exigirLogin();
        categoria.validar();
        if (categoriaDAO.existeNome(categoria.getNome(), categoria.getId())) {
            throw new NegocioException("Já existe uma categoria com esse nome.");
        }
        if (categoria.getId() == null) {
            categoriaDAO.inserir(categoria);
        } else {
            categoriaDAO.atualizar(categoria);
        }
    }

    public void excluir(int id) {
        Sessao.getInstance().exigirAdmin();
        categoriaDAO.excluir(id);
    }
}
