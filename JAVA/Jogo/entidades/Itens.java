package entidades;

import entidades.jogador.Player;

public class Itens {
    private String nome;
    private String atributoModificado;
    private int valorModificador;
    private int preco;

    public Itens(String nome, String atributoModificado, int valorModificador, int preco) {
        this.nome = nome;
        this.atributoModificado = atributoModificado;
        this.valorModificador = valorModificador;
        this.preco = preco;
    }

    public int getValorModificador() {
        return valorModificador;
    }
    
    public int getPreco() {
        return preco;
    }
    
    public String getNome() {
        return nome;
    }

    public String getAtributoModificado() {
        return atributoModificado;
    }

    public void aplicarEfeito(Player alvo) {
        switch(atributoModificado){ 

            case "resistencia": alvo.aumentarResistencia(valorModificador);
                break;
            case "forca": alvo.aumentarForca(valorModificador);
                break;
            case "inteligencia": alvo.aumentarInteligencia(valorModificador);
                break;
            case "agilidade": alvo.aumentarAgilidade(valorModificador);
                break;
        } 
    }
}