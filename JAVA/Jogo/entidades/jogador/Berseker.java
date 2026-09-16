package entidades.jogador;

public class Berseker extends Player {
    public Berseker(String nome) {
        super(nome, 1, "berseker");
    }

    @Override
    public void distribuirAtributosIniciais() {
        this.forca = 10;
        this.resistencia = 14;
        this.agilidade = 3;
        this.inteligencia = 1;
    }
}