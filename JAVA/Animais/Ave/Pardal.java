public class Pardal extends Ave {

    public Pardal(String nome, double peso, int idade) {
        super(nome, peso, idade, true);
    }

    @Override
    public void emitirSom() {
        System.out.println(getNome() + " canta: Piu piu!");
    }

    @Override
    public void mover() {
        System.out.println(getNome() + " voa rapidamente entre as arvores.");
    }
}