public abstract class Mamifero extends Animal {
    private String tipoPelagem;

    public Mamifero(String nome, double peso, int idade, String tipoPelagem) {
        super(nome, peso, idade);
        this.tipoPelagem = tipoPelagem;
    }

    public String getTipoPelagem() {
        return tipoPelagem;
    }

    public void setTipoPelagem(String tipoPelagem) {
        this.tipoPelagem = tipoPelagem;
    }

    public void amamentar() {
        System.out.println(getNome() + " está amamentando os filhotes.");
    }
}