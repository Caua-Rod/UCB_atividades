package entidades.jogador;

import entidades.Entidade;
import entidades.inimigos.Enemy;

public abstract class Player extends Entidade {
    protected  int forca;
    protected  int resistencia;
    protected  int agilidade;
    protected  int inteligencia;
    private    int vidaAtual; 
    private    int manaAtual;

    public Player(String nome, int level, String tipo) {
        super(nome, level, tipo);
        distribuirAtributosIniciais(); 
        this.vidaAtual = getVidaMaxima();
        this.manaAtual = getManaMaxima();
    }

    public int getVidaMaxima() {
        return 2 * resistencia;
    }

    public int getDano() {
        return (int) (0.5 * forca);
    }

    public int getManaMaxima() {
        return 2 * inteligencia;
    }

    public int getManaAtual() {
        return manaAtual;
    }

    public int getAgilidade() {
        return agilidade;
    }

    public int getForca() { 
        return forca; 
    }

    public int getResistencia() { 
        return resistencia; 
    }

    public int getInteligencia() { 
        return inteligencia; 
    }

    public abstract void distribuirAtributosIniciais();

    public void ganharNivel() {
        level++;
        pontosDisponiveis++;
        vidaAtual = getVidaMaxima();
        manaAtual = getManaMaxima();
    }

    public boolean investirPonto(String atributo) {
        if (pontosDisponiveis <= 0) {
            return false;
        }
        switch (atributo) {
            case "forca": forca++; 
                break;
            case "resistencia": resistencia++;
            vidaAtual = getVidaMaxima(); 
                break;
            case "agilidade": agilidade++; 
                break;
            case "inteligencia": inteligencia++;
            manaAtual = getManaMaxima(); 
                break;
            default: return false;
        }
        pontosDisponiveis--;
        return true;
    }

    public int getPontosDisponiveis() {
        return pontosDisponiveis;
    }

    public int getVidaAtual() {
        return vidaAtual;
    }

    public boolean estaVivo() {
        return vidaAtual > 0;
    }

    public void receberDano(int dano) {
        vidaAtual = Math.max(0, vidaAtual - dano);
    }

    public void atacar(Enemy alvo) {
        alvo.receberDano(this.getDano());
    }

    public void aumentarForca(int valor){
        this.forca += valor;
    }
    
    public void aumentarResistencia(int valor){
        this.resistencia += valor;
    }
    
    public void aumentarAgilidade(int valor){
        this.agilidade += valor;
    }

    public void aumentarInteligencia(int valor){
        this.inteligencia += valor;
    }


    private int pontosDisponiveis;
    private int ouro;

    public void receberOuro(int quantidade) {
        ouro += quantidade;
    }

    public int getOuro() {
        return ouro;
    }

    public void gastarOuro(int quantidade) {
        this.ouro -= quantidade;
    }

    private boolean encontroBConcluido = false;

    public boolean isEncontroBConcluido() {
        return encontroBConcluido;
    }

    public void marcarEncontroBConcluido() {
        this.encontroBConcluido = true;
    }
    
    public boolean temHabilidadeEspecial() {
        return false;
    }

    public int usarHabilidadeEspecial() {
        return getDano();
    }

    public boolean gastarMana(int custo) {
        if (manaAtual < custo) return false;
        manaAtual -= custo;
        return true;
    }
}
