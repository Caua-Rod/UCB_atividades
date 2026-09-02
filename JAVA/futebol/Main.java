public class Main {
    public static void main(String[] args) {
        Time flamengo = new Time("Flamengo", 1990, "RJ");

        Premiacao mundial = new Premiacao("Mundial", 2010, "INTER");

        Jogador jogador1 = new Jogador("Bruno", 1, "GL");
        Jogador jogador2 = new Jogador("Gabi", 10, "MEI");

        flamengo.adicionarJogador(jogador1);
        flamengo.adicionarJogador(jogador2);
        flamengo.adicionarPremiacao(mundial);

        System.out.println("------FLAMENGO------");

        for (Jogador j: flamengo.getJogadores()) {
            System.out.println("Nome: " + j.getNome());
            System.out.println("Numero: " + j.getNumeroCamisa());
            System.out.println("Posicao: " + j.getPosicao());
        }

        for (Premiacao p: flamengo.getPremios()) {
            System.out.println("Titulo: " + p.getTitulo());
            System.out.println("Ano: " + p.getAno());
            System.out.println("Categoria: " + p.getCategoria());
        }

    }
}