public class Iguana extends Reptil {

    public Iguana(String nome, double peso, int idade) {
        super(nome, peso, idade, false);
    }

    @Override
    public void emitirSom() {
        System.out.println(getNome() + " quase nao faz som.");
    }

    @Override
    public void mover() {
        System.out.println(getNome() + " rasteja e escala superficies.");
    }
}