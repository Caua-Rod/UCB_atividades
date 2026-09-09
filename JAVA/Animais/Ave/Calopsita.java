public class Calopsita extends Ave {

    public Calopsita(String nome, double peso, int idade) {
        super(nome, peso, idade, true);
    }

    @Override
    public void emitirSom() {
        System.out.println(getNome() + " assobia uma melodia.");
    }

    @Override
    public void mover() {
        System.out.println(getNome() + " voa de galho em galho.");
    }
}