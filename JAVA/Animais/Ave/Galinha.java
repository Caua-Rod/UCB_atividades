public class Galinha extends Ave {

    public Galinha(String nome, double peso, int idade) {
        super(nome, peso, idade, false);
    }

    @Override
    public void emitirSom() {
        System.out.println(getNome() + " diz: Cocorico!");
    }

    @Override
    public void mover() {
        System.out.println(getNome() + " anda pelo galinheiro.");
    }
}