package entidades.inimigos;

import entidades.Entidade;
import entidades.jogador.Player;

public abstract class Enemy extends Entidade {
    protected  int forca;
    protected  int resistencia;
    private    int vidaAtual; 

    public Enemy(String nome,int level, String tipo) {
        super(nome, level, tipo);
        distribuirAtributos();
        this.vidaAtual = getVidaMaxima();
    }

    public int getVidaMaxima() {
        return resistencia;
    }

    public int getDano() {
        return forca;
    }

    public abstract void distribuirAtributos();

        public int getVidaAtual() {
        return vidaAtual;
    }

    public boolean estaVivo() {
        return vidaAtual > 0;
    }

    public void receberDano(int dano) {
        vidaAtual = Math.max(0, vidaAtual - dano);
    }

    public void atacar(Player alvo) {
        alvo.receberDano(this.getDano());
    }
    
}
