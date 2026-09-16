package entidades.npcs;

import java.util.List;
import entidades.Itens;
import entidades.jogador.Player;

public class Comerciante extends NPC {
    private List<Itens> itensDisponiveis;

    public Comerciante(String nome, List<Itens> itensDisponiveis) {
        super(nome, 1, "Comerciante");
        this.itensDisponiveis = itensDisponiveis;
    }

    public List<Itens> getItensDisponiveis() {
        return itensDisponiveis;
    }

    public boolean venderItem(Player comprador, int indice) {
        Itens item = itensDisponiveis.get(indice);
            if (comprador.getOuro() < item.getPreco()) {
                System.out.println("Ouro insuficiente");
                return false;
            }
        comprador.gastarOuro(item.getPreco());
            item.aplicarEfeito(comprador);
            return true;
    }
}