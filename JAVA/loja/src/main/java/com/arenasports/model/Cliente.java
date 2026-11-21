package com.arenasports.model;

import com.arenasports.util.NegocioException;

public class Cliente extends Pessoa {
    private String email;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String getTipo() {
        return "Cliente";
    }

    @Override
    public void validar() {
        super.validar();
        if (email != null && !email.isBlank()) {
            if (email.trim().length() > 100) {
                throw new NegocioException("O e-mail pode ter no máximo 100 caracteres.");
            }
            if (!email.trim().matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
                throw new NegocioException("E-mail inválido.");
            }
        }
    }
}
