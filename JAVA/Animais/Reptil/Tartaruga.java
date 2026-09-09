public class Tartaruga extends Reptil {

    public Tartaruga(String nome, double peso, int idade) {
        super(nome, peso, idade, true);
    }

    @Override
    public void emitirSom() {
        System.out.println(getNome() + " quase nao emite som.");
    }

    @Override
    public void mover() {
        System.out.println(getNome() + " se move bem devagar.");
    }
}