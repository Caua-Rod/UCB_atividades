public class Arara extends Ave {

    public Arara(String nome, double peso, int idade) {
        super(nome, peso, idade, true);
    }

    @Override
    public void emitirSom() {
        System.out.println(getNome() + " grasna bem alto!");
    }

    @Override
    public void mover() {
        System.out.println(getNome() + " voa exibindo suas penas coloridas.");
    }
}