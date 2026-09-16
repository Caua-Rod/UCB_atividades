package entidades.npcs;

import entidades.jogador.Player;

public class QuestGiver extends NPC {
    private int recompensaOuro;

    public QuestGiver(String nome, int recompensaOuro) {
        super(nome, 1, "Quest Giver");
        this.recompensaOuro = recompensaOuro;
    }

    public boolean missaoCumprida(Player jogador) {
        return jogador.isEncontroBConcluido();
    }

    public void entregarRecompensa(Player jogador) {
        jogador.receberOuro(recompensaOuro);
    }
}