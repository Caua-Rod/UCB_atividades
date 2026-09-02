public class Premiacao {
    private String titulo;
    private int ano;
    private String categoria;

    public Premiacao(String titulo, int ano, String categoria) {
        this.titulo = titulo;
        this.ano = ano;
        this.categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getAno() {
        return ano;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}