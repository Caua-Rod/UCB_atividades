public class Cachorro extends Mamifero {

    public Cachorro(String nome, double peso, int idade) {
        super(nome, peso, idade, "Pelo curto");
    }

    @Override
    public void emitirSom() {
        System.out.println(getNome() + " diz: Au au!");
    }

    @Override
    public void mover() {
        System.out.println(getNome() + " corre em quatro patas.");
    }
}