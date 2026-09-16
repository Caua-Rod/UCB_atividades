package entidades;

public abstract class Entidade {
    private String nome;
    protected int level;
    private String tipo;

    public Entidade(String nome, int level, String tipo) {
        this.nome = nome;
        this.level = level;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDadosBasicos() {
        return "Nome: " + nome + " Level: " + level + " Tipo: " + tipo;
    }
}