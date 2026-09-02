import java.util.ArrayList;

public class Time {
    private String nome;
    private int anoFundacao;
    private String cidade;
    private ArrayList<Jogador> jogadores;
    private ArrayList<Premiacao> premios;

    public Time(String nome, int anoFundacao, String cidade) {
        this.nome = nome;
        this.anoFundacao = anoFundacao;
        this.cidade = cidade;
        jogadores = new ArrayList<Jogador>();
        premios = new ArrayList<Premiacao>();
    }

    public String getNome() {
        return nome;
    }

    public int getAnoFundacao() {
        return anoFundacao;
    }

    public String getCidade() {
        return cidade;
    }

    public ArrayList<Jogador> getJogadores() {
        return jogadores;
    }

    public ArrayList<Premiacao> getPremios() {
        return premios;
    }

    public void adicionarJogador(Jogador jogador) {
        jogadores.add(jogador);
    }

    public void adicionarPremiacao(Premiacao premiacao) {
        premios.add(premiacao);
    }

}