package com.arenasports.model;

import com.arenasports.util.NegocioException;

public abstract class Pessoa implements Validavel {
    private Integer id;
    private String nome;
    private String telefone;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public abstract String getTipo();

    public String getDescricao() {
        return getTipo() + ": " + nome;
    }

    @Override
    public void validar() {
        if (nome == null || nome.isBlank()) {
            throw new NegocioException("Informe o nome.");
        }
        if (nome.trim().length() > 100) {
            throw new NegocioException("O nome pode ter no máximo 100 caracteres.");
        }
        if (telefone != null && !telefone.isBlank()) {
            int digitos = telefone.replaceAll("\\D", "").length();
            if (digitos < 10 || digitos > 11) {
                throw new NegocioException("Telefone inválido. Use DDD + número (10 ou 11 dígitos).");
            }
        }
    }

    @Override
    public String toString() {
        return nome;
    }
}
