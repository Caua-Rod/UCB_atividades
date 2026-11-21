package com.arenasports.model;

public enum Perfil {
    ADMIN("Administrador"),
    OPERADOR("Operador");

    private final String rotulo;

    Perfil(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }

    public static Perfil deTexto(String texto) {
        for (Perfil p : values()) {
            if (p.name().equalsIgnoreCase(texto)) {
                return p;
            }
        }
        return OPERADOR;
    }

    @Override
    public String toString() {
        return rotulo;
    }
}
