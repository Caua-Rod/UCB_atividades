public abstract class Reptil extends Animal {
    private boolean possuiCasco;

    public Reptil(String nome, double peso, int idade, boolean possuiCasco) {
        super(nome, peso, idade);
        this.possuiCasco = possuiCasco;
    }

    public boolean isPossuiCasco() {
        return possuiCasco;
    }

    public void setPossuiCasco(boolean possuiCasco) {
        this.possuiCasco = possuiCasco;
    }

    public void trocarPele() {
        System.out.println(getNome() + " está trocando de pele.");
    }
}