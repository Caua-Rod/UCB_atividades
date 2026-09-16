package entidades.jogador;

public class Assasino extends Player {
    public Assasino(String nome) {
        super(nome, 1, "assasino");
    }

    @Override
    public void distribuirAtributosIniciais() {
        this.forca = 6;
        this.resistencia = 8;
        this.agilidade = 10;
        this.inteligencia = 5;
    }
}