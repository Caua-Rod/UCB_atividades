package com.arenasports.service;

import java.util.List;

import com.arenasports.dao.ClienteDAO;
import com.arenasports.model.Cliente;
import com.arenasports.util.NegocioException;
import com.arenasports.util.Sessao;

public class ClienteService {
    private final ClienteDAO clienteDAO = new ClienteDAO();

    public List<Cliente> listar() {
        Sessao.getInstance().exigirLogin();
        return clienteDAO.listar();
    }

    public List<Cliente> buscar(String termo) {
        Sessao.getInstance().exigirLogin();
        if (termo == null || termo.isBlank()) {
            return clienteDAO.listar();
        }
        return clienteDAO.buscarPorNome(termo);
    }

    public void salvar(Cliente cliente) {
        Sessao.getInstance().exigirLogin();
        cliente.validar();
        if (clienteDAO.existeEmail(cliente.getEmail(), cliente.getId())) {
            throw new NegocioException("Já existe um cliente com esse email.");
        }
        if (cliente.getId() == null) {
            clienteDAO.inserir(cliente);
        } else {
            clienteDAO.atualizar(cliente);
        }
    }

    public void excluir(int id) {
        Sessao.getInstance().exigirAdmin();
        clienteDAO.excluir(id);
    }
}
