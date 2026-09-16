package entidades.jogador;

public class Mago extends Player {
    private static final int CUSTO_MANA_ESPECIAL = 5;
    public Mago(String nome) {
        super(nome, 1, "mago");
    }

    @Override
    public void distribuirAtributosIniciais() {
        this.forca = 3;
        this.resistencia = 6;
        this.agilidade = 3;
        this.inteligencia = 8;
    }

    @Override
    public boolean temHabilidadeEspecial() {
        return getManaAtual() >= CUSTO_MANA_ESPECIAL;
    }

    @Override
    public int usarHabilidadeEspecial() {
        gastarMana(CUSTO_MANA_ESPECIAL);
        return getDano() * 5;
    }
}