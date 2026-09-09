public class Gato extends Mamifero {

    public Gato(String nome, double peso, int idade) {
        super(nome, peso, idade, "Pelo macio");
    }

    @Override
    public void emitirSom() {
        System.out.println(getNome() + " diz: Miau!");
    }

    @Override
    public void mover() {
        System.out.println(getNome() + " caminha silenciosamente.");
    }
}