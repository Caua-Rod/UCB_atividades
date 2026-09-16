package entidades.jogador;

public class Guerreiro extends Player {
    public Guerreiro(String nome) {
        super(nome, 1, "guerreiro");
    }

    @Override
    public void distribuirAtributosIniciais() {
        this.forca = 8;
        this.resistencia = 10;
        this.agilidade = 5;
        this.inteligencia = 5;
    }
}