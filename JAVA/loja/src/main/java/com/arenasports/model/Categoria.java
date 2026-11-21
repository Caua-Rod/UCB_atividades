package com.arenasports.model;

import com.arenasports.util.NegocioException;

public class Categoria implements Validavel {
    private Integer id;
    private String nome;

    public Categoria() {
    }

    public Categoria(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    @Override
    public void validar() {
        if (nome == null || nome.isBlank()) {
            throw new NegocioException("Informe o nome da categoria.");
        }
        if (nome.trim().length() > 50) {
            throw new NegocioException("O nome da categoria pode ter no máximo 50 caracteres.");
        }
    }

    @Override
    public String toString() {
        return nome;
    }
}
