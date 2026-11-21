package com.arenasports.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexao {
    private static final Conexao INSTANCIA = new Conexao();

    private final String url;
    private final String usuario;
    private final String senha;

    private Conexao() {
        Properties config = new Properties();
        try (InputStream in = Conexao.class.getResourceAsStream("/database.properties")) {
            if (in == null) {
                throw new DaoException("Arquivo database.properties não encontrado em src/main/resources. "
                        + "Copie o database.properties.example e preencha com os seus dados.");
            }
            config.load(in);
        } catch (IOException e) {
            throw new DaoException("Não foi possível ler o database.properties.", e);
        }
        this.url = valor("DB_URL", config.getProperty("db.url"));
        this.usuario = valor("DB_USER", config.getProperty("db.user"));
        this.senha = valor("DB_PASSWORD", config.getProperty("db.password", ""));
    }

    private static String valor(String variavelAmbiente, String valorArquivo) {
        String doAmbiente = System.getenv(variavelAmbiente);
        return (doAmbiente != null && !doAmbiente.isBlank()) ? doAmbiente : valorArquivo;
    }

    public static Conexao getInstance() {
        return INSTANCIA;
    }

    public Connection obterConexao() {
        if (url == null || url.isBlank() || usuario == null || usuario.isBlank()) {
            throw new DaoException("Configure db.url e db.user no database.properties.");
        }
        try {
            return DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            throw new DaoException("Não foi possível conectar ao banco de dados. Verifique se o MySQL está ligado "
                    + "e se o usuário e a senha do database.properties estão corretos.", e);
        }
    }
}
