package entidades.npcs;

import entidades.Entidade;

public abstract class NPC extends Entidade {

    public NPC(String nome, int level, String tipo) {
        super(nome, 1, tipo);
    }   
}